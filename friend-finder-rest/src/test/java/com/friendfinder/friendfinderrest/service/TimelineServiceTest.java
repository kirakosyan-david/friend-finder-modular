package com.friendfinder.friendfinderrest.service;

import com.friendfinder.friendfindercommon.dto.userDto.UserUpdateRequestDto;
import com.friendfinder.friendfindercommon.entity.Country;
import com.friendfinder.friendfindercommon.entity.User;
import com.friendfinder.friendfindercommon.entity.UserImage;
import com.friendfinder.friendfindercommon.entity.types.UserGender;
import com.friendfinder.friendfindercommon.repository.CountryRepository;
import com.friendfinder.friendfindercommon.repository.UserRepository;
import com.friendfinder.friendfindercommon.security.CurrentUser;
import com.friendfinder.friendfindercommon.service.UserImageService;
import com.friendfinder.friendfindercommon.service.impl.TimelineServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.friendfinder.friendfinderrest.util.TestUtil.mockCurrentUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TimelineServiceTest {

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserImageService userImageService;

    @InjectMocks
    private TimelineServiceImpl timelineService;

    private CurrentUser currentUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        currentUser = mockCurrentUser();
    }

    @Test
    void testFindAllCountries() {
        List<Country> countries = new ArrayList<>();
        countries.add(new Country(1, "Country 1"));
        countries.add(new Country(2, "Country 2"));

        when(countryRepository.findAll()).thenReturn(countries);
        List<Country> result = countryRepository.findAll();

        assertEquals(countries, result);
    }

    @Test
    void testUpdateUser() {
        UserUpdateRequestDto userDto = UserUpdateRequestDto.builder()
                .name("John")
                .surname("Doe")
                .email("john.doe@example.com")
                .dateOfBirth(new Date(2000, 10, 10))
                .gender(UserGender.MALE)
                .city("New York")
                .country(new Country(1, "United States"))
                .personalInformation("Some personal info")
                .build();

        User loggedInUser = currentUser.getUser();

        when(timelineService.updateUser(userDto, currentUser)).thenReturn(loggedInUser);

        User updatedUser = timelineService.updateUser(userDto, currentUser);

        assertEquals(userDto.getName(), updatedUser.getName());
        assertEquals(userDto.getSurname(), updatedUser.getSurname());
        assertEquals(userDto.getCity(), updatedUser.getCity());
        assertEquals(userDto.getEmail(), updatedUser.getEmail());
        assertEquals(userDto.getGender(), updatedUser.getGender());
        assertEquals(userDto.getDateOfBirth(), updatedUser.getDateOfBirth());
        assertEquals(userDto.getPersonalInformation(), updatedUser.getPersonalInformation());
    }

    @Test
    void testUpdateUserProfilePic() {
        byte[] profilePicData = "Dummy profile pic data".getBytes();

        MockMultipartFile image = new MockMultipartFile(
                "image1010IgOsadlCAAsd",
                "image1010IgOsadlCAAsd.jpg",
                "image/jpeg",
                profilePicData
        );

        CurrentUser user = currentUser;
        UserImage userImage = UserImage.builder()
                .user(user.getUser())
                .imageName("profilePic")
                .build();

        when(timelineService.updateUserProfilePic(image, user, userImage)).thenReturn(user.getUser());
        User updatedUser = timelineService.updateUserProfilePic(image, user, userImage);

        assertTrue(image.getName().contains("image1010IgOsadlCAAsd") &&
                updatedUser.getProfilePicture().contains("image1010IgOsadlCAAsd"));
    }

    @Test
    void testUpdateUserProfileBackgroundPic() {
        byte[] profilePicData = "Dummy profile pic data".getBytes();

        MockMultipartFile image = new MockMultipartFile(
                "image1010IgOsadlCAAsd",
                "image1010IgOsadlCAAsd.jpg",
                "image/jpeg",
                profilePicData
        );

        CurrentUser user = currentUser;

        when(timelineService.updateUserProfileBackgroundPic(image, user)).thenReturn(user.getUser());
        User updatedUser = timelineService.updateUserProfileBackgroundPic(image, user);

        assertTrue(image.getName().contains("image1010IgOsadlCAAsd") &&
                updatedUser.getProfileBackgroundPic().contains("image1010IgOsadlCAAsd"));
    }



}
