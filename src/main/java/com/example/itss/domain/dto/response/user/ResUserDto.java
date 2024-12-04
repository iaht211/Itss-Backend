package com.example.itss.domain.dto.response.user;

import com.example.itss.util.constant.GenderEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ResUserDto {
    private Integer id;
    private String name;
    private String email;
    private int age;
    private GenderEnum gender;
    private String phone;
    private String avatar;
    private String address;
    private Instant updatedAt;
}
