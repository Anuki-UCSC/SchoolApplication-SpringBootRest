package com.anucode.SchoolApp.services;


import com.anucode.SchoolApp.models.Student;
import com.anucode.SchoolApp.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public Student saveStudent(Student student) throws Exception {
        studentRepository.save(student);
        return student;
    }



}
