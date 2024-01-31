package com.anucode.SchoolApp.repositories;

import com.anucode.SchoolApp.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
