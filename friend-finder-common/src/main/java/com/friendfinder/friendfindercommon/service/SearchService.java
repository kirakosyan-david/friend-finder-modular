package com.friendfinder.friendfindercommon.service;

import com.friendfinder.friendfindercommon.entity.User;
import com.friendfinder.friendfindercommon.security.CurrentUser;
import org.springframework.data.domain.Page;

public interface SearchService {
    Page<User> searchByKeyword(String keyword, CurrentUser currentUser, int currentPage);

}
