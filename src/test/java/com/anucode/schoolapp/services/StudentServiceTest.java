package com.anucode.schoolapp.services;

import com.anucode.schoolapp.dto.requestDto.StudentRequestDTO;
import com.anucode.schoolapp.dto.responseDto.StudentResponseDTO;
import com.anucode.schoolapp.exceptions.ResourceNotFoundException;
import com.anucode.schoolapp.exceptions.StudentIdInvalidException;
import com.anucode.schoolapp.exceptions.StudentNameAlreadyExistsException;
import com.anucode.schoolapp.models.Student;
import com.anucode.schoolapp.repositories.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService underTest;


    @Test
    public void Should_ReturnListOfStudentResponseDTO_When_GetAllStudents() throws ParseException {
        //given
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<Student> studentList = new ArrayList<Student>();
        studentList.add(new Student(1L,"Anna", "Johns", dateFormat.parse("2000-05-05"),"Colombo"));
        studentList.add(new Student(2L,"Bob", "Ronald", dateFormat.parse("1995-11-05"),"Galle"));
        List<StudentResponseDTO> studentResponseDTOList = new ArrayList<StudentResponseDTO>();
        studentResponseDTOList.add(new StudentResponseDTO(1L,"Anna", "Johns", dateFormat.parse("2000-05-05"),"Colombo"));
        studentResponseDTOList.add(new StudentResponseDTO(2L,"Bob", "Ronald", dateFormat.parse("1995-11-05"),"Galle"));

        //when
        when(studentRepository.findAll()).thenReturn(studentList);
        List<StudentResponseDTO> actualOutput = underTest.getAllStudents();

        //then
        verify(studentRepository, Mockito.times(1)).findAll();
        assertEquals(studentResponseDTOList.size(), actualOutput.size());
        assertEquals(studentResponseDTOList, actualOutput);
    }

    @Test
    public void Should_ReturnEmptyList_When_NoResultsReturned(){
        //when
        when(studentRepository.findAll()).thenReturn(Collections.emptyList());
        List<StudentResponseDTO> actualOutput = underTest.getAllStudents();

        //then
        assertEquals(0, actualOutput.size());
    }

    @Test
    public void Should_ReturnStudentResponseDTO_When_GivenValidId() throws ParseException {
        //given
        Long id = 1L;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Student student = new Student(1L,"Anna", "Johns", dateFormat.parse("2000-05-05"),"Colombo");
        StudentResponseDTO studentResponseDTO = new StudentResponseDTO(1L,"Anna", "Johns", dateFormat.parse("2000-05-05"),"Colombo");

        //when
        when(studentRepository.findById(id)).thenReturn(Optional.of(student));
        StudentResponseDTO actualOutput = underTest.getStudentById(id);

        //then
        verify(studentRepository, Mockito.times(1)).findById(id);
        assertEquals(studentResponseDTO,actualOutput);
    }

    @Test
    public void Should_ThrowResourceNotFoundException_When_NoRecordFoundWithGivenId() throws ResourceNotFoundException{
        //given
        Long id = 33L;

        //when
        when(studentRepository.findById(id)).thenThrow(new ResourceNotFoundException("id=" + id + " Student not found!"));

        //when, then
        assertThrows(ResourceNotFoundException.class, () -> underTest.getStudentById(id));
    }

    @Test
    public void Should_ThrowResourceNotFoundExceptionMessage_When_NoRecordFoundWithGivenId() throws ResourceNotFoundException {
        // Given
        Long id = 33L;
        String message = "id=33 Student not found!";

        // When
        when(studentRepository.findById(id)).thenThrow(new ResourceNotFoundException("id=" + id + " Student not found!"));
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> underTest.getStudentById(id));

        // Then
        assertEquals(message, exception.getMessage());
    }

    @Test
    public void Should_SaveStudentAndReturnId_When_GivenStudentRequestDTO() throws ParseException {
        //given
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        StudentRequestDTO studentRequestDTO= new StudentRequestDTO("Anna", "Johns", dateFormat.parse("2000-05-05"),"Colombo");
        Student studentInput= new Student("Anna", "Johns", dateFormat.parse("2000-05-05"),"Colombo");
        Student studentOutput= new Student(1L,"Anna", "Johns", dateFormat.parse("2000-05-05"),"Colombo");

        //when
        when(studentRepository.save(studentInput)).thenReturn(studentOutput);
        Long actualOutput = underTest.saveStudent(studentRequestDTO);

        assertEquals(studentOutput.getId(), actualOutput);
    }

    @Test
    public void Should_ThrowStudentIdInvalidException_When_GivenExistingFirstNameAndLastName() throws StudentIdInvalidException, ParseException {
        //given
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        StudentRequestDTO studentRequestDTO = new StudentRequestDTO("Anna", "Johns", dateFormat.parse("1999-05-05"),"Jaffna");

        //when
        when(studentRepository.findByFirstNameAndLastName(studentRequestDTO.getFirstName(), studentRequestDTO.getLastName()))
                .thenReturn(Collections.singletonList(new Student()));

        // When & Then
        assertThrows(StudentNameAlreadyExistsException.class, () -> underTest.saveStudent(studentRequestDTO));
        verify(studentRepository, Mockito.times(1)).findByFirstNameAndLastName(anyString(), anyString());
        verify(studentRepository, Mockito.times(0)).save(any());

    }

    @Test
    public void Should_UpdateStudentAndReturnId_When_GivenStudentRequestDTOAndId() throws ParseException {
        //given
        Long id = 1L;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        StudentRequestDTO studentRequestDTO= new StudentRequestDTO("Annmarie", "Willow", dateFormat.parse("2000-05-15"),"Rathnapura");
        Student existingStudent = new Student(1L,"Anna", "Johns", dateFormat.parse("1999-05-05"),"Jaffna");
        Student changedStudentOutput = new Student(1L,"Annmarie", "Willow", dateFormat.parse("2000-05-15"),"Rathnapura");


        //when
        when(studentRepository.findById(id)).thenReturn(Optional.of(existingStudent));
        when(studentRepository.save(changedStudentOutput)).thenReturn(changedStudentOutput);
        Long actualOutput = underTest.updateStudent(id, studentRequestDTO);

        assertEquals(changedStudentOutput.getId(), actualOutput);
    }

    @Test
    public void Should_ThrowStudentIdInvalidException_When_NoStudentForGivenId() throws ParseException {
        //given
        Long id = 33L;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        StudentRequestDTO studentRequestDTO= new StudentRequestDTO("Annmarie", "Willow", dateFormat.parse("2000-05-15"),"Rathnapura");

        //when
        when(studentRepository.findById(id)).thenThrow(new StudentIdInvalidException("given id is invalid."));

        //then
        assertThrows(StudentIdInvalidException.class, ()-> underTest.updateStudent(id, studentRequestDTO));
        verify(studentRepository, Mockito.times(1)).findById(id);
        verify(studentRepository, Mockito.times(0)).save(any());
    }


    @Test
    public void Should_DeleteStudent_When_GivenStudentId() throws ParseException {
        //given
        Long id = 1L;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Student existingStudent = new Student(1L,"Anna", "Johns", dateFormat.parse("1999-05-05"),"Jaffna");

        //when
        when(studentRepository.findById(id)).thenReturn(Optional.of(existingStudent));
        Long actualOutput = underTest.deleteStudent(id);

        //then
        assertEquals(id, actualOutput);
    }


    @Test
    public void Should_ThrowStudentIdInvalidException_when_NoStudentForGivenId(){
        //given
        Long id =33L;

        //when
        when(studentRepository.findById(id)).thenThrow(new StudentIdInvalidException("given id is invalid."));

        //then
        assertThrows(StudentIdInvalidException.class,()-> underTest.deleteStudent(id));
        verify(studentRepository, Mockito.times(1)).findById(id);
        verify(studentRepository, Mockito.times(0)).deleteById(id);
    }


    @Test
    void Should_ReturnListOfStudentResponseDTO_WhenGivenSearchStringsArrayContainsInRecords() throws ParseException {
        //given
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String[] stringArray = {"Anuki", "Alwis" };

        List<Student> searchResult = new ArrayList<>();
        searchResult.add(new Student(1L,"Anuki", "Alwis",dateFormat.parse("2000-01-11"),"Colombo"));


        //when
        when(studentRepository.findAll((Specification<Student>) any())).thenReturn(searchResult);
        List<StudentResponseDTO> actualOutput = underTest.searchStudent(stringArray);

        //then
        assertEquals(1, actualOutput.size());
    }

    @Test
    void Should_ReturnEmptyList_WhenSearchStringsNotContainsInRecords() throws ParseException {
        //given
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String[] stringArray = {"Anuki", "Alwis" };

        List<Student> searchResult = new ArrayList<>();

        //when
        when(studentRepository.findAll((Specification<Student>) any())).thenReturn(searchResult);
        List<StudentResponseDTO> actualOutput = underTest.searchStudent(stringArray);

        //then
        assertEquals(0, actualOutput.size());
    }
};