package com.modernbank.account_service.repository;

import com.modernbank.account_service.entity.SavedAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavedAccountRepository extends JpaRepository<SavedAccount, String> {

    @Query("""
        SELECT s FROM SavedAccount s
        WHERE s.user.id = :userId
          AND (
              LOWER(s.nickname) LIKE LOWER(CONCAT('%', :query, '%'))
              OR LOWER(s.firstName) LIKE LOWER(CONCAT('%', :query, '%'))
              OR LOWER(s.lastName) LIKE LOWER(CONCAT('%', :query, '%'))
          )
    """)
    Optional<List<SavedAccount>> findByUserIdAndNameOrNicknameLike(@Param("userId") String userId, @Param("query") String query);

    @Query("SELECT sa FROM SavedAccount sa WHERE sa.user.id =?1")
    Optional<List<SavedAccount>> findAllByUserId(String userId);

    @Query("SELECT sa FROM SavedAccount sa WHERE sa.id =?1 AND sa.user.id =?2")
    Optional<SavedAccount> findByIdAndUser(String id, String userId);
}