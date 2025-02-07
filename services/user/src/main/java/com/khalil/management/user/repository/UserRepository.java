package com.khalil.management.user.repository;

import java.util.List;

import com.khalil.management.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    List<User> findByGroupId(Integer groupId);

    boolean existsByEmail(String email);

}
