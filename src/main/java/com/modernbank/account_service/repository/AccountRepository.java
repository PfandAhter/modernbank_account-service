package com.modernbank.account_service.repository;

import com.modernbank.account_service.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

    @Query("SELECT a FROM Account a WHERE a.user.id = ?1")
    List<Optional<Account>> findAccountByUserId(String userId);

    @Query("select a from Account a left join fetch a.user where a.iban = :iban")
    Optional<Account> findAccountByIban(@Param("iban") String iban);

    boolean existsByIban(String iban);

    @Query("SELECT a FROM Account a WHERE a.id = ?1")
    Optional<Account> findAccountById(String accountId);
}