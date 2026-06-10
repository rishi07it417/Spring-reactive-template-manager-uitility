package com.example.reactive.template.repo;

import com.example.reactive.template.entity.TemplateStore;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface  TemplateStoreRepo extends ReactiveCrudRepository<TemplateStore, Long> {

    @Query("SELECT templateStoreId, templateName, createdBy, createdDate FROM templateStore")
    Flux<TemplateStore> getAllTemplates();

}
