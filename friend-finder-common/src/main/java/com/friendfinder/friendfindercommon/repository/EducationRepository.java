package com.friendfinder.friendfindercommon.repository;

import com.friendfinder.friendfindercommon.entity.Education;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EducationRepository extends JpaRepository<Education, Integer> {

    List<Education> findAllByUserId(int userId);
}
