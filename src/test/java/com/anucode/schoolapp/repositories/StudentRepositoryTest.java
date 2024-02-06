package com.anucode.schoolapp.repositories;

import com.anucode.schoolapp.models.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.EmptyResultDataAccessException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void Should_ReturnStudent_When_GivenExistingId() throws ParseException {
        //given
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Student studentEntity = new Student(1L,"Helena", "Wonka", dateFormat.parse("2000-05-05"),"Colombo");
        entityManager.merge(studentEntity);

        //when
        Optional<Student> actualOutput = studentRepository.findById(1L);

        //then
        assertTrue(actualOutput.isPresent());
        assertEquals(studentEntity, actualOutput.get());
    }

    @Test
    public void Should_ReturnNull_When_GivenNonExistingId() throws ParseException {
        //given
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Student studentEntity = new Student(1L,"Helena", "Wonka", dateFormat.parse("2000-05-05"),"Colombo");
        entityManager.merge(studentEntity);

        //when
        Optional<Student> student = studentRepository.findById(33L);

        //then
        assertTrue(student.isEmpty());
    }

    @Test
    public void Should_ReturnStudentList_When_FindAll() throws ParseException {
        //given
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Student studentEntity1 = new Student(1L,"Helena", "Wonka", dateFormat.parse("2000-05-05"),"Colombo");
        entityManager.merge(studentEntity1);
        Student studentEntity2 = new Student(2L,"Ramya", "Vinodi", dateFormat.parse("2000-11-14"),"Gampaha");
        entityManager.merge(studentEntity2);

        //when
        List<Student> actualOutput = studentRepository.findAll();

        //then
        assertEquals(2, actualOutput.size());
        assertEquals(studentEntity1, actualOutput.get(0));
        assertEquals(studentEntity2, actualOutput.get(1));
    }

    @Test
    public void Should_ReturnNull_When_GivenFirstNameAndLastNameNotExistsInRecords() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Student existingStudent = new Student(1L,"Helena", "Wonka", dateFormat.parse("2000-05-05"),"Colombo");
        entityManager.merge(existingStudent);
        Student newlyInsertingStudent = new Student(2L,"Ramya", "Vinodi", dateFormat.parse("2000-11-14"),"Gampaha");

        //when
        List<Student> actualOutput = studentRepository.findByFirstNameAndLastName(
                newlyInsertingStudent.getFirstName(),
                newlyInsertingStudent.getLastName()
        );

        //then
        assertTrue(actualOutput.isEmpty());
    }

    @Test
    public void Should_ReturnNull_When_GivenFirstNameExistsInRecords() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Student existingStudent = new Student(1L,"Helena", "Wonka", dateFormat.parse("2000-05-05"),"Colombo");
        entityManager.merge(existingStudent);
        Student newlyInsertingStudent = new Student(2L,"Helena", "Vinodi", dateFormat.parse("2000-11-14"),"Gampaha");

        //when
        List<Student> actualOutput = studentRepository.findByFirstNameAndLastName(
                newlyInsertingStudent.getFirstName(),
                newlyInsertingStudent.getLastName()
        );

        //then
        assertTrue(actualOutput.isEmpty());
    }

    @Test
    public void Should_ReturnList_When_GivenFirstNameAndLastNameExistsInRecords() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Student existingStudent = new Student(1L,"Helena", "Wonka", dateFormat.parse("2000-05-05"),"Colombo");
        entityManager.merge(existingStudent);
        Student newlyInsertingStudent = new Student(2L,"Helena", "Wonka", dateFormat.parse("2000-11-14"),"Gampaha");

        //when
        List<Student> actualOutput = studentRepository.findByFirstNameAndLastName(
                newlyInsertingStudent.getFirstName(),
                newlyInsertingStudent.getLastName()
        );

        //then
        assertFalse(actualOutput.isEmpty());
        assertEquals(1, actualOutput.size());
    }


    @Test
    public void Should_SaveAndReturnStudentObject_When_GivenStudentObject() throws ParseException {
        //given
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Student student = new Student(2L,"Helena", "Wonka", dateFormat.parse("2000-05-05"),"Colombo");

        //when
        Student actualOutput = studentRepository.save(student);

        //then
        assertEquals(student, actualOutput);
    }


    @Test
    public void Should_DeleteStudentRecord_When_GivenStudentId() throws ParseException {
        //given
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Student existingStudent = new Student(1L,"Helena", "Wonka", dateFormat.parse("2000-05-05"),"Colombo");
        entityManager.merge(existingStudent);

        //when
        studentRepository.deleteById(existingStudent.getId());

        //then
        Student removedStudent = entityManager.find(Student.class, existingStudent.getId());
        assertNull(removedStudent, "Student should be removed from the test database");
    }

}