package com.modernbank.account_service.repository;

import com.modernbank.account_service.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

    @Query("SELECT a FROM Account a WHERE a.user.id = ?1")
    List<Optional<Account>> findAccountByUserId(String userId);

    @Query("SELECT a FROM Account a WHERE a.iban = ?1")
    Optional<Account> findAccountByIban(String iban);

    boolean existsByIban(String iban);
}