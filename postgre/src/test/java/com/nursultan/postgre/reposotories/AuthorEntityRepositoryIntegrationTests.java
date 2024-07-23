package com.nursultan.postgre.reposotories;

import com.nursultan.postgre.TestDataUtil;
import com.nursultan.postgre.domain.entities.AuthorEntity;
import com.nursultan.postgre.repository.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AuthorEntityRepositoryIntegrationTests {

    private AuthorRepository underTest;
    @Autowired
    public AuthorEntityRepositoryIntegrationTests(AuthorRepository underTest) {
        this.underTest = underTest;
    }
    @Test
    public void testAuthorCanBeCreatedAndRecalled(){
        AuthorEntity authorEntity = TestDataUtil.createTestAuthor();
        underTest.save(authorEntity);
        Optional<AuthorEntity> result= underTest.findById(authorEntity.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(authorEntity);
    }


//    public void testThatMultipleAuthorsCanBeCreatedAndRecalled(){
//        AuthorEntity authorA = TestDataUtil.createTestAuthor();
//        AuthorEntity authorB = TestDataUtil.createTestAuthorB();
//        AuthorEntity authorC = TestDataUtil.createTestAuthorC();
//        underTest.create(authorA);
//        underTest.create(authorB);
//        underTest.create(authorC);
//        List<AuthorEntity> authors = underTest.find();
//        assertThat(authors).hasSize(3)
//                .contains(authorA, authorB, authorC);
//    }



}
