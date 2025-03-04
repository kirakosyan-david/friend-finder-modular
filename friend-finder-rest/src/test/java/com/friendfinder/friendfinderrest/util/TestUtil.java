package com.friendfinder.friendfinderrest.util;

import com.friendfinder.friendfindercommon.entity.*;
import com.friendfinder.friendfindercommon.entity.types.UserGender;
import com.friendfinder.friendfindercommon.entity.types.UserRole;
import com.friendfinder.friendfindercommon.security.CurrentUser;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.Date;

@UtilityClass
public class TestUtil {


    public static CurrentUser mockCurrentUser() {
        Country country = new Country(1, "Afghanistan");
        User currentUser = User.builder()
                .id(1)
                .name("user")
                .surname("user")
                .email("user1@mail.ru")
                .password("user")
                .dateOfBirth(new Date(1990, 5, 15))
                .gender(UserGender.MALE)
                .city("New York")
                .country(country)
                .personalInformation("Some personal info")
                .enabled(true)
                .role(UserRole.USER)
                .build();
        return new CurrentUser(currentUser);
    }

    public static User mockUser() {
        return User.builder()
                .id(2)
                .name("user")
                .surname("user")
                .email("test@user.com")
                .password("user")
                .dateOfBirth(new Date(1990, 5, 15))
                .gender(UserGender.MALE)
                .city("New York")
                .country(new Country(1, "Afghanistan"))
                .personalInformation("Some personal info")
                .enabled(true)
                .role(UserRole.USER)
                .build();
    }

    public static UserImage createImage() {
        CurrentUser user = mockCurrentUser();
        return UserImage.builder()
                .id(1)
                .imageName("image.jpg")
                .user(user.getUser())
                .build();
    }


    public static Comment createComment(){
        CurrentUser user = mockCurrentUser();
        Post post = createPost();
        return Comment.builder()
                .id(1)
                .datetime(LocalDateTime.now())
                .user(user.getUser())
                .post(post)
                .commentaryText("This is a test comment.")
                .build();
    }

    public static Post createPost() {
        CurrentUser user = mockCurrentUser();
        return Post.builder()
                .description("barev")
                .imgName("image.jpg")
                .musicFileName("video.mp4")
                .postDatetime(new Date())
                .user(user.getUser())
                .build();
    }

    public static User mockUserFirst() {
        return User.builder()
                .id(1)
                .name("user")
                .surname("user")
                .email("user@friendfinder.com")
                .password("user")
                .dateOfBirth(new Date(1990, 5, 15))
                .gender(UserGender.MALE)
                .city("New York")
                .country(new Country(1, "Afghanistan"))
                .personalInformation("Some personal info")
                .enabled(true)
                .role(UserRole.ADMIN)
                .build();
    }

    public static User mockUserSecond() {
        return User.builder()
                .id(2)
                .name("user")
                .surname("user")
                .email("test@user.com")
                .password("user")
                .dateOfBirth(new Date(1990, 5, 15))
                .gender(UserGender.MALE)
                .city("New York")
                .country(new Country(1, "Afghanistan"))
                .personalInformation("Some personal info")
                .enabled(true)
                .role(UserRole.USER)
                .build();
    }
}
