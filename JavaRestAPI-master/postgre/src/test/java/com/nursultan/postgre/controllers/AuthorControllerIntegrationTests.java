package com.nursultan.postgre.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nursultan.postgre.TestDataUtil;
import com.nursultan.postgre.domain.dto.AuthorDto;
import com.nursultan.postgre.domain.entities.AuthorEntity;
import com.nursultan.postgre.services.AuthorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class AuthorControllerIntegrationTests {
    private MockMvc mockMvc;

    private AuthorService authorService;

    private ObjectMapper objectMapper;

    @Autowired
    public AuthorControllerIntegrationTests(MockMvc mockMvc, ObjectMapper objectMapper, AuthorService authorService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.authorService = authorService;
    }

    @Test
    public void TestThatCreateAuthorReturnsWith201Created() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthor();
        author.setId(null);
        String json = objectMapper.writeValueAsString(author);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(MockMvcResultMatchers.status().isCreated());

    }



    @Test
    public void TestThatCreateAuthorSuccessfullyReturnsSavedAuthor() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthor();
        author.setId(null);
        String json = objectMapper.writeValueAsString(author);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber()).
                andExpect(MockMvcResultMatchers.jsonPath("$.name").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").isNumber());

    }

    @Test
    public void TestThatListAuthorReturnsWith200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/authors")).andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    public void TestThatListAuthorReturnsListOfAuthors() throws Exception {

        AuthorEntity author1 = TestDataUtil.createTestAuthor();
        authorService.save(author1);
        mockMvc.perform(MockMvcRequestBuilders.get("/authors").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Nursultan"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(20));
    }

    @Test
    public void TestThatGetAuthorReturnsWith200() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthor();
        authorService.save(author);
        mockMvc.perform(MockMvcRequestBuilders.get("/authors/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void TestThatGetAuthorReturnsWith404() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/authors/99").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void TestThatGetAuthorReturnsAuthorById() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthor();
        authorService.save(author);
        mockMvc.perform(MockMvcRequestBuilders.get("/authors/" + author.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(author.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(author.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(author.getAge()));
    }

    @Test
    public void TestThatUpdateAuthorReturn404() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthor();
        String json = objectMapper.writeValueAsString(author);
        mockMvc.perform(MockMvcRequestBuilders.put("/authors/" + author.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void TestThatUpdateAuthorReturn200() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthor();
        authorService.save(author);
        String json = objectMapper.writeValueAsString(author);
        mockMvc.perform(MockMvcRequestBuilders.put("/authors/" + author.getId()).
                        contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void TestThatUpdateUpdatesAuthor() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthor();
        AuthorEntity updatedAuthor = authorService.save(author);

        AuthorEntity authorDto = TestDataUtil.createTestAuthor();
        authorDto.setId(updatedAuthor.getId());

        String json = objectMapper.writeValueAsString(authorDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/authors/" + author.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(author.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(authorDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(authorDto.getAge()));
    }

    @Test
    public void TestThatPartialUpdateExistingAuthorReturnsOK() throws Exception{
        AuthorEntity author = TestDataUtil.createTestAuthor();
        AuthorEntity savedAuthor = authorService.save(author);

        AuthorDto authorDto = TestDataUtil.createTestAuthorDto();
        authorDto.setId(savedAuthor.getId());
        authorDto.setName("Nureke");
        authorDto.setAge(30);

        String json = objectMapper.writeValueAsString(author);
        mockMvc.perform(MockMvcRequestBuilders.patch("/authors/" + author.getId()).
                        contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void TestThatPartialUpdateExistingAuthorReturnsAuthor() throws Exception{
        AuthorEntity author = TestDataUtil.createTestAuthor();
        AuthorEntity savedAuthor = authorService.save(author);

        AuthorDto authorDto = TestDataUtil.createTestAuthorDto();
        authorDto.setName("Nureke");
        authorDto.setAge(30);

        String json = objectMapper.writeValueAsString(authorDto);
        mockMvc.perform(MockMvcRequestBuilders.patch("/authors/" + savedAuthor.getId()).
                        contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Nureke"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(30));
    }

    @Test
    public void TestThatDeleteAuthorReturnNoContent() throws Exception{
        AuthorEntity author = TestDataUtil.createTestAuthor();
        AuthorEntity saved = authorService.save(author);
        authorService.deleteOne(saved.getId());

        mockMvc.perform(MockMvcRequestBuilders.delete("/authors/" + saved.getId()).
                        contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
