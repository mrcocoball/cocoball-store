package com.dateplanner.admin.user.repository;

import com.dateplanner.user.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserAdminRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = "roleSet")
    @Override
    List<User> findAll();

    @EntityGraph(attributePaths = "roleSet")
    Optional<User> findByUid(Long uid);


}

