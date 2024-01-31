package com.anucode.SchoolApp.controllers;


import com.anucode.SchoolApp.models.Student;
import com.anucode.SchoolApp.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping()
    public Student saveStudent(@RequestBody Student student) throws Exception {
        Student response = studentService.saveStudent(student);
        return response;
    }
}
