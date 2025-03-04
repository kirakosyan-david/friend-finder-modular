package com.friendfinder.friendfindercommon.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "commentary")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @NotEmpty
    private User user;

    @ManyToOne
    @NotEmpty
    private Post post;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "date_time")
    private LocalDateTime datetime;

    private String commentaryText;
}
