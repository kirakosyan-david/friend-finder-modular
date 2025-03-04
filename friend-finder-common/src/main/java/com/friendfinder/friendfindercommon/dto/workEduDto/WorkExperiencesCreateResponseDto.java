package com.friendfinder.friendfindercommon.dto.workEduDto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class WorkExperiencesCreateResponseDto {
    private String company;
    private String weDesignation;
    private int weFromDate;
    private int weToDate;
    private String weCity;
    private String weDescription;
    private int userId;
}
