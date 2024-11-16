package com.masters.chat.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.masters.chat.domain.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    @Query("""
            SELECT u
            FROM User u
            WHERE :query IS NULL
            OR (u.fullName LIKE CONCAT('%', :query, '%')
            OR u.email LIKE CONCAT('%', :query, '%'))""")
    List<User> searchUsers(@Param("query") String query);
}
