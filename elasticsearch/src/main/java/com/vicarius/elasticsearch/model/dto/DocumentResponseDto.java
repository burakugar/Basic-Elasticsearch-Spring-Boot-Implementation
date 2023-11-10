package com.vicarius.elasticsearch.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

public record DocumentResponseDto(
        @NotBlank(message = "Document ID must not be blank")
        @JsonProperty("documentId")
        String documentId,

        @NotNull(message = "Fields must not be null")
        @JsonProperty("fields")
        Map<String, Object> fields) {
    @Builder
    public DocumentResponseDto {}
}