package com.anucode.SchoolApp.services;


import com.anucode.SchoolApp.exceptions.ValidationException;
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

    public Student saveStudent(Student student) throws ValidationException {
        try {
            studentRepository.save(student);
        }catch (DataIntegrityViolationException e){
            throw new ValidationException("firstName and LastName are already available");
        }catch (Exception e){
            throw new ValidationException("Create user failed");
        }
        return student;
    }



}
