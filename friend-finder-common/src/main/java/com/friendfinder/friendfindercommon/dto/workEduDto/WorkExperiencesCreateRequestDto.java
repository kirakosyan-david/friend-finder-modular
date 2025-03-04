package com.friendfinder.friendfindercommon.dto.workEduDto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class WorkExperiencesCreateRequestDto {
    private String company;
    private String weDesignation;
    private int weFromDate;
    private int weToDate;
    private String weCity;
    private String weDescription;
}
