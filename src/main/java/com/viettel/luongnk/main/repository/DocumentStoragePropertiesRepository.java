package com.viettel.luongnk.main.repository;

import com.viettel.luongnk.main.entity.DocumentStorageProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author: luongnk
 * @since: 24/11/2020
 */

@Repository
public interface DocumentStoragePropertiesRepository extends JpaRepository<DocumentStorageProperties, Integer> {

    DocumentStorageProperties findDocumentByUserIdAndDocumentType(Integer userId, String documentType);

    @Query("select a.fileName from DocumentStorageProperties a where a.userId = ?1 and a.documentType = ?2")
    String getUploadDocumentPath(Integer userId, String docType);
}
