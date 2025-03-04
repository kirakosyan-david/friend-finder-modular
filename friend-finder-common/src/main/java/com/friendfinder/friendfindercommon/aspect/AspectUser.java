package com.friendfinder.friendfindercommon.aspect;

import com.friendfinder.friendfindercommon.dto.userDto.UserLoginRequestDto;
import com.friendfinder.friendfindercommon.dto.userDto.UserRegisterRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class AspectUser {


    @After("execution(* com.friendfinder.friendfindercommon.service.UserService.userLogin(com.friendfinder.friendfindercommon.dto.userDto.UserLoginRequestDto)) && args(dto)")
    public void afterUserLogin(UserLoginRequestDto dto) {
        log.info("Login user: email: " + dto.getEmail());
    }

    @After("execution(* com.friendfinder.friendfindercommon.service.UserService.userRegister(com.friendfinder.friendfindercommon.dto.userDto.UserRegisterRequestDto)) && args(dto)")
    public void afterUserRegister(UserRegisterRequestDto dto) {
        log.info("Register user: name: " + dto.getName() + ", surname: " + dto.getSurname() + ", email: " + dto.getEmail());
    }
}
