package com.friendfinder.friendfindercommon.dto.userDto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserLoginRequestDto {
    private String email;
    private String password;
}
