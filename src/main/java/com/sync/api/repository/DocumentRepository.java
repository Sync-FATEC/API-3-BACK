package com.sync.api.repository;

import com.sync.api.model.Documents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface DocumentRepository extends JpaRepository<Documents,String> {

//    @Query("SELECT d  FROM Documents d WHERE d.projectId = ?1")
//    Collection<Documents> findByProjectId(String projectId);
}
