package com.nursultan.postgre.mappers.impl;

import com.nursultan.postgre.domain.dto.BookDto;
import com.nursultan.postgre.domain.entities.BookEntity;
import com.nursultan.postgre.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class BookMapper implements Mapper<BookEntity, BookDto> {

    private ModelMapper modelMapper;

    public BookMapper(ModelMapper modelMapper) {this.modelMapper = modelMapper;}
    @Override
    public BookDto mapTo(BookEntity bookEntity) {
        return modelMapper.map(bookEntity, BookDto.class);
    }

    @Override
    public BookEntity mapFrom(BookDto bookDto) {
        return modelMapper.map(bookDto, BookEntity.class);
    }
}
