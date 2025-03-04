package com.friendfinder.friendfinderrest.service;

import com.friendfinder.friendfindercommon.entity.Language;
import com.friendfinder.friendfindercommon.entity.User;
import com.friendfinder.friendfindercommon.repository.LanguageRepository;
import com.friendfinder.friendfindercommon.security.CurrentUser;
import com.friendfinder.friendfindercommon.service.impl.LanguageServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static com.friendfinder.friendfinderrest.util.TestUtil.mockCurrentUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LanguageServiceImplTest {

    @Mock
    private LanguageRepository languageRepository;

    @InjectMocks
    private LanguageServiceImpl languageService;

    @Test
    void testFindAllByUserId() {
        CurrentUser currentUser = mockCurrentUser();
        int userId = 1;
        List<Language> expectedLanguages = new ArrayList<>();
        expectedLanguages.add(new Language(1, "asd", currentUser.getUser()));
        expectedLanguages.add(new Language(2, "asdd", currentUser.getUser()));
        expectedLanguages.add(new Language(3, "asddd", currentUser.getUser()));

        when(languageRepository.findAllByUserId(userId)).thenReturn(expectedLanguages);

        List<Language> resultLanguages = languageService.findAllByUserId(userId);

        assertEquals(expectedLanguages, resultLanguages);
    }

    @Test
    void testSave() {
        User user = mockCurrentUser().getUser();
        Language language = Language.builder()
                .language("arm")
                .user(user)
                .build();

        when(languageRepository.save(language)).thenReturn(language);

        Language savedLanguage = languageService.save(language);

        assertEquals(language, savedLanguage);
        verify(languageRepository, times(1)).save(language);
    }

}
