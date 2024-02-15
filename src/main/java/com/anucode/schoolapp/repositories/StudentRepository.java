package com.anucode.schoolapp.repositories;

import com.anucode.schoolapp.models.Student;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByFirstNameAndLastName(String firstName, String lastName);

    List<Student> findAll(Specification<Student> specification);
}
