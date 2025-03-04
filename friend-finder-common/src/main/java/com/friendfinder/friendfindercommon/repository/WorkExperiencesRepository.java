package com.friendfinder.friendfindercommon.repository;

import com.friendfinder.friendfindercommon.entity.WorkExperiences;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkExperiencesRepository extends JpaRepository<WorkExperiences,Integer> {
    List<WorkExperiences> findAllByUserId(int userId);
}
