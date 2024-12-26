package com.smart.classroom.smartclassroom.repository;

import com.smart.classroom.smartclassroom.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
}
