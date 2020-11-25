package com.viettel.luongnk.main.service;

import com.viettel.luongnk.main.entity.DocumentStorageProperties;
import com.viettel.luongnk.main.exception.DocumentStorageException;
import com.viettel.luongnk.main.repository.DocumentStoragePropertiesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

/**
 * @author: luongnk
 * @since: 24/11/2020
 */

@Service
public class DocumentStorageService {
    private final DocumentStoragePropertiesRepository documentStorageRepository;

    private final Path fileStorageLocation;

    @Autowired
    public DocumentStorageService(DocumentStorageProperties storageProperties,
                                  DocumentStoragePropertiesRepository documentStorageRepository) {
        this.documentStorageRepository = documentStorageRepository;

        this.fileStorageLocation = Paths.get(storageProperties.getUploadDir()).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException exception) {
            throw new DocumentStorageException("Could not create the directory where the uploaded files will be stored.", exception);
        }
    }

    public String storeFile(MultipartFile file, Integer userId, String docType) {

        try {
            String fileName = generateFileName(file, userId, docType);

            saveFileToStorage(file, fileName);

            saveFileInfoToDatabase(file, userId, docType, fileName);

            return fileName;
        } catch (IOException ex) {
            throw new DocumentStorageException("Could not store file " + file.getOriginalFilename() + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(final String fileName) throws FileNotFoundException {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("File not found " + fileName);
        }
    }

    public String getDocumentName(Integer userId, String docType) {
        return documentStorageRepository.getUploadDocumentPath(userId, docType);
    }

    private void saveFileInfoToDatabase(MultipartFile file, Integer userId, String docType, String fileName) {
        DocumentStorageProperties doc =
                documentStorageRepository.findDocumentByUserIdAndDocumentType(userId, docType);

        if (doc != null) {
            doc.setDocumentFormat(file.getContentType());
            doc.setFileName(fileName);
            documentStorageRepository.save(doc);
        } else {
            DocumentStorageProperties newDoc = new DocumentStorageProperties();
            newDoc.setUserId(userId);
            newDoc.setDocumentFormat(file.getContentType());
            newDoc.setFileName(fileName);
            newDoc.setDocumentType(docType);
            documentStorageRepository.save(newDoc);
        }
    }

    private String generateFileName(MultipartFile file, Integer userId, String docType) {
        String fileExtension = getFileExtension(file);
        return userId + "_" + docType + fileExtension;
    }

    private String getFileExtension(MultipartFile file) throws DocumentStorageException {
        // Normalize file name
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        // Check if the file's name contains invalid characters
        if (originalFileName.contains("..")) {
            throw new DocumentStorageException("Sorry! Filename contains invalid path sequence " + originalFileName);
        }

        String fileExtension = "";

        try {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        } catch (Exception e) {
            fileExtension = "";
        }

        return fileExtension;
    }

    private void saveFileToStorage(MultipartFile file, String fileName) throws IOException {
        // Copy file to the target location (Replacing existing file with the same name)
        Path targetLocation = this.fileStorageLocation.resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
    }
}
