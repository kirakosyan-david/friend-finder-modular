package com.friendfinder.friendfindercommon.repository;

import com.friendfinder.friendfindercommon.entity.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterestsRepository extends JpaRepository<Interest, Integer> {
    List<Interest> findAllByUserId(int userId);
}
