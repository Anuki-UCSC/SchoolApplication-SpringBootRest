package com.anucode.schoolapp.controllers;


import com.anucode.schoolapp.dto.requestDto.StudentRequestDTO;
import com.anucode.schoolapp.dto.responseDto.StudentResponseDTO;
import com.anucode.schoolapp.exceptions.ResourceNotFoundException;
import com.anucode.schoolapp.exceptions.StudentIdInvalidException;
import com.anucode.schoolapp.exceptions.StudentNameAlreadyExistsException;
import com.anucode.schoolapp.services.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class StudentController {

    @Autowired
    private StudentService studentService;


    /**
     * Retrieves all students from the database.
     *
     * @return A ResponseEntity containing a list of StudentResponseDTO objects representing all students.
     */
    @GetMapping()
    public ResponseEntity<List<StudentResponseDTO>> getAll(){
        List<StudentResponseDTO> studentDtoList = studentService.getAllStudents();
        return ResponseEntity.ok(studentDtoList);
    }


    /**
     * Retrieves a student from the database by their ID.
     *
     * @param id The ID of the student to retrieve.
     * @return A ResponseEntity containing a StudentResponseDTO object representing the student with the specified ID.
     * @throws ResourceNotFoundException if the provided ID is not valid.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> getStudentById(@PathVariable @Valid Long id) throws ResourceNotFoundException {
        StudentResponseDTO studentResponseDTO = studentService.getStudentById(id);
        return ResponseEntity.ok(studentResponseDTO);
    }


    /**
     * Saves a new student to the database.
     *
     * @param student The StudentRequestDTO object containing information about the student to be saved.
     * @return A ResponseEntity containing the ID of the newly saved student.
     * @throws StudentIdInvalidException if the provided student data is not valid.
     */
    @PostMapping()
    public ResponseEntity<Long> saveStudent(@RequestBody @Valid StudentRequestDTO student) throws StudentNameAlreadyExistsException {
        Long id = studentService.saveStudent(student);
        return ResponseEntity.ok(id);
    }


    /**
     * Updates an existing student in the database with the provided ID.
     *
     * @param id The ID of the student to be updated.
     * @param student The StudentRequestDTO object containing updated information for the student.
     * @return A ResponseEntity containing the ID of the updated student record.
     * @throws StudentIdInvalidException if the provided ID or student data is not valid.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Long> updateStudent(@PathVariable @Valid Long id, @RequestBody @Valid StudentRequestDTO student) throws StudentIdInvalidException {
        Long recordId = studentService.updateStudent(id, student);
        return ResponseEntity.ok(recordId);
    }

    /**
     * Deletes a student from the database by their ID.
     *
     * @param id The ID of the student to be deleted.
     * @return A ResponseEntity containing the ID of the deleted student record.
     * @throws StudentIdInvalidException if the provided ID is not valid.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteStudent(@PathVariable @Valid Long id) throws StudentIdInvalidException {
        Long response = studentService.deleteStudent(id);
        return ResponseEntity.ok(response);
    }
}
