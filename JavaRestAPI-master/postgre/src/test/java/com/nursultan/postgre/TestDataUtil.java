package com.nursultan.postgre;

import com.nursultan.postgre.domain.dto.AuthorDto;
import com.nursultan.postgre.domain.dto.BookDto;
import com.nursultan.postgre.domain.entities.AuthorEntity;
import com.nursultan.postgre.domain.entities.BookEntity;

public class TestDataUtil {
    private TestDataUtil() {}

    public static AuthorEntity createTestAuthor() {
        return AuthorEntity.builder()
                .id(1L)
                .name("Nursultan")
                .age(20)
                .build();
    }

    public static AuthorDto createTestAuthorDto() {
        return AuthorDto.builder()
                .id(1L)
                .name("Nursultan")
                .age(20)
                .build();
    }


    public static AuthorEntity createTestAuthorB() {
        return AuthorEntity.builder()
                .id(2L)
                .name("Nurs")
                .age(35)
                .build();
    }

    public static AuthorEntity createTestAuthorC() {
        return AuthorEntity.builder()
                .id(3L)
                .name("Nursinhio")
                .age(35)
                .build();
    }

    public static BookEntity createTestBook(AuthorEntity author) {
        return BookEntity.builder()
                .isbn("978-1-222")
                .title("Abay Zholy")
                .authorEntity(author)
                .build();
    }

    public static BookDto createTestBookDto(AuthorDto author) {
        return BookDto.builder()
                .isbn("978-1-222")
                .title("Abay Zholy")
                .author(author)
                .build();
    }
}
