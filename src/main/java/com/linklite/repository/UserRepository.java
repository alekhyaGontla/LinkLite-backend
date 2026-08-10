package com.linklite.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.linklite.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

}