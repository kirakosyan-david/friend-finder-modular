package com.friendfinder.friendfindercommon.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Year;
import java.time.YearMonth;

@Entity
@Data
@Table(name = "education")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Education {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "ed_name")
    private String edName;

    @Column(name = "ed_from_date")
    @Min(1920)
    private int edFromDate;

    @Column(name = "ed_to_date")
    @Max(2023)
    private int edToDate;

    @Column(name = "ed_description")
    private String edDescription;

    @ManyToOne
    private User user;

}
