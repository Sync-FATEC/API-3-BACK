package com.sync.api.infra.repository;

import com.sync.api.domain.model.Documents;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Documents,String> {

//    @Query("SELECT d  FROM Documents d WHERE d.projectId = ?1")
//    Collection<Documents> findByProjectId(String projectId);
}
