package com.friendfinder.friendfinderrest.service;

import com.friendfinder.friendfindercommon.dto.postLikeDto.PostLikeDto;
import com.friendfinder.friendfindercommon.entity.Post;
import com.friendfinder.friendfindercommon.entity.PostLike;
import com.friendfinder.friendfindercommon.entity.types.LikeStatus;
import com.friendfinder.friendfindercommon.mapper.PostLikeMapper;
import com.friendfinder.friendfindercommon.repository.PostLikeRepository;
import com.friendfinder.friendfindercommon.repository.PostRepository;
import com.friendfinder.friendfindercommon.security.CurrentUser;
import com.friendfinder.friendfindercommon.service.impl.LikeAndDislikeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.friendfinder.friendfinderrest.util.TestUtil.createPost;
import static com.friendfinder.friendfinderrest.util.TestUtil.mockCurrentUser;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LikeAndDislikeServiceImplTest {

    @Mock
    private PostLikeRepository postLikeRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostLikeMapper postLikeMapper;

    @InjectMocks
    private LikeAndDislikeServiceImpl likeAndDislikeService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        likeAndDislikeService = new LikeAndDislikeServiceImpl(postLikeRepository,
                postRepository, postLikeMapper);
    }

    @Test
     void testSaveReactionLike() {
        CurrentUser currentUser = mockCurrentUser();
        Post post = createPost();
        PostLikeDto postLikeDto = new PostLikeDto();
        postLikeDto.setLikeStatus(LikeStatus.DISLIKE);

        PostLike existingLike = new PostLike();
        existingLike.setId(1);
        existingLike.setUser(currentUser.getUser());
        existingLike.setPost(post);
        existingLike.setLikeStatus(LikeStatus.DISLIKE);


        when(postLikeRepository.findByUserIdAndPostId(currentUser.getUser().getId(), post.getId())).thenReturn(Optional.empty());
        when(postLikeMapper.map(postLikeDto)).thenReturn(new PostLike());

        PostLike result = likeAndDislikeService.saveReaction(postLikeDto, currentUser, post);

        verify(postLikeRepository, times(1)).findByUserIdAndPostId(anyInt(), anyInt());
        verify(postRepository, times(1)).save(post);
        assertNull(result);

    }

    @Test
    void testRemoveLike() {
        CurrentUser currentUser = mockCurrentUser();
        Post post = createPost();
        PostLikeDto postLikeDto = new PostLikeDto();
        postLikeDto.setLikeStatus(LikeStatus.LIKE);

        PostLike existingLike = new PostLike();
        existingLike.setId(1);
        existingLike.setUser(currentUser.getUser());
        existingLike.setPost(post);
        existingLike.setLikeStatus(LikeStatus.LIKE);

        when(postLikeRepository.findByUserIdAndPostId(anyInt(), anyInt())).thenReturn(Optional.of(existingLike));

        PostLike result = likeAndDislikeService.saveReaction(postLikeDto, currentUser, post);

        verify(postLikeRepository, times(1)).findByUserIdAndPostId(anyInt(), anyInt());
        verify(postRepository, times(1)).save(post);
        verify(postLikeRepository, times(1)).delete(existingLike);
        assertNull(result);
    }

    @Test
    void testRemoveDislike() {
        CurrentUser currentUser = mockCurrentUser();
        Post post = createPost();

        PostLikeDto postDislikeDto = new PostLikeDto();
        postDislikeDto.setLikeStatus(LikeStatus.DISLIKE);

        PostLike existingDislike = new PostLike();
        existingDislike.setId(1);
        existingDislike.setUser(currentUser.getUser());
        existingDislike.setPost(post);
        existingDislike.setLikeStatus(LikeStatus.DISLIKE);

        when(postLikeRepository.findByUserIdAndPostId(anyInt(), anyInt())).thenReturn(Optional.of(existingDislike));

        PostLike result = likeAndDislikeService.saveReaction(postDislikeDto, currentUser, post);

        verify(postLikeRepository, times(1)).findByUserIdAndPostId(anyInt(), anyInt());
        verify(postRepository, times(1)).save(post);
        verify(postLikeRepository, times(1)).delete(existingDislike);
        assertNull(result);
    }
}
