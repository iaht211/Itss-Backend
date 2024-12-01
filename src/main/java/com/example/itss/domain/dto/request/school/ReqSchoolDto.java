package com.example.itss.domain.dto.request.school;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqSchoolDto {
    private Integer id;
    private String name;
    private String email;
    private String address;
}
