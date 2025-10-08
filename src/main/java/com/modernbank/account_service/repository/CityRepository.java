package com.modernbank.account_service.repository;

import com.modernbank.account_service.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City,Long> {

    @Query("SELECT c FROM City c where c.status = 'ACTIVE' AND c.id = :id")
    Optional<City> findCityById(Long id);

    @Query("SELECT c FROM City c where c.status = 'ACTIVE'")
    Optional<List<City>> findAllActiveCities();
}