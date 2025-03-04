package com.friendfinder.friendfinderrest.service;

import com.friendfinder.friendfindercommon.dto.commentDto.CommentRequestDto;
import com.friendfinder.friendfindercommon.entity.Comment;
import com.friendfinder.friendfindercommon.entity.Post;
import com.friendfinder.friendfindercommon.mapper.CommentMapper;
import com.friendfinder.friendfindercommon.repository.CommentRepository;
import com.friendfinder.friendfindercommon.security.CurrentUser;
import com.friendfinder.friendfindercommon.service.UserActivityService;
import com.friendfinder.friendfindercommon.service.impl.CommentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.friendfinder.friendfinderrest.util.TestUtil.createComment;
import static com.friendfinder.friendfinderrest.util.TestUtil.mockCurrentUser;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
 class CommentServiceTest {


    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CurrentUser currentUser;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private UserActivityService userActivityService;

    @InjectMocks
    private CommentServiceImpl commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        commentService = new CommentServiceImpl(commentRepository, userActivityService,commentMapper);
        currentUser = mockCurrentUser();
    }

    @Test
    void testFindAll() {
        List<Comment> commentList = new ArrayList<>();
        Comment comment = new Comment();
        comment.setId(1);
        Comment comment2 = new Comment();
        comment2.setId(2);
        commentList.add(comment);
        commentList.add(comment2);

        when(commentRepository.findAll()).thenReturn(commentList);

        List<Comment> result = commentService.commentList();

        assertNotNull(result);
        assertEquals(commentList.size(), result.size());
        assertEquals(comment.getId(), result.get(0).getId());
        assertEquals(comment2.getId(), result.get(1).getId());
    }
    @Test
    void testAddComment(){
        CommentRequestDto commentDto = new CommentRequestDto();
        commentDto.setCommentaryText("Test comment text");
        Post post = new Post();
        CurrentUser currentUser = mockCurrentUser();
        Comment savedComment = new Comment();
        when(commentMapper.map(any(CommentRequestDto.class))).thenReturn(savedComment);
        Comment result = commentService.addComment(commentDto, currentUser, post);
        assertNull(result);
    }

    @Test
    void testDeleteComment(){
        int postId = 1;
        when(commentRepository.findById(postId)).thenReturn(Optional.empty());

        Comment comment = createComment();
        when(commentRepository.findById(postId)).thenReturn(Optional.of(comment));
        Comment deleteComment = commentService.deleteComment(postId);

        assertNull(deleteComment, "Deleted comment should be null for non-existing ID.");

    }
}
