package com.friendfinder.friendfindercommon.dto.languageDto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class LanguageCreateResponseDto {
    private int id;
    private String lang;
    private int userId;
}

