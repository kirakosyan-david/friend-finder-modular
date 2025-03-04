package com.friendfinder.friendfindercommon.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "message")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @NotEmpty(message = "chat can't be empty")
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @ManyToOne
    @NotEmpty(message = "sender can't be empty")
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @NotEmpty(message = "receiver can't be empty")
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @Column(name = "content")
    private String content;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    public boolean isSentByCurrentUser(User user){
        return sender.getId() == user.getId();
    }
}
