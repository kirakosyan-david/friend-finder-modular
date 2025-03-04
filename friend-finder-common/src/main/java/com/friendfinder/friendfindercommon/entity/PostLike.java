package com.friendfinder.friendfindercommon.entity;

import com.friendfinder.friendfindercommon.entity.types.LikeStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "post_like")
public class PostLike {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    @Enumerated(value = EnumType.STRING)
    @NotEmpty
    private LikeStatus likeStatus;

    @ManyToOne
    @NotEmpty
    private Post post;

    @ManyToOne
    @NotEmpty
    private User user;
}
