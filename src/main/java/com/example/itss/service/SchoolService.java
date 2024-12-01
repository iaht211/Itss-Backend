package com.example.itss.service;

import com.example.itss.domain.dto.request.school.ReqSchoolDto;
import com.example.itss.domain.dto.response.ResponseDto;
import com.example.itss.domain.dto.response.ResultPaginationDto;
import com.example.itss.domain.dto.response.school.ResSchoolDto;
import com.example.itss.domain.dto.response.school.ResUpdateSchoolDto;
import com.example.itss.domain.model.School;
import com.example.itss.repository.SchoolRepository;
import com.example.itss.util.error.FomatException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SchoolService {
    private final SchoolRepository schoolRepository;

    public SchoolService(SchoolRepository schoolRepository) {
        this.schoolRepository = schoolRepository;
    }

    public ResponseDto<ResultPaginationDto> getAllSchoolsPagination(Specification<School> spec, Pageable pageable) {
        Page<School> page = this.schoolRepository.findAll(spec, pageable);

        ResultPaginationDto resultPaginationDto = new ResultPaginationDto();
        ResultPaginationDto.Meta meta = new ResultPaginationDto.Meta();

        List<ResSchoolDto> listSchools = page.getContent()
                .stream().map(item -> new ResSchoolDto(
                        item.getId(),
                        item.getName(),
                        item.getEmail(),
                        item.getAddress()))
                .collect(Collectors.toList());

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(page.getTotalPages());
        meta.setTotal((int) page.getTotalElements());

        resultPaginationDto.setResult(listSchools);
        resultPaginationDto.setMeta(meta);

        return new ResponseDto<>(200, "Tất cả người dùng", resultPaginationDto);
    }

    public ResponseDto<ResSchoolDto> createSchool(School school) throws FomatException {
        if (schoolRepository.existsByEmail(school.getEmail())) {
            throw new FomatException("Email đã tồn tại");
        }

        School curr = this.schoolRepository.save(school);

        ResSchoolDto resSchoolDto = this.convertToResSchoolDto(curr);
        return new ResponseDto<>(201, "Tạo tài khoản thành công", resSchoolDto);
    }

    public ResponseDto<ResSchoolDto> updateSchool(ResSchoolDto schoolDto) throws FomatException {
        Optional<School> optionalSchool = this.schoolRepository.findByEmail(schoolDto.getEmail());
        if (optionalSchool.isPresent()) {
            School currentSchool = optionalSchool.get();

            currentSchool.setName(schoolDto.getName());
            currentSchool.setEmail(schoolDto.getEmail());
            currentSchool.setAddress(schoolDto.getAddress());

            currentSchool = this.schoolRepository.save(currentSchool);

            ResSchoolDto updatedSchoolDto = convertToResSchoolDto(currentSchool);
            return new ResponseDto<>(200, "Cập nhật trường học thành công", updatedSchoolDto);
        } else {
            throw new FomatException("Không tìm thấy trường học với ID: " + schoolDto.getId());
        }
    }

    public ResponseDto<Void> deleteSchool(ResSchoolDto resSchoolDto) throws FomatException {
        Optional<School> optionalSchool = this.schoolRepository.findById(resSchoolDto.getId());
        if (optionalSchool.isPresent()) {
            this.schoolRepository.delete(optionalSchool.get());
            return new ResponseDto<>(204, "Xóa trường học thành công", null);
        } else {
            throw new FomatException("Không tìm thấy trường học với email: " + optionalSchool.get().getEmail());
        }
    }

    public ResSchoolDto convertToResSchoolDto(School school) {
        if (school == null) {
            return null;
        }

        ResSchoolDto resSchoolDto = new ResSchoolDto();
        resSchoolDto.setId(school.getId());
        resSchoolDto.setName(school.getName());
        resSchoolDto.setEmail(school.getEmail());
        resSchoolDto.setAddress(school.getAddress());
        return resSchoolDto;
    }

}