package com.smart.classroom.smartclassroom.repository;

import com.smart.classroom.smartclassroom.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query(value = "select type from Question where id = ?1", nativeQuery = true)
    String findTypeById(Long email);
}
