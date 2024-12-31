package com.smart.classroom.smartclassroom.repository;

import com.smart.classroom.smartclassroom.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Member, String> {

    Optional<Member> findByUsername(String username);

    @Query(value = "select type from Member where username = ?1", nativeQuery = true)
    String findTypeByUsername(String username);

}
