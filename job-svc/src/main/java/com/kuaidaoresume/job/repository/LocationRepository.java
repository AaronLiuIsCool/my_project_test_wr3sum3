package com.kuaidaoresume.job.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.kuaidaoresume.job.model.Location;

import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findByCountryIgnoreCaseAndCityIgnoreCaseAndPostCodeIgnoreCase(String country, String city, String postCode);
}
