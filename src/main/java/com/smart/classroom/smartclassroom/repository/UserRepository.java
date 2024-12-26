package com.smart.classroom.smartclassroom.repository;

import com.smart.classroom.smartclassroom.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    @Query(value = "select type from Member where email = ?1", nativeQuery = true)
    String findTypeByEmail(String email);

}
