package com.viettel.luongnk.main.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.persistence.*;

/**
 * @author: luongnk
 * @since: 24/11/2020
 */

@ConfigurationProperties(prefix = "file")
@Entity
@Table(name = "merchant_documents")
@Data
public class DocumentStorageProperties {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer documentId;

    private Integer userId;
    private String fileName;
    private String documentType;
    private String documentFormat;
    private String uploadDir;
}
