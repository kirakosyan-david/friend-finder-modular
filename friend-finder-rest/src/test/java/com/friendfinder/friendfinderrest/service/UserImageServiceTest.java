package com.friendfinder.friendfinderrest.service;

import com.friendfinder.friendfindercommon.entity.User;
import com.friendfinder.friendfindercommon.entity.UserImage;
import com.friendfinder.friendfindercommon.repository.UserImageRepository;
import com.friendfinder.friendfindercommon.repository.UserRepository;
import com.friendfinder.friendfindercommon.security.CurrentUser;
import com.friendfinder.friendfindercommon.service.impl.UserImageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.friendfinder.friendfinderrest.util.TestUtil.createImage;
import static com.friendfinder.friendfinderrest.util.TestUtil.mockCurrentUser;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
 class UserImageServiceTest {


    @Mock
    private UserImageRepository userImageRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserImageServiceImpl userImageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userImageService = new UserImageServiceImpl(userImageRepository, userRepository);
        CurrentUser  currentUser = mockCurrentUser();
    }

    @Test
    void testUserImagePageByUserId() {
        int userId = 123;
        int pageNumber = 1;
        List<UserImage> userImages = new ArrayList<>();
        UserImage userImage1 = new UserImage();
        userImage1.setId(1);
        userImages.add(userImage1);

        when(userImageRepository.findUserImagesByUserIdIn(anyList(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(userImages));

        Page<UserImage> result = userImageService.userImagePageByUserId(userId, pageNumber);

        assertFalse(result.isEmpty());
        assertEquals(userImages, result.getContent());

        verify(userImageRepository).findUserImagesByUserIdIn(anyList(), any(Pageable.class));
    }


    @Test
    void testUserImageSave() {
        CurrentUser currentUser = mockCurrentUser();
        UserImage userImage = new UserImage();

        userImageService.userImageSave(userImage, currentUser);

        ArgumentCaptor<UserImage> captor = ArgumentCaptor.forClass(UserImage.class);
        verify(userImageRepository).save(captor.capture());

        UserImage savedUserImage = captor.getValue();
        assertNotNull(savedUserImage);
        assertEquals(currentUser.getUser(), savedUserImage.getUser());
        assertEquals(currentUser.getUser().getProfilePicture(), savedUserImage.getImageName());

    }



    @Test
    void testPostUserById() {
        int userId = 1;
        User user = new User();
        user.setId(userId);
        UserImage userImage = new UserImage();
        userImage.setId(1);
        userImage.setUser(user);
        UserImage userImage2 = new UserImage();
        userImage2.setId(2);
        userImage2.setUser(user);
        List<UserImage> userImages = List.of(userImage, userImage2);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userImageService.getUserImageById(userId)).thenReturn(userImages);
        List<UserImage> result = userImageService.getUserImageById(userId);
        assertNotNull(result);
        assertEquals(userImages.size(), result.size());
        assertEquals(userImage.getId(), result.get(0).getId());
        assertEquals(userImage2.getId(), result.get(1).getId());
    }

    @Test
    void testDeletePostId() {
        int postId = 1;
        when(userImageRepository.findById(postId)).thenReturn(Optional.empty());

        UserImage existingPost = createImage();
        when(userImageRepository.findById(postId)).thenReturn(Optional.of(existingPost));
        UserImage deletedPost = userImageService.deleteUserImageById(postId);

        assertNull(deletedPost, "Deleted post should be null for non-existing ID.");
    }

}
