package com.example.itss.controller;

import com.example.itss.domain.dto.request.school.ReqSchoolDto;
import com.example.itss.domain.dto.response.ResponseDto;
import com.example.itss.domain.dto.response.ResultPaginationDto;
import com.example.itss.domain.dto.response.school.ResSchoolDto;
import com.example.itss.domain.model.School;
import com.example.itss.service.SchoolService;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/schools")
public class SchoolController {
    private final SchoolService schoolService;

    public SchoolController(SchoolService schoolService) {
        this.schoolService = schoolService;
    }

    @GetMapping
    public ResponseEntity<ResponseDto<ResultPaginationDto>> getSchoolPagination(
            @Filter Specification<School> spec,
            Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.schoolService.getAllSchoolsPagination(spec, pageable));
    }

    @PostMapping
    public ResponseEntity<ResponseDto<ResSchoolDto>> createUse(@Valid @RequestBody School school) {
        return ResponseEntity.status(HttpStatus.CREATED).body(schoolService.createSchool(school));
    }

    @PutMapping
    public ResponseEntity<ResponseDto<ResSchoolDto>> updateSchool(@Valid @RequestBody ResSchoolDto resSchoolDto) {
        return ResponseEntity.status(HttpStatus.OK).body(schoolService.updateSchool(resSchoolDto));
    }

    @DeleteMapping
    public ResponseEntity<ResponseDto<Void>> deleteSchool(@RequestBody ResSchoolDto resSchoolDto) {
        return ResponseEntity.status(HttpStatus.OK).body(schoolService.deleteSchool(resSchoolDto));
    }
}
