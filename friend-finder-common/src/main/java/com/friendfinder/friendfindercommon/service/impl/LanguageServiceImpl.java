package com.friendfinder.friendfindercommon.service.impl;

import com.friendfinder.friendfindercommon.entity.Language;
import com.friendfinder.friendfindercommon.repository.LanguageRepository;
import com.friendfinder.friendfindercommon.service.LanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * LanguageServiceImpl is the implementation of the LanguageService interface, providing methods to interact with the
 * LanguageRepository and perform operations related to user languages.
 * </p>
 *
 * <p>Fields:</p>
 * <ul>
 *     <li>languageRepository: The LanguageRepository interface, allowing this service to interact with the database
 *     to perform CRUD operations on Language entities.</li>
 * </ul>
 *
 * <p>Methods:</p>
 * <ul>
 *     <li>findAllByUserId(userId): Retrieves a list of Language objects associated with the specified userId. The method
 *     fetches languages from the database based on the given userId.</li>
 *     <li>save(lang): Saves a new Language object in the database. The method creates a new Language object based on the
 *     provided input, then saves it to the database.</li>
 * </ul>
 *
 * <p>Usage:</p>
 * <p>
 * LanguageServiceImpl is used to manage user languages in the application. It is typically utilized by the application's
 * endpoints or controllers that handle user interactions related to languages. For example, when a user adds a new
 * language to their profile, this service is responsible for saving the language to the database. Similarly, when the
 * application needs to retrieve a user's languages, this service is used to fetch the relevant data from the database
 * and return it to the calling component.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class LanguageServiceImpl implements LanguageService {
    private final LanguageRepository languageRepository;

    @Override
    public List<Language> findAllByUserId(int userId) {
        return languageRepository.findAllByUserId(userId);
    }

    @Override
    public Language save(Language lang) {
        return languageRepository.save(lang);
    }
}
