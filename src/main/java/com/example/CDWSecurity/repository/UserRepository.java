package com.example.CDWSecurity.repository;

import com.example.CDWSecurity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(String usename);
}
