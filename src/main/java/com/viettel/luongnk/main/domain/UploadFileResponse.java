package com.viettel.luongnk.main.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: luongnk
 * @since: 25/11/2020
 */

@Data
@AllArgsConstructor
public class UploadFileResponse {
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;
}
