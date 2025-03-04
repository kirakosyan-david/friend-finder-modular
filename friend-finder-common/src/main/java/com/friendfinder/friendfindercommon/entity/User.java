package com.friendfinder.friendfindercommon.entity;

import com.friendfinder.friendfindercommon.entity.types.UserGender;
import com.friendfinder.friendfindercommon.entity.types.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String surname;

    @NotBlank
    private String email;

    @NotEmpty
    private String password;

    @Column(name = "date_of_birth")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateOfBirth;

    @Enumerated(value = EnumType.STRING)
    private UserGender gender;
    private String city;

    @ManyToOne
    private Country country;

    @Column(name = "profile_pic")
    private String profilePicture;

    @Column(name = "profile_bg_pic")
    private String profileBackgroundPic;

    @Column(name = "personal_information")
    private String personalInformation;

    private boolean enabled;

    private String token;

    @Enumerated(value = EnumType.STRING)
    private UserRole role;


    /**
     * The getter that automatically created @Data didn't work, so
     * I wrote a separate one for the date and used it in the
     * edit-profile-basic.html input of the date type to pass the value
     * */
    public Date getDateOfBirth() {
        return dateOfBirth;
    }
}
