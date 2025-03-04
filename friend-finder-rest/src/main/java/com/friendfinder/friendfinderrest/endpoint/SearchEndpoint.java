package com.friendfinder.friendfinderrest.endpoint;

import com.friendfinder.friendfindercommon.entity.User;
import com.friendfinder.friendfindercommon.security.CurrentUser;
import com.friendfinder.friendfindercommon.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The SearchEndpoint class defines RESTful endpoints for searching and retrieving potential friends.
 * It provides methods to search for users by keyword and retrieve results in paginated form.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/search/friend")
public class SearchEndpoint {

    private final SearchService searchService;

    /**
     * Endpoint for searching potential friends by keyword and page number.
     *
     * @param keyword     The keyword to use for searching potential friends.
     * @param currentPage The page number to retrieve for paginated results.
     * @param currentUser The CurrentUser object representing the currently logged-in user.
     * @return ResponseEntity containing a list of users who match the search criteria for the specified page.
     */
    @PostMapping("/{pageNumber}")
    public ResponseEntity<List<User>> listByPageSearch(@RequestParam String keyword,
                                                       @PathVariable("pageNumber") int currentPage,
                                                       @AuthenticationPrincipal CurrentUser currentUser) {
        Page<User> page = searchService.searchByKeyword(keyword, currentUser, currentPage);
        List<User> content = page.getContent();
        return ResponseEntity.ok(content);
    }
}
