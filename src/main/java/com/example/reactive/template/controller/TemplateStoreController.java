package com.example.reactive.template.controller;

import com.example.reactive.template.model.TemplateStoreModel;
import com.example.reactive.template.service.TemplateStoreService;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;

@RestController
@RequestMapping
public class TemplateStoreController {

    private final TemplateStoreService  templateStoreService;

    public TemplateStoreController(final TemplateStoreService templateStoreService) {
        this.templateStoreService = templateStoreService;
    }

    @GetMapping("/getAll")
    public final Flux<TemplateStoreModel> findAll() {
        return templateStoreService.findAll();
    }

    @GetMapping("/getById/{id}")
    public final Mono<TemplateStoreModel> findById(@PathVariable("id") final Long id) {
        return templateStoreService.findById(id);
    }

    @GetMapping("/getTemplateById/{id}")
    public final Mono<byte[]> getTemplateById(@PathVariable("id") final Long id) {
        return templateStoreService.findById(id).map(TemplateStoreModel::getTemplate);
    }

    @PostMapping(value = "/addTemplate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public final Mono<TemplateStoreModel> addTemplate(@RequestPart("file") FilePart file,
                                                      @RequestPart("templateName") String templateName,
                                                      @RequestPart("user") String user) throws IOException {
        return DataBufferUtils.join(file.content())
                .map(buffer -> {
                    byte[] bytes = new byte[buffer.readableByteCount()];
                    buffer.read(bytes);
                    DataBufferUtils.release(buffer);
                    return bytes;
                })
                .flatMap(bytes -> {
                    TemplateStoreModel templateStoreModel = TemplateStoreModel.builder()
                            .templateName(templateName)
                            .createdBy(user)
                            .createdDate(java.time.LocalDateTime.now())
                            .template(bytes)
                            .build();
                    return templateStoreService.addTemplate(templateStoreModel);
                });
    }

    @PostMapping(path = "/updateTemplate/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public final Mono<TemplateStoreModel> updateTemplate(@RequestPart("file") FilePart file,
                                                      @RequestPart("templateName") String templateName,
                                                      @RequestPart("user") String user,
                                                      @PathVariable("id") Long id)  {
       return DataBufferUtils.join(file.content())
                .map(buffer -> {
                    byte[] bytes = new byte[buffer.readableByteCount()];
                    buffer.read(bytes);
                    DataBufferUtils.release(buffer);
                    return bytes;
                })
                .flatMap(bytes -> {
                    TemplateStoreModel templateStoreModel = TemplateStoreModel.builder()
                            .templateStoreId(id)
                            .templateName(templateName)
                            .updatedBy(user)
                            .updatedDate(java.time.LocalDateTime.now())
                            .template(bytes)
                            .build();
                    return templateStoreService.updateTemplate(templateStoreModel);
                });
    }

    @DeleteMapping("/deleteTemplate/{id}")
    public final Mono<TemplateStoreModel> deleteTemplate(@PathVariable("id") final Long id){
        return templateStoreService.deleteTemplateById(id);
    }



}
