package com.anucode.schoolapp.specifications;

import com.anucode.schoolapp.models.Student;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class StudentSpecification implements Specification<Student> {

    private final String[] keywordsArray;

    public StudentSpecification(String[] keywordsArray) {
        this.keywordsArray = keywordsArray;
    }

    @Override
    public Predicate toPredicate(Root<Student> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicateList = new ArrayList<>();
        for (String keyword : keywordsArray){
            String containsKeyword = "%"+keyword+"%";
            predicateList.add(criteriaBuilder.or(
                    criteriaBuilder.like(root.get("firstName"),containsKeyword),
                    criteriaBuilder.like(root.get("lastName"),containsKeyword),
                    criteriaBuilder.like(root.get("address"),containsKeyword)
            ));
        }
        return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
    }
}
