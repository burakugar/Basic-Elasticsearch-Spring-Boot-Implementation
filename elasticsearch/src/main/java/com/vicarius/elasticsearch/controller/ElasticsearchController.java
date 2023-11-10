package com.vicarius.elasticsearch.controller;

import com.vicarius.elasticsearch.model.dto.DocumentRequestDto;
import com.vicarius.elasticsearch.model.dto.DocumentResponseDto;
import com.vicarius.elasticsearch.model.dto.IndexRequestDto;
import com.vicarius.elasticsearch.service.ElasticsearchService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/es")
@AllArgsConstructor
public class ElasticsearchController {
    private final ElasticsearchService elasticsearchService;

    @PostMapping("/index")
    @ResponseStatus(HttpStatus.CREATED)
    public void createIndex(@RequestBody @Valid final IndexRequestDto indexRequestDto) {
        elasticsearchService.createIndex(indexRequestDto);
    }

    @PostMapping("/document")
    @ResponseStatus(HttpStatus.CREATED)
    public void createDocument(@RequestBody @Valid final DocumentRequestDto documentRequestDto) {
        elasticsearchService.createDocument(documentRequestDto);
    }

    @GetMapping("/document/{indexName}/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DocumentResponseDto getDocument(@PathVariable final String indexName, @PathVariable final String id) {
        return elasticsearchService.findDocumentById(indexName, id);
    }
}
