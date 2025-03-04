package com.friendfinder.friendfindercommon.dto.exceptionRestDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestErrorDto {

    private int statusCode;

    private String errorMessage;
}