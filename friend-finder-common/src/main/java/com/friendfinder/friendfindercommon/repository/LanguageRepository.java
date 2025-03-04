package com.friendfinder.friendfindercommon.repository;

import com.friendfinder.friendfindercommon.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Integer> {
    List<Language> findAllByUserId(int userId);
}
