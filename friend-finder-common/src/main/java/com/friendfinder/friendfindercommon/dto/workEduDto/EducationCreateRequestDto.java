package com.friendfinder.friendfindercommon.dto.workEduDto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class EducationCreateRequestDto {
    private String edName;
    private int edFromDate;
    private int edToDate;
    private String edDescription;
}
