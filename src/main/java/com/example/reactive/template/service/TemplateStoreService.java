package com.example.reactive.template.service;

import com.example.reactive.template.entity.TemplateStore;
import com.example.reactive.template.model.TemplateStoreModel;
import com.example.reactive.template.repo.TemplateStoreRepo;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TemplateStoreService {

    private final TemplateStoreRepo templateStoreRepo;

    public TemplateStoreService(TemplateStoreRepo templateStoreRepo) {
        this.templateStoreRepo = templateStoreRepo;
    }

    public final Flux<TemplateStoreModel> findAll() {
        return templateStoreRepo.getAllTemplates().map(templateStore -> TemplateStoreModel.builder()
                .templateStoreId(templateStore.getTemplateStoreId())
                .templateName(templateStore.getTemplateName())
                .createdBy(templateStore.getCreatedBy())
                .createdDate(templateStore.getCreatedDate())
                .updatedBy(templateStore.getUpdatedBy())
                .updatedDate(templateStore.getUpdatedDate())
                .build());
    }

    public final Mono<TemplateStoreModel> findById(final Long id) {
        return templateStoreRepo.findById(id).map(templateStore -> TemplateStoreModel.builder()
                .templateStoreId(templateStore.getTemplateStoreId())
                .templateName(templateStore.getTemplateName())
                .createdBy(templateStore.getCreatedBy())
                .createdDate(templateStore.getCreatedDate())
                .updatedBy(templateStore.getUpdatedBy())
                .updatedDate(templateStore.getUpdatedDate())
                .template(templateStore.getTemplate())
                .build());
    }



    public final Mono<TemplateStoreModel> addTemplate(final TemplateStoreModel template){
            final TemplateStore entityTemplate = TemplateStore.builder()
                    .templateStoreId(template.getTemplateStoreId())
                    .templateName(template.getTemplateName())
                    .createdBy(template.getCreatedBy())
                    .createdDate(template.getCreatedDate())
                    .updatedBy(template.getUpdatedBy())
                    .updatedDate(template.getUpdatedDate())
                    .template(template.getTemplate())
                    .build();

            return templateStoreRepo.save(entityTemplate).map(savedTemplate -> TemplateStoreModel.builder()
                    .templateStoreId(savedTemplate.getTemplateStoreId())
                    .templateName(savedTemplate.getTemplateName())
                    .createdBy(savedTemplate.getCreatedBy())
                    .createdDate(savedTemplate.getCreatedDate())
                    .updatedBy(savedTemplate.getUpdatedBy())
                    .updatedDate(savedTemplate.getUpdatedDate())
                    .build());
    }

    public final Mono<TemplateStoreModel> updateTemplate(final TemplateStoreModel template){
        return templateStoreRepo.findById(template.getTemplateStoreId())
                .switchIfEmpty(Mono.error(new RuntimeException("Template not found with id: " + template.getTemplateStoreId())))
                .map(existingTemplate -> {
                    existingTemplate.setTemplateName(template.getTemplateName());
                    existingTemplate.setUpdatedBy(template.getUpdatedBy());
                    existingTemplate.setUpdatedDate(template.getUpdatedDate());
                    existingTemplate.setTemplate(template.getTemplate());
                    templateStoreRepo.save(existingTemplate).subscribe();

                    return template;
                });



    }

    public final Mono<TemplateStoreModel> deleteTemplateById(final Long templateId){
        return templateStoreRepo.findById(templateId).map(templateStore -> {
            templateStoreRepo.delete(templateStore).subscribe();
            return TemplateStoreModel.builder()
                    .templateStoreId(templateStore.getTemplateStoreId())
                    .templateName(templateStore.getTemplateName())
                    .createdBy(templateStore.getCreatedBy())
                    .createdDate(templateStore.getCreatedDate())
                    .updatedBy(templateStore.getUpdatedBy())
                    .updatedDate(templateStore.getUpdatedDate())
                    .build();
        }).switchIfEmpty(Mono.error(new RuntimeException("Template not found with id: " + templateId)));
    }


}
