package com.viettel.luongnk.main.document;

import com.viettel.luongnk.main.domain.UploadFileResponse;
import com.viettel.luongnk.main.service.DocumentStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author: luongnk
 * @since: 25/11/2020
 */

@Slf4j
@RequiredArgsConstructor
@RestController
public class DocumentController {

    private final DocumentStorageService documentStorageService;

    @PostMapping("/upload-file")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file,
                                         @RequestParam("user_id") Integer userId,
                                         @RequestParam("doc_type") String docType) {
        String fileName = documentStorageService.storeFile(file, userId, docType);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download-file/")
                .path(fileName)
                .toUriString();

        return new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());
    }

    @GetMapping("/download-file/{file_name}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("file_name") String fileName,
                                                 HttpServletRequest request) {

        if (fileName != null && !fileName.isEmpty()) {
            Resource resource = null;
            try {
                resource = documentStorageService.loadFileAsResource(fileName);
            } catch (FileNotFoundException e) {
                log.error("File not found: {}", e.getMessage(), e);
            }

            String contentType = null;

            try {
                assert resource != null;
                contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            } catch (IOException exception) {
                log.error("Could not determine file type: {}", exception.getMessage(), exception);
            }

            // default file type if not determined
            if (contentType == null) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        }

        return ResponseEntity.notFound().build();
    }
}
