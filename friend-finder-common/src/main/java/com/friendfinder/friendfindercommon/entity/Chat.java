package com.friendfinder.friendfindercommon.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "chat")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "current_user_id")
    @NotEmpty(message = "Current user can't be empty")
    private User currentUser;

    @ManyToOne
    @JoinColumn(name = "another_user_id")
    @NotEmpty(message = "Receiver user can't be empty")
    private User anotherUser;


    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    private List<Message> messages;

}
