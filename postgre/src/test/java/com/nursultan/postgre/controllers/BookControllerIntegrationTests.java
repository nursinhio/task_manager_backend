package com.nursultan.postgre.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nursultan.postgre.TestDataUtil;
import com.nursultan.postgre.domain.dto.BookDto;
import com.nursultan.postgre.domain.entities.BookEntity;
import com.nursultan.postgre.services.BookService;
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

public class BookControllerIntegrationTests {
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private BookService bookService;
   @Autowired
   public BookControllerIntegrationTests(MockMvc mockMvc, ObjectMapper objectMapper, BookService bookService) {
       this.mockMvc = mockMvc;
       this.objectMapper = objectMapper;
       this.bookService = bookService;
   }

    @Test
    public void TestThatCreatedBookReturns201() throws Exception {
        BookDto bookDto = TestDataUtil.createTestBookDto(null);
        String json = objectMapper.writeValueAsString(bookDto);
        mockMvc.perform(MockMvcRequestBuilders.put("/books/" + bookDto.getIsbn())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void TestThatCreatedBook() throws Exception {
        BookDto bookDto = TestDataUtil.createTestBookDto(null);
        String json = objectMapper.writeValueAsString(bookDto);
        mockMvc.perform(MockMvcRequestBuilders.put("/books/" + bookDto.getIsbn())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(bookDto.getIsbn()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(bookDto.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(bookDto.getAuthor()));
    }

    @Test
    public void TestThatListBookReturns200() throws Exception {
       mockMvc.perform(MockMvcRequestBuilders.get("/books").accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void TestThatListBookReturnListBooks() throws Exception {
       BookEntity bookEntity = TestDataUtil.createTestBook(null);
       bookService.createUpdateBook( "978-1-222",bookEntity);
       mockMvc.perform(MockMvcRequestBuilders.get("/books").accept(MediaType.APPLICATION_JSON))
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].isbn").value(bookEntity.getIsbn()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value(bookEntity.getTitle()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].author").value(bookEntity.getAuthorEntity()));
    }

    @Test
    public void TestThatGetBookReturns200() throws Exception {
       BookEntity bookEntity = TestDataUtil.createTestBook(null);
       bookService.createUpdateBook("978-1-222",bookEntity);
       mockMvc.perform(MockMvcRequestBuilders.get("/books/978-1-222").accept(MediaType.APPLICATION_JSON))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void TestThatGetBookReturnsBook() throws Exception {
       BookEntity bookEntity = TestDataUtil.createTestBook(null);
       bookService.createUpdateBook("978-1-222",bookEntity);
       mockMvc.perform(MockMvcRequestBuilders.get("/books/978-1-222").accept(MediaType.APPLICATION_JSON))
               .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(bookEntity.getIsbn()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(bookEntity.getTitle()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(bookEntity.getAuthorEntity()));

    }

    @Test
    public void TestThatUpdateBookReturns200() throws Exception {
       BookEntity bookEntity = TestDataUtil.createTestBook(null);
       BookEntity savedBook = bookService.createUpdateBook(bookEntity.getIsbn(),bookEntity);

       BookDto bookDto = TestDataUtil.createTestBookDto(null);
       bookDto.setIsbn(bookEntity.getIsbn());

       String bookJson = objectMapper.writeValueAsString(bookDto);

       mockMvc.perform(MockMvcRequestBuilders.put("/books/" + bookEntity.getIsbn())
               .contentType(MediaType.APPLICATION_JSON)
                       .content(bookJson))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void TestThatUpdateBookReturnsUpdatedBook() throws Exception {
       BookEntity bookEntity = TestDataUtil.createTestBook(null);
       BookEntity savedBook = bookService.createUpdateBook(bookEntity.getIsbn(),bookEntity);

       BookDto bookDto = TestDataUtil.createTestBookDto(null);
       bookDto.setIsbn(savedBook.getIsbn());
       bookDto.setTitle("Updated Title");
       String bookJson = objectMapper.writeValueAsString(bookDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/books/" + savedBook.getIsbn()).contentType(MediaType.APPLICATION_JSON).content(bookJson))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value("978-1-222"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Updated Title"));
    }

    @Test
    public void TestThatPartialUpdateBookReturns200() throws Exception {
       BookEntity bookEntity = TestDataUtil.createTestBook(null);
       BookEntity savedBook = bookService.createUpdateBook(bookEntity.getIsbn(),bookEntity);
       BookDto bookDto = TestDataUtil.createTestBookDto(null);
       bookDto.setTitle("Salam");
       String bookJson = objectMapper.writeValueAsString(bookDto);

       mockMvc.perform(MockMvcRequestBuilders.patch("/books/" + savedBook.getIsbn()).contentType(MediaType.APPLICATION_JSON).content(bookJson))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void TestThatPartialUpdateBookReturnsUpdatedBook() throws Exception {
       BookEntity bookEntity = TestDataUtil.createTestBook(null);
       BookEntity savedBook = bookService.createUpdateBook(bookEntity.getIsbn(),bookEntity);
       BookDto bookDto = TestDataUtil.createTestBookDto(null);
       bookDto.setTitle("Salam");
       String bookJson = objectMapper.writeValueAsString(bookDto);

       mockMvc.perform(MockMvcRequestBuilders.patch("/books/" + savedBook.getIsbn()).contentType(MediaType.APPLICATION_JSON).content(bookJson))
               .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(bookDto.getTitle()));
    }

    @Test
    public void TestThatDeleteBookReturnNoContent() throws Exception{
       mockMvc.perform(MockMvcRequestBuilders.delete("/books/978-1-222").accept(MediaType.APPLICATION_JSON))
               .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
