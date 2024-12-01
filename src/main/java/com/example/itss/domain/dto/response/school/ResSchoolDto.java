package com.example.itss.domain.dto.response.school;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResSchoolDto {
    private Integer id;
    private String name;
    private String email;
    private String address;
}
