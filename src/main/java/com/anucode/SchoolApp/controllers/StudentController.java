package com.anucode.SchoolApp.controllers;


import com.anucode.SchoolApp.exceptions.ResourceNotFoundException;
import com.anucode.SchoolApp.exceptions.ValidationException;
import com.anucode.SchoolApp.models.Student;
import com.anucode.SchoolApp.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class StudentController {

    @Autowired
    private StudentService studentService;


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

    
}
