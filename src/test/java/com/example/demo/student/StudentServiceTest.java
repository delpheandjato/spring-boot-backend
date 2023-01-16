package com.example.demo.student;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;
//    private AutoCloseable autoCloseable;
    private StudentService underTest;

    @BeforeEach
    void setUp() {
//        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new StudentService(studentRepository);
    }

//    @AfterEach
//    void tearDown() throws Exception {
//        autoCloseable.close();
//    }

    @Test
    void canGetStudents() {
        // when
        underTest.getStudents();

        // then
        verify(studentRepository).findAll();
    }

    @Test
    void canSetStudent() {
        // given
        Student jamal = new Student(
                "Jamal",
                "jamal@gmail.com",
                LocalDate.of(2000, Month.JANUARY, 5)
        );

        // when
        underTest.setStudent(jamal);

        // then
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);

        verify(studentRepository).save(studentArgumentCaptor.capture());

        Student capturedStudent = studentArgumentCaptor.getValue();

        assertThat(capturedStudent).isEqualTo(jamal);
    }

    @Test
    void willThrowWhenEmailIsTaken() {
        // given
        Student jamal = new Student(
                "Jamal",
                "jamal@gmail.com",
                LocalDate.of(2000, Month.JANUARY, 5)
        );

        // when
        given(studentRepository.findStudentByEmail(jamal.getEmail()))
                .willReturn(Optional.of(jamal));
        // then
        assertThatThrownBy(() -> underTest.setStudent(jamal))
                .isInstanceOf(IllegalStateException.class).
                hasMessage("Email already taken");

        verify(studentRepository, never()).save(any());
    }

    @Test
    void canDeleteStudent() {
        // when
        given(studentRepository.existsById(anyLong()))
                .willReturn(true);

        underTest.deleteStudent(anyLong());

        // then
        verify(studentRepository).deleteById(anyLong());
    }
}