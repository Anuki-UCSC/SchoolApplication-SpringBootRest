package com.anucode.schoolapp.services;


import com.anucode.schoolapp.dto.requestDto.StudentRequestDTO;
import com.anucode.schoolapp.dto.responseDto.StudentResponseDTO;

import com.anucode.schoolapp.exceptions.ResourceNotFoundException;
import com.anucode.schoolapp.exceptions.StudentIdInvalidException;
import com.anucode.schoolapp.exceptions.StudentNameAlreadyExistsException;
import com.anucode.schoolapp.models.Student;
import com.anucode.schoolapp.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;


    public List<StudentResponseDTO> getAllStudents(){
        List<Student> students = studentRepository.findAll();
        List<StudentResponseDTO> responseDTOList = new ArrayList<>();
        students.forEach(student ->
                  responseDTOList.add(new StudentResponseDTO(
                          student.getId(),
                          student.getFirstName(),
                          student.getLastName(),
                          student.getDateOfBirth(),
                          student.getAddress()))
                );
        return responseDTOList;
    }

    public StudentResponseDTO getStudentById(Long id) throws ResourceNotFoundException {
        Student student = studentRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("id=" + id + " Student not found!"));
        return new StudentResponseDTO(
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                student.getDateOfBirth(),
                student.getAddress()
        );
    }

    public Long saveStudent(StudentRequestDTO studentRequestDTO) throws StudentNameAlreadyExistsException{
            List<Student> existingStudentList = studentRepository.findByFirstNameAndLastName(
                    studentRequestDTO.getFirstName(),
                    studentRequestDTO.getLastName()
            );
            if(!existingStudentList.isEmpty()){
                throw new StudentNameAlreadyExistsException("firstName and LastName are already available");
            }
            Student student = new Student(
                    studentRequestDTO.getFirstName(),
                    studentRequestDTO.getLastName(),
                    studentRequestDTO.getDateOfBirth(),
                    studentRequestDTO.getAddress()
            );
        return studentRepository.save(student).getId();
    }

    public Long updateStudent(Long id, StudentRequestDTO studentRequestDTO) throws StudentIdInvalidException {
        Student modifiedStudent = studentRepository.findById(id)
                .orElseThrow(()-> new StudentIdInvalidException("given id is invalid."));
        modifiedStudent.setFirstName(studentRequestDTO.getFirstName());
        modifiedStudent.setLastName(studentRequestDTO.getLastName());
        modifiedStudent.setAddress(studentRequestDTO.getAddress());
        modifiedStudent.setDateOfBirth(studentRequestDTO.getDateOfBirth());
        return studentRepository.save(modifiedStudent).getId();
    }

    public Long deleteStudent(Long id) throws StudentIdInvalidException {
        Student deletingStudent = studentRepository.findById(id)
                .orElseThrow(()-> new StudentIdInvalidException("given id is invalid."));
        studentRepository.deleteById(id);
        return id;
    }


}
