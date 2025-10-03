package com.modernbank.account_service.repository;

import com.modernbank.account_service.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DistrictRepository extends JpaRepository<District,Long> {

    @Query("SELECT d FROM District d WHERE d.city.id = ?1")
    Optional<List<District>> findDistrictByCityId(Long cityId);
}