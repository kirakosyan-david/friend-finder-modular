package com.friendfinder.friendfindercommon.repository;

import com.friendfinder.friendfindercommon.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {

}