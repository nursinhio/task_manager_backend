package com.nursultan.postgre.controllers;

import com.nursultan.postgre.domain.dto.BookDto;
import com.nursultan.postgre.domain.entities.BookEntity;
import com.nursultan.postgre.mappers.Mapper;
import com.nursultan.postgre.services.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class BookController {
    private final Mapper<BookEntity, BookDto> bookMapper;

    private final BookService bookService;

    public BookController(Mapper<BookEntity, BookDto> bookMapper, BookService bookService) {
        this.bookMapper = bookMapper;
        this.bookService = bookService;
    }

    @PutMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDto> createUpdateBook(@PathVariable("isbn") String isbn, @RequestBody BookDto book) {
        BookEntity bookEntity =bookMapper.mapFrom(book);
        boolean bookExist = bookService.isExists(isbn);
        BookEntity savedBook = bookService.createUpdateBook(isbn, bookEntity);
        BookDto savedUpdatedBookDto = bookMapper.mapTo(savedBook);

        if(bookExist){
            return new  ResponseEntity<>(savedUpdatedBookDto, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(savedUpdatedBookDto, HttpStatus.CREATED);
        }
    }

    @GetMapping(path = "/books")
    public Page<BookDto> getAllBooks(Pageable pageable) {
        Page<BookEntity> books = bookService.findAll(pageable);
        return books.map(bookMapper::mapTo);
    }

    @GetMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDto> getBook(@PathVariable("isbn") String isbn) {
        Optional<BookEntity> foundBook = bookService.findOne(isbn);
        return foundBook.map(bookEntity -> {
           BookDto bookDto = bookMapper.mapTo(bookEntity);
           return new ResponseEntity<>(bookDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping(path = "books/{isbn}")
    public ResponseEntity<BookDto> partialUpdateBook(@PathVariable("isbn") String isbn, @RequestBody BookDto bookDto) {
        BookEntity bookEntity = bookMapper.mapFrom(bookDto);
        if(!bookService.isExists(isbn)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }


        BookEntity updatedBook = bookService.partialUpdate(isbn, bookEntity);
        return new ResponseEntity<>(bookMapper.mapTo(updatedBook), HttpStatus.OK);
    }

    @DeleteMapping(path = "books/{isbn}")
    public ResponseEntity deleteBook(@PathVariable("isbn") String isbn) {
        bookService.deleteOne(isbn);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
