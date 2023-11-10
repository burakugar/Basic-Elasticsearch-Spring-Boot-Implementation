package com.vicarius.elasticsearch.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import javax.validation.constraints.NotNull;

public record IndexRequestDto(
        @JsonProperty("index")
        @NotNull(message = "Index name must not be blank")
        String indexName) {
    @Builder
    public IndexRequestDto {
    }
}


