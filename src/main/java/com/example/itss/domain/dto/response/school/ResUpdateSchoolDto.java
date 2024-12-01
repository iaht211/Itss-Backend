package com.example.itss.domain.dto.response.school;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResUpdateSchoolDto {
    private Integer id;
    private String name;
    private String email;
    private String address;
}