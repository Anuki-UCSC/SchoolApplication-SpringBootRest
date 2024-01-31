package com.anucode.SchoolApp.controllers;


import com.anucode.SchoolApp.exceptions.ResourceNotFoundException;
import com.anucode.SchoolApp.exceptions.ValidationException;
import com.anucode.SchoolApp.models.Student;
import com.anucode.SchoolApp.services.StudentService;
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
     * Handles HTTP GET requests to retrieve all students.
     *
     * @return ResponseEntity<List<Student>> A ResponseEntity containing a list of all students and an HTTP status code.
     */
    @GetMapping()
    public ResponseEntity<List<Student>> getAll(){
        List<Student> studentList = studentService.getAllStudents();
        return ResponseEntity.ok(studentList);
    }


    /**
     * Handles HTTP GET requests to retrieve a student by ID.
     *
     * @param id The ID of the student to retrieve.
     * @return ResponseEntity<Student> A ResponseEntity containing the student with the specified ID and an HTTP status code.
     * @throws ResourceNotFoundException If the student with the given ID is not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) throws ValidationException {
        Student student = studentService.getStudentById(id);
        return ResponseEntity.ok(student);
    }


    /**
     * Handles HTTP POST requests to save a new student.
     *
     * @param student The student object to be saved, obtained from the request body.
     * @return ResponseEntity<Student> A ResponseEntity containing the saved student object and an HTTP status code.
     * @throws Exception If an error occurs during the saving process.
     */
    @PostMapping()
    public ResponseEntity<Student> saveStudent(@RequestBody Student student) throws Exception {
        Student response = studentService.saveStudent(student);
        return ResponseEntity.ok(response);
    }


    /**
     * Handles HTTP PUT requests to update a student by ID.
     *
     * @param id      The ID of the student to update.
     * @param student The updated student object, obtained from the request body.
     * @return ResponseEntity<Student> A ResponseEntity containing the updated student object and an HTTP status code.
     * @throws ValidationException If the student data is invalid or validation fails.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student student) throws ValidationException {
        Student response = studentService.updateStudent(id, student);
        return ResponseEntity.ok(response);
    }

}
