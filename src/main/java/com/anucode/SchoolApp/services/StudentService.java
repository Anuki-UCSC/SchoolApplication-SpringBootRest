package com.anucode.SchoolApp.services;


import com.anucode.SchoolApp.exceptions.ResourceNotFoundException;
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


    public List<Student> getAllStudents(){
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id) throws ValidationException {
        return studentRepository.findById(id)
                .orElseThrow(()-> new ValidationException("given id is invalid."));
    }

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

    public Student updateStudent(Long id, Student student) throws ValidationException {
        Student modifiedStudent = studentRepository.findById(id)
                .orElseThrow(()-> new ValidationException("given id is invalid."));
        modifiedStudent.setFirstName(student.getFirstName());
        modifiedStudent.setLastName(student.getLastName());
        modifiedStudent.setAddress(student.getAddress());
        modifiedStudent.setDateOfBirth(student.getDateOfBirth());
        return studentRepository.save(modifiedStudent);
    }

    public Long deleteStudent(Long id) throws ValidationException {
        Student deletingStudent = studentRepository.findById(id)
                .orElseThrow(()-> new ValidationException("given id is invalid."));
        studentRepository.deleteById(id);
        return id;
    }


}
