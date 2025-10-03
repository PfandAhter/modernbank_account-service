package com.modernbank.account_service.repository;

import com.modernbank.account_service.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {

    @Query("SELECT u FROM User u WHERE u.email = ?1")
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.gsm = ?1")
    Optional<User> findByGSM(String gsm);

    @Query("SELECT u FROM User u WHERE u.tckn = ?1")
    Optional<User> findByTCKN(String tckn);

    @Query("SELECT u FROM User u WHERE u.id = ?1")
    Optional<User> findByUserId(String userId);
}