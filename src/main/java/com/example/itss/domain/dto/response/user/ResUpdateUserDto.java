package com.example.itss.domain.dto.response.user;

import com.example.itss.util.constant.GenderEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ResUpdateUserDto {
    private Integer id;
    private String name;
    private String email;
    private int age;
    private GenderEnum gender;
    private String address;
    private Instant updatedAt;
}
