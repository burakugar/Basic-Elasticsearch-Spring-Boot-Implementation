package com.vicarius.elasticsearch.service.impl;

import com.vicarius.elasticsearch.model.dto.DocumentRequestDto;
import com.vicarius.elasticsearch.model.dto.DocumentResponseDto;
import com.vicarius.elasticsearch.model.dto.IndexRequestDto;

import java.io.IOException;

public interface ElasticsearchServiceImpl {
    DocumentResponseDto findDocumentById(final String indexName, final String documentId) throws IOException;

    void createDocument(final DocumentRequestDto documentRequestDto) throws IOException;

    void createIndex(final IndexRequestDto indexRequestDto) throws IOException;

}
