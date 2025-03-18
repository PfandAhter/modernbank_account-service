package com.modernbank.account_service.repository;

import com.modernbank.account_service.model.entity.ErrorCodes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ErrorCodesRepository extends JpaRepository<ErrorCodes, String> {

    @Query("SELECT e FROM ErrorCodes e WHERE e.id = ?1")
    Optional<ErrorCodes> findErrorCodesById(String id);

}