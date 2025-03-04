package com.friendfinder.friendfindercommon.service;

import com.friendfinder.friendfindercommon.dto.chatDto.SendMessageDto;
import com.friendfinder.friendfindercommon.entity.Message;
import com.friendfinder.friendfindercommon.entity.User;

public interface MessageService {
    boolean save(SendMessageDto sendMessageDto, User currentUser);

    void save(Message message);
}
