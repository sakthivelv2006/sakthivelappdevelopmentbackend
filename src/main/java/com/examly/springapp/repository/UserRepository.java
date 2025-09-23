package com.examly.springapp.repository;

import com.examly.springapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
Optional<User> findByEmail(String email);
Optional<User> findByVerificationToken(String token);

@Query("SELECT u FROM User u WHERE u.email = :email AND u.password = :password AND u.verified = true")
Optional<User> findByEmailAndPasswordAndVerifiedTrue(@Param("email") String email, @Param("password") String password);


}
