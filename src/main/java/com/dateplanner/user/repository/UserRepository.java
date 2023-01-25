package com.dateplanner.user.repository;

import com.dateplanner.user.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    @EntityGraph(attributePaths = "roleSet")
    @Query("select u from User u where u.uid = :uid and u.social = false")
    Optional<User> getWithRoles(@Param("uid") String uid);

    @EntityGraph(attributePaths = "roleSet")
    Optional<User> findByEmail(String email);

    @Modifying
    @Transactional
    @Query("update User u set u.password = :password where u.uid = :uid")
    void updatePassword(@Param("password") String password, @Param("uid") String uid);

    Optional<User> findByUid(String uid);

    boolean existsById(String uid);

}
