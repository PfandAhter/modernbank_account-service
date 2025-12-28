package com.modernbank.account_service.repository;

import com.modernbank.account_service.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, String> {

    List<Card> findByAccountId(String accountId);

    List<Card> findByAccountIban(String iban);

    boolean existsByCardNumber(String cardNumber);

    /*@Query("SELECT c FROM Card c WHERE c.account.user.id = ?1 AND c.account.id = ?2")
    Optional<List<Card>> getCardsByUserIdAndAccountId(String userId, String accountId);*/

    @Query("SELECT c FROM Card c WHERE c.account.user.id = ?1 AND (?2 = 'ALL' OR c.account.id = ?2)")
    Optional<List<Card>> getCardsByUserIdAndAccountId(String userId, String accountId);

    @Query("SELECT c FROM Card c WHERE c.cardNumber = ?1 AND c.account.user.id = ?2")
    Optional<Card> findByCardNumberAndUserId(String cardNumber, String userId);
}