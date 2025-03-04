package com.friendfinder.friendfinderrest.endpoint;

import com.friendfinder.friendfindercommon.dto.workEduDto.EducationCreateRequestDto;
import com.friendfinder.friendfindercommon.dto.workEduDto.EducationCreateResponseDto;
import com.friendfinder.friendfindercommon.dto.workEduDto.WorkExperiencesCreateRequestDto;
import com.friendfinder.friendfindercommon.dto.workEduDto.WorkExperiencesCreateResponseDto;
import com.friendfinder.friendfindercommon.entity.Education;
import com.friendfinder.friendfindercommon.entity.WorkExperiences;
import com.friendfinder.friendfindercommon.mapper.EducationMapper;
import com.friendfinder.friendfindercommon.mapper.WorkExperiencesMapper;
import com.friendfinder.friendfindercommon.security.CurrentUser;
import com.friendfinder.friendfindercommon.service.EducationService;
import com.friendfinder.friendfindercommon.service.WorkExperiencesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST API endpoint for handling user education and work experiences.
 *
 * <p>This class provides endpoints for users to add their education and work experiences to their profile.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/profile/work-education")
public class EducationWorkEndpoint {

    private final EducationService educationService;
    private final WorkExperiencesService workExperiencesService;
    private final EducationMapper educationMapper;
    private final WorkExperiencesMapper workExperiencesMapper;

    /**
     * Adds education details to the user's profile.
     *
     * @param education   The DTO containing the education details.
     * @param currentUser The currently authenticated user.
     * @return ResponseEntity with the response DTO containing the saved education details.
     */
    @PostMapping("/education/add")
    public ResponseEntity<EducationCreateResponseDto> educationAdd(@RequestBody EducationCreateRequestDto education,
                                                                   @AuthenticationPrincipal CurrentUser currentUser) {
        Education map = educationMapper.map(education);
        Education saveEducation = educationService.saveEducation(map, currentUser);
        return ResponseEntity.ok(educationMapper.mapToResponseDto(saveEducation));
    }

    /**
     * Adds work experiences details to the user's profile.
     *
     * @param workExperiences The DTO containing the work experiences details.
     * @param currentUser     The currently authenticated user.
     * @return ResponseEntity with the response DTO containing the saved work experiences details.
     */
    @PostMapping("/work/add")
    public ResponseEntity<WorkExperiencesCreateResponseDto> workAdd(@RequestBody WorkExperiencesCreateRequestDto workExperiences,
                                                                    @AuthenticationPrincipal CurrentUser currentUser) {
        WorkExperiences map = workExperiencesMapper.map(workExperiences);
        WorkExperiences saveWorkExperiences = workExperiencesService.saveWorkExperiences(map, currentUser);
        return ResponseEntity.ok(workExperiencesMapper.mapToResponseDto(saveWorkExperiences));
    }
}
