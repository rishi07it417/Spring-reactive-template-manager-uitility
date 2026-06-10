package com.example.reactive.template.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Builder
@Table("templatestore")
public class TemplateStore {
    @Id
    @Column("templateStoreId")
    private Long templateStoreId;

    @Column("templateName")
    private String templateName;

    @Column("createdBy")
    private String createdBy;

    @Column("createdDate")
    private LocalDateTime createdDate;

    @Column("updatedBy")
    private String updatedBy;

    @Column("updatedDate")
    private LocalDateTime updatedDate;

    @Lazy
    @Column("template")
    private byte[] template;

}
