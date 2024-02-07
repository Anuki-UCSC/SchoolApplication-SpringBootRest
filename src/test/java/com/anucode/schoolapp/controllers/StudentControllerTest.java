package com.anucode.schoolapp.controllers;

import com.anucode.schoolapp.dto.requestDto.StudentRequestDTO;
import com.anucode.schoolapp.dto.responseDto.StudentResponseDTO;
import com.anucode.schoolapp.exceptions.ResourceNotFoundException;
import com.anucode.schoolapp.exceptions.StudentIdInvalidException;
import com.anucode.schoolapp.models.Student;
import com.anucode.schoolapp.services.StudentService;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    private StudentRequestDTO studentRequestDTO;
    private StudentResponseDTO studentResponseDTO;

    @Test
    public void Should_ReturnStatusOk_When_GivenValidStudent() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        studentRequestDTO = new StudentRequestDTO("Anna", "Johns", dateFormat.parse("2000-05-05"),"Colombo");
        studentResponseDTO = new StudentResponseDTO(1L,"Anna", "Johns", dateFormat.parse("2000-05-05"),"Colombo");

        when(studentService.saveStudent(studentRequestDTO)).thenReturn(studentResponseDTO.getId());

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"firstName\": \"Tim\",\n" +
                                "    \"lastName\": \"Thomsan\",\n" +
                                "    \"dateOfBirth\": \"2000-01-01\",\n" +
                                "    \"address\": \"Jaffna\"\n" +
                                "}"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    public void Should_SaveReturnStatusBadRequest_When_GivenStudentWithMissingField() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.post("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\n" +
                            "    \"lastName\": \"Thomsan\",\n" + //firstName Missing
                            "    \"dateOfBirth\": \"2000-01-01\",\n" +
                            "    \"address\": \"Jaffna\"\n" +
                            "}"))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("firstName should not be null or blank"));

    }

    @Test
    public void Should_SaveReturnStatusBadRequest_When_GivenStudentWithMultipleMissingFields() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"dateOfBirth\": \"2000-01-01\",\n" + //firstName, lastname Missing
                                "    \"address\": \"Jaffna\"\n" +
                                "}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("firstName should not be null or blank"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("lastName should not be null or blank"));

    }


    @Test
    public void Should_UpdateReturnStatusOk_When_GivenStudentUpdate() throws Exception {
        int id =1;
        mockMvc.perform(MockMvcRequestBuilders.put("/users/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"firstName\": \"Tim_updated\",\n" +
                        "    \"lastName\": \"Thomsan_updated\",\n" +
                        "    \"dateOfBirth\": \"2002-02-01\",\n" +
                        "    \"address\": \"Jaffna_updated\"\n" +
                        "}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void Should_UpdateReturnStatusBadRequest_When_GivenStudentWithMissingField() throws Exception {
        //given
        Long id =-2L; //Negative value for Id

        //when
        when(studentService.updateStudent(any(Long.class), any(StudentRequestDTO.class)))
                .thenThrow(new StudentIdInvalidException("given id is invalid."));

        //then
        mockMvc.perform(MockMvcRequestBuilders.put("/users/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "firstName": "Tim_updated",
                            "lastName": "Thomsan_updated",
                            "dateOfBirth": "2002-02-01",
                            "address": "Jaffna_updated"
                        }"""))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    @Test
    public void Should_GetAllReturnStudentResponseDTOList_When_Call() throws Exception {
        //given
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        StudentResponseDTO studentResponseDTO1= new StudentResponseDTO(1L,"Anna", "Johns", dateFormat.parse("2000-05-05"),"Colombo");
        StudentResponseDTO studentResponseDTO2= new StudentResponseDTO(2L,"Bella", "Ruzzi", dateFormat.parse("2000-05-05"),"Jaffna");
        List<StudentResponseDTO> list = new ArrayList<StudentResponseDTO>();
        list.add(studentResponseDTO1);
        list.add(studentResponseDTO2);
        String expectedJson = "[\n" +
                "    {\n" +
                "        \"id\": 1,\n" +
                "        \"firstName\": \"Anna\",\n" +
                "        \"lastName\": \"Johns\",\n" +
                "        \"dateOfBirth\": \"2000-05-05T00:00:00.000+00:00\",\n" +
                "        \"address\": \"Colombo\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\": 2,\n" +
                "        \"firstName\": \"Bella\",\n" +
                "        \"lastName\": \"Ruzzi\",\n" +
                "        \"dateOfBirth\": \"2000-05-05T00:00:00.000+00:00\",\n" +
                "        \"address\": \"Jaffna\"\n" +
                "    }\n" +
                "]\n";;
        //when
        when(studentService.getAllStudents()).thenReturn(list);

        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedJson));
    }


    @Test
    public void Should_GetAllReturnEmptyStudentResponseDTOList_When_NoRecords() throws Exception {
        List<StudentResponseDTO> list = new ArrayList<StudentResponseDTO>();

        //when
        when(studentService.getAllStudents()).thenReturn(list);

        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json("[]"));
    }

    @Test
    public void Should_GetByIdReturnsStudentResponseDTO_When_GivenExistingId() throws Exception {
        //given
        Long id = 1L;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        StudentResponseDTO studentResponseDTO= new StudentResponseDTO(1L,"Anna", "Johns", dateFormat.parse("2000-05-05"),"Colombo");
        String expectedJson = "{\n" +
                "    \"id\": 1,\n" +
                "    \"firstName\": \"Anna\",\n" +
                "    \"lastName\": \"Johns\",\n" +
                "    \"dateOfBirth\": \"2000-05-05T00:00:00.000+00:00\",\n" +
                "    \"address\": \"Colombo\"\n" +
                "}\n";

        //when
        when(studentService.getStudentById(id)).thenReturn(studentResponseDTO);

        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}",id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedJson));

    }

    @Test
    public void Should_GetByIdThrowsResourceNotFoundException_When_GivenNonExistingId() throws Exception {
        //given
        Long id = 3003L;

        //when
        when(studentService.getStudentById(id)).thenThrow(new ResourceNotFoundException("id=" + id + " Student not found!"));

        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    @Test
    public void Should_DeleteByIdReturnId_When_SuccessfulDeletion() throws Exception {
        //given
        Long id =1L;
        String expectedJson = "1";
        //when
        when(studentService.deleteStudent(id)).thenReturn(id);

        //then
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}",id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedJson));
    }

    @Test
    public void Should_DeleteByIdThrowsStudentIdInvalidException_When_GivenInvalidId() throws Exception {
        //given
        Long id =2221L;
        String expectedJson = "given id is invalid.";
        //when
        when(studentService.deleteStudent(id)).thenThrow(new StudentIdInvalidException("given id is invalid."));

        //then
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}",id))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


}