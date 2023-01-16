package com.example.demo.student;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldFindStudentByEmail() {
        // given
        String email = "jamal@gmail.com";
        Student mariam = new Student(
                "Jamal",
                email,
                LocalDate.of(2000, Month.JANUARY, 5)
        );
        underTest.save(mariam);

        // when
        Optional<Student> expected = underTest.findStudentByEmail(email);

        // then
        assertThat(expected).isPresent();
    }

    @Test
    void itShouldCheckIfStudentExistsEmail() {
        // given
        String email = "mariam@gmail.com";
        Student mariam = new Student(
                "Mariam",
                email,
                LocalDate.of(2000, Month.JANUARY, 5)
        );
        underTest.save(mariam);

        // when
        boolean expected = underTest.selectExistsEmail(email);

        // then
        assertThat(expected).isTrue();
    }

    @Test
    void itShouldCheckIfStudentNotExistsEmail() {
        // given
        String email = "jamal@gmail.com";

        // when
        boolean expected = underTest.selectExistsEmail(email);

        // then
        assertThat(expected).isFalse();
    }
}