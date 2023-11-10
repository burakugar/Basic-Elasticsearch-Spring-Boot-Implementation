package com.vicarius.elasticsearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vicarius.elasticsearch.controller.ElasticsearchController;
import com.vicarius.elasticsearch.model.dto.DocumentRequestDto;
import com.vicarius.elasticsearch.model.dto.DocumentResponseDto;
import com.vicarius.elasticsearch.model.dto.IndexRequestDto;
import com.vicarius.elasticsearch.service.ElasticsearchService;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ElasticsearchApplicationTests {
    @Mock
    private ElasticsearchService elasticsearchService;

    @InjectMocks
    private ElasticsearchController elasticsearchController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = standaloneSetup(elasticsearchController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testCreateIndex() throws Exception {
        IndexRequestDto indexRequestDto = new IndexRequestDto("testIndex");

        mockMvc.perform(post("/api/v1/es/index")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(indexRequestDto)))
                .andExpect(status().isCreated());

        verify(elasticsearchService).createIndex(any(IndexRequestDto.class));
    }

    @Test
    public void testCreateDocument() throws Exception {
        DocumentRequestDto documentRequestDto = new DocumentRequestDto("testIndex", "doc1", Map.of("key", "value"));

        mockMvc.perform(post("/api/v1/es/document")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(documentRequestDto)))
                .andExpect(status().isCreated());

        verify(elasticsearchService).createDocument(any(DocumentRequestDto.class));
    }

    @Test
    public void testGetDocument() throws Exception {
        String indexName = "testIndex";
        String documentId = "doc1";
        DocumentResponseDto documentResponseDto = new DocumentResponseDto(documentId, Map.of("key", "value"));

        given(elasticsearchService.findDocumentById(indexName, documentId)).willReturn(documentResponseDto);

        mockMvc.perform(get("/api/v1/es/document/{indexName}/{id}", indexName, documentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.documentId").value(documentId))
                .andExpect(jsonPath("$.fields.key").value("value"));

        verify(elasticsearchService).findDocumentById(indexName, documentId);
    }
}