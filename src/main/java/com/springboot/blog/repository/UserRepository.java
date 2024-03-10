package com.springboot.blog.repository;

import com.springboot.blog.entity.User;
import com.springboot.blog.payload.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Optional<User> findByName(String name);

    @Query("SELECT u FROM User u WHERE " +
            "(:#{#userDto.username} IS NULL OR LOWER(u.username) LIKE LOWER(CONCAT('%', :#{#userDto.username}, '%'))) AND " +
            "(:#{#userDto.email} IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :#{#userDto.email}, '%'))) AND " +
            "(:#{#userDto.name} IS NULL OR LOWER(u.name) LIKE LOWER(CONCAT('%', :#{#userDto.name}, '%')))")
    Page<User> searchUsers(UserDto userDto, Pageable pageable);
}
