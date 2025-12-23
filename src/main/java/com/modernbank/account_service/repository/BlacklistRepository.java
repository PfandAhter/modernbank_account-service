package com.modernbank.account_service.repository;

import com.modernbank.account_service.entity.Blacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlacklistRepository extends JpaRepository<Blacklist, String> {

    Optional<Blacklist> findByIbanAndIsActiveTrue(String iban);

    boolean existsByIbanAndIsActiveTrue(String iban);
}
