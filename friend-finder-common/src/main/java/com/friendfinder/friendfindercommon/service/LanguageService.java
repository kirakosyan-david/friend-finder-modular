package com.friendfinder.friendfindercommon.service;

import com.friendfinder.friendfindercommon.entity.Language;

import java.util.List;

public interface LanguageService {
    List<Language> findAllByUserId(int userId);

    Language save(Language lang);
}
