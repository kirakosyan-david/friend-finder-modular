package com.friendfinder.friendfindercommon.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "work_experiences")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkExperiences {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "company")
    private String company;

    @Column(name = "we_designation")
    private String weDesignation;

    @Column(name = "we_from_date")
    @Min(1930)
    private int weFromDate;

    @Column(name = "we_to_date")
    @Max(2030)
    private int weToDate;

    @Column(name = "we_city")
    private String weCity;

    @Column(name = "we_description")
    private String weDescription;

    @ManyToOne
    @NotEmpty
    private User user;

}
