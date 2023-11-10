package com.vicarius.elasticsearch.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import com.vicarius.elasticsearch.exception.DocumentNotFoundException;
import com.vicarius.elasticsearch.exception.ElasticsearchOperationException;
import com.vicarius.elasticsearch.model.dto.DocumentRequestDto;
import com.vicarius.elasticsearch.model.dto.DocumentResponseDto;
import com.vicarius.elasticsearch.model.dto.IndexRequestDto;
import com.vicarius.elasticsearch.service.impl.ElasticsearchServiceImpl;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class ElasticsearchService implements ElasticsearchServiceImpl {
    private final ElasticsearchClient elasticsearchClient;

    public ElasticsearchService(final ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    @Override
    public void createIndex(final IndexRequestDto indexRequestDto) {
        try {
            var response = elasticsearchClient.indices().create(c -> c.index(indexRequestDto.indexName()));
            if (!response.acknowledged()) {
                throw new ElasticsearchOperationException("Index creation not acknowledged");
            }
        } catch (final IOException | ElasticsearchException e) {
            throw new ElasticsearchOperationException("Failed to create index: ", e);
        }
    }

    @Override
    public void createDocument(final DocumentRequestDto documentRequestDto) {
        try {
            elasticsearchClient.index(i -> i
                    .index(documentRequestDto.indexName())
                    .id(documentRequestDto.documentId())
                    .document(documentRequestDto.fields())
            );
        } catch (final IOException | ElasticsearchException e) {
            throw new ElasticsearchOperationException("Failed to create document", e);
        }
    }

    @Override
    public DocumentResponseDto findDocumentById(final String indexName, final String documentId) {
        try {
            var response = elasticsearchClient.get(g -> g
                    .index(indexName)
                    .id(documentId), Map.class);
            if (!response.found()) {
                throw new DocumentNotFoundException("Document with ID " + documentId + " not found");
            }
            return new DocumentResponseDto(documentId, response.source());
        } catch (final IOException | ElasticsearchException e) {
            throw new ElasticsearchOperationException("Failed to retrieve document", e);
        }
    }
}


