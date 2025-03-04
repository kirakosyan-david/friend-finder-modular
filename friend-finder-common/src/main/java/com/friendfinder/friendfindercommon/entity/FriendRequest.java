package com.friendfinder.friendfindercommon.entity;

import com.friendfinder.friendfindercommon.entity.types.FriendStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "friend_request")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FriendRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @NotEmpty
    private User sender;

    @ManyToOne
    @NotEmpty
    private User receiver;

    @Enumerated(value = EnumType.STRING)
    private FriendStatus status;
}
