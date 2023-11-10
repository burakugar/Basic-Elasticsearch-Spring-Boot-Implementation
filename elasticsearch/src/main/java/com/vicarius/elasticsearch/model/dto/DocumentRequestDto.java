package com.vicarius.elasticsearch.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Map;
public record DocumentRequestDto(
        @JsonProperty("indexName")
        @NotBlank(message = "Index name must not be blank")
        String indexName,
        @JsonProperty("documentId")
        @NotBlank(message = "Document ID must not be blank")
        String documentId,
        @JsonProperty("fields")
        @Size(min = 1, message = "At least one field is required")
        Map<String, Object> fields
) {
    @Builder
    public DocumentRequestDto {
    }
}
