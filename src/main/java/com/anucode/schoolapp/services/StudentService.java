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

    /**
     * Retrieves all students from the studentRepository.
     *
     * @return A list of StudentResponseDTO objects containing information for all students.
     */
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

    /**
     * Retrieves a student from the studentRepository by the provided ID.
     *
     * @param id The ID of the student to be retrieved.
     * @return A StudentResponseDTO object containing information for the student.
     * @throws ResourceNotFoundException if the student with the provided ID is not found in the database.
     */
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


    /**
     * Saves a new student to the database based on the provided information by calling studentRepository.
     *
     * @param studentRequestDTO The StudentRequestDTO object containing information for the new student.
     * @return The ID of the newly saved student.
     * @throws StudentIdInvalidException if a student with the same first name and last name already exists.
     */
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


    /**
     * Updates an existing student provided ID based on the information provided in the StudentRequestDTO object.
     *
     * @param id The ID of the student to be updated.
     * @param studentRequestDTO The StudentRequestDTO object containing updated information for the student.
     * @return The ID of the updated student.
     * @throws StudentIdInvalidException if the provided ID is invalid.
     */
    public Long updateStudent(Long id, StudentRequestDTO studentRequestDTO) throws StudentIdInvalidException {
        Student modifiedStudent = studentRepository.findById(id)
                .orElseThrow(()-> new StudentIdInvalidException("given id is invalid."));
        modifiedStudent.setFirstName(studentRequestDTO.getFirstName());
        modifiedStudent.setLastName(studentRequestDTO.getLastName());
        modifiedStudent.setAddress(studentRequestDTO.getAddress());
        modifiedStudent.setDateOfBirth(studentRequestDTO.getDateOfBirth());
        return studentRepository.save(modifiedStudent).getId();
    }


    /**
     * Deletes an existing student from the database with the provided ID.
     *
     * @param id The ID of the student to be deleted.
     * @return The ID of the deleted student.
     * @throws StudentIdInvalidException if the provided ID is invalid.
     */
    public Long deleteStudent(Long id) throws StudentIdInvalidException {
        Student deletingStudent = studentRepository.findById(id)
                .orElseThrow(()-> new StudentIdInvalidException("given id is invalid."));
        studentRepository.deleteById(id);
        return id;
    }


}
