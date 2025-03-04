package com.friendfinder.friendfindercommon.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_image")
public class UserImage {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    private String imageName;

    @ManyToOne
    private User user;
}
