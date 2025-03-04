package com.friendfinder.friendfinderweb.controller;

import com.friendfinder.friendfindercommon.entity.Chat;
import com.friendfinder.friendfindercommon.entity.Message;
import com.friendfinder.friendfindercommon.entity.User;
import com.friendfinder.friendfindercommon.security.CurrentUser;
import com.friendfinder.friendfindercommon.service.ChatService;
import com.friendfinder.friendfindercommon.service.FriendRequestService;
import com.friendfinder.friendfindercommon.service.MessageService;
import com.friendfinder.friendfindercommon.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/newsfeed")
@RequiredArgsConstructor
public class ChatController {

    private final UserService userService;
    private final ChatService chatService;
    private final MessageService messageService;
    private final FriendRequestService friendRequestService;

    @GetMapping("/messages")
    public String messagesPage(@AuthenticationPrincipal CurrentUser currentUser, ModelMap modelMap) {
        List<Chat> allChats = chatService.findAllByCurrentUserId(currentUser.getUser().getId());
        allChats.addAll(chatService.findAllByAnotherUserId(currentUser.getUser().getId()));

        modelMap.addAttribute("user", currentUser.getUser());
        modelMap.addAttribute("chats", new HashSet<>(allChats));
        modelMap.addAttribute("users", userService.userForAddFriend(currentUser));
        modelMap.addAttribute("allExceptCurrentUser", userService.findAllExceptCurrentUser(currentUser.getUser().getId()));
        modelMap.addAttribute("requestSenders", friendRequestService.findSenderByReceiverId(currentUser.getUser().getId()));

        return "newsfeed-messages";
    }

    @GetMapping("/chat/create/{id}")
    public String createNewChat(@PathVariable("id") int userId, @AuthenticationPrincipal CurrentUser currentUser) {
        chatService.create(userId, currentUser.getUser());
        return "redirect:/newsfeed/messages";
    }

    @PostMapping("/send-message")
    public String sendMessage(@AuthenticationPrincipal CurrentUser currentUser,
                              @RequestParam("chatId") int chatId,
                              @RequestParam("receiverId") int receiverId,
                              @RequestParam("content") String content) {
        Optional<User> userById = userService.findUserById(receiverId);
        if (userById.isEmpty()) {
            return "redirect:/newsfeed/messages";
        }

        Optional<Chat> chatById = chatService.findById(chatId);
        if (chatById.isEmpty()) {
            return "redirect:/newsfeed/messages";
        }

        messageService.save(Message.builder()
                .receiver(userById.get())
                .chat(chatById.get())
                .sender(currentUser.getUser())
                .content(content)
                .sentAt(LocalDateTime.now())
                .build());

        return "redirect:/newsfeed/messages";
    }


}