package dev.be.modulecore.repositories.user;

import dev.be.modulecore.domain.user.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    @EntityGraph(attributePaths = "roleSet")
    @Override
    List<User> findAll();

    @EntityGraph(attributePaths = "roleSet")
    @Query("select u from User u where u.email = :email")
    Optional<User> getWithRolesByEmail(@Param("email") String email);

    @EntityGraph(attributePaths = "roleSet")
    Optional<User> findByEmail(String email);

    @EntityGraph(attributePaths = "roleSet")
    Optional<User> findByNickname(String nickname);

    @EntityGraph(attributePaths = "roleSet")
    Optional<User> findByEmailAndProvider(String email, String provider);

    @Modifying
    @Transactional
    @Query("update User u set u.password = :password where u.uid = :uid")
    void updatePassword(@Param("password") String password, @Param("uid") Long uid);

    @EntityGraph(attributePaths = "roleSet")
    Optional<User> findByUid(Long uid);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

}
