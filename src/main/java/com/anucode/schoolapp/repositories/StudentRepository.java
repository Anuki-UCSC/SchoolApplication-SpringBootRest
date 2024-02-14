package com.anucode.schoolapp.repositories;

import com.anucode.schoolapp.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByFirstNameAndLastName(String firstName, String lastName);
}
