package com.friendfinder.friendfindercommon.dto.workEduDto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class EducationCreateResponseDto {
    private String edName;
    private int edFromDate;
    private int edToDate;
    private String edDescription;
    private int userId;
}
