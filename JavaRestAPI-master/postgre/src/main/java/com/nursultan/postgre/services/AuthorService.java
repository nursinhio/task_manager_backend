package com.nursultan.postgre.services;

import com.nursultan.postgre.domain.entities.AuthorEntity;

import java.util.List;
import java.util.Optional;


public interface AuthorService {
    public AuthorEntity save(AuthorEntity author);

    List<AuthorEntity> findAll();

    Optional<AuthorEntity> findOne(Long id);

    boolean isExists(Long id);

    AuthorEntity partialUpdate(Long id, AuthorEntity authorEntity);

    void deleteOne(Long id);
}
