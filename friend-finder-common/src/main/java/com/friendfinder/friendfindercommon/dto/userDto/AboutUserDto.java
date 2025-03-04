package com.friendfinder.friendfindercommon.dto.userDto;

import com.friendfinder.friendfindercommon.entity.Interest;
import com.friendfinder.friendfindercommon.entity.Language;
import com.friendfinder.friendfindercommon.entity.WorkExperiences;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AboutUserDto {

    private List<Interest> interestList;
    private List<Language> languageList;
    private List<WorkExperiences> workExperiencesList;

}
