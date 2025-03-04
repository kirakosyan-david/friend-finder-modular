package com.friendfinder.friendfindercommon.service.impl;

import com.friendfinder.friendfindercommon.entity.Country;
import com.friendfinder.friendfindercommon.repository.CountryRepository;
import com.friendfinder.friendfindercommon.service.MainService;
import com.friendfinder.friendfindercommon.util.ImageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * <p>
 * MainServiceImpl is a service class that provides various utility methods related to handling images and videos, as well
 * as retrieving data related to countries.
 * </p>
 *
 * <p>Fields:</p>
 * <ul>
 *     <li>imageUploadPath: The path to the directory where image files are uploaded and stored.</li>
 *     <li>videoUploadPath: The path to the directory where video files are uploaded and stored.</li>
 *     <li>userProfilePicPath: The path to the directory where user profile pictures are stored.</li>
 *     <li>userBgProfilePicPath: The path to the directory where user background profile pictures are stored.</li>
 *     <li>countryRepository: The CountryRepository interface used to access and retrieve country-related data from the database.</li>
 * </ul>
 *
 * <p>Methods:</p>
 * <ul>
 *     <li>findAllCountries(): Retrieves a list of all countries from the database.</li>
 *     <li>getImage(imageName): Retrieves the byte array representation of an image with the specified image name from the
 *     imageUploadPath directory.</li>
 *     <li>getVideo(imageName): Retrieves the byte array representation of a video with the specified image name from the
 *     videoUploadPath directory.</li>
 *     <li>getProfilePic(imageName): Retrieves the byte array representation of a user's profile picture with the specified
 *     image name from the userProfilePicPath directory.</li>
 *     <li>getBgProfilePic(imageName): Retrieves the byte array representation of a user's background profile picture with
 *     the specified image name from the userBgProfilePicPath directory.</li>
 * </ul>
 *
 * <p>Usage:</p>
 * <p>
 * MainServiceImpl provides utility methods to handle image and video retrieval, as well as access to country-related data.
 * It is used in various parts of the application to serve images and videos to users, such as profile pictures, background
 * profile pictures, and media content associated with posts. Additionally, it offers the capability to retrieve a list of all
 * countries for user selection or display purposes in the application. By providing these methods, the class enhances the
 * overall functionality of the application, allowing it to efficiently manage and serve media content and country data.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MainServiceImpl implements MainService {

    @Value("${post.upload.image.path}")
    private String imageUploadPath;

    @Value("${post.video.upload.image.path}")
    private String videoUploadPath;

    @Value("${user.profile.picture.path}")
    private String userProfilePicPath;

    @Value("${user.profile.background-picture.path}")
    private String userBgProfilePicPath;

    private final CountryRepository countryRepository;

    @Override
    public List<Country> findAllCountries() {
        return countryRepository.findAll();
    }

    @Override
    public @ResponseBody byte[] getImage(String imageName) {
        return ImageUtil.getBytes(imageName, imageUploadPath);
    }

    @Override
    public @ResponseBody byte[] getVideo(String imageName) {
        return ImageUtil.getBytes(imageName, videoUploadPath);
    }

    @Override
    public @ResponseBody byte[] getProfilePic(String imageName) {
        return ImageUtil.getBytes(imageName, userProfilePicPath);
    }

    @Override
    public @ResponseBody byte[] getBgProfilePic(String imageName) {
        return ImageUtil.getBytes(imageName, userBgProfilePicPath);
    }
}
