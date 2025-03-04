package com.friendfinder.friendfindercommon.repository;

import com.friendfinder.friendfindercommon.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Integer> {

}
