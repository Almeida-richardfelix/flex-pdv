package com.flexpdv.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void cleanProducts() {
        productRepository.deleteAll();
    }

    @Test
    void createsAndFindsProductByIdAndCode() throws Exception {
        String request = """
                {
                  "code": "7891234567890",
                  "name": "Café",
                  "price": 12.50,
                  "stock": 8
                }
                """;

        String response = mockMvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("7891234567890"))
                .andExpect(jsonPath("$.price").value(12.50))
                .andReturn()
                .getResponse()
                .getContentAsString();
        long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(get("/api/produtos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Café"));

        mockMvc.perform(get("/api/produtos/codigo/{code}", "7891234567890"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    void rejectsDuplicateCode() throws Exception {
        String request = """
                {
                  "code": "SKU-1",
                  "name": "Produto",
                  "price": 5.00,
                  "stock": 1
                }
                """;

        mockMvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isConflict());
    }

    @Test
    void rejectsInvalidProductAndReturnsNotFoundForUnknownCode() throws Exception {
        mockMvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "code": "",
                                  "name": "Produto",
                                  "price": -1,
                                  "stock": -1
                                }
                                """))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/api/produtos/codigo/INEXISTENTE"))
                .andExpect(status().isNotFound());
    }
}
