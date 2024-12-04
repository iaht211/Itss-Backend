package com.example.itss.service;

import com.example.itss.domain.User;
import com.example.itss.domain.response.ResponseDto;
import com.example.itss.domain.response.ResultPaginationDto;
import com.example.itss.domain.response.user.ResUserDto;
import com.example.itss.repository.UserRepository;
import com.example.itss.util.error.ValidInforException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseDto<ResUserDto> createUser(User user) {
        // if (userRepository.existsByEmail(user.getEmail())) {
        // throw new FomatException("Email đã tốn tại");
        // }
        // String hashPass = passwordEncoder.encode(user.getPassword());
        // user.setPassword(hashPass);
        User savedUser = userRepository.save(user);

        ResUserDto resUserDto = this.convertToResUserDto(savedUser);
        return new ResponseDto<>(201, "Tạo tài khoản thành công", resUserDto);
    }

    public ResponseDto<ResultPaginationDto> getAllUsersPagination(Specification<User> spec, Pageable pageable) {
        Page<User> page = this.userRepository.findAll(spec, pageable);

        ResultPaginationDto resultPaginationDto = new ResultPaginationDto();
        ResultPaginationDto.Meta meta = new ResultPaginationDto.Meta();

        List<ResUserDto> listUsers = page.getContent()
                .stream().map(item -> new ResUserDto(
                        item.getId(),
                        item.getName(),
                        item.getEmail(),
                        item.getAge(),
                        item.getAddress(),
                        item.getPhone(),
                        item.getCreatedAt(),
                        item.getUpdatedAt()))
                .collect(Collectors.toList());

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(page.getTotalPages());
        meta.setTotal((int) page.getTotalElements());

        resultPaginationDto.setResult(listUsers);
        resultPaginationDto.setMeta(meta);

        return new ResponseDto<>(200, "Tất cả người dùng", resultPaginationDto);
    }

    public ResponseDto<Void> deleteUser(Long id) throws ValidInforException {
        Optional<User> optional = this.userRepository.findById(id);
        if (!optional.isPresent()) {
            throw new ValidInforException("Không tồn tại người dùng");
        }
        this.userRepository.delete(optional.get());
        return new ResponseDto<>(204, "Xoá người dùng", null);
    }

    public ResponseDto<ResUserDto> updateUser(User user) throws ValidInforException {
        Optional<User> optional = this.userRepository.findById(user.getId());
        if (optional.isPresent()) {
            User currUser = optional.get();
            currUser.setAddress(user.getAddress());
            currUser.setAge(user.getAge());
            currUser.setName(user.getName());
            currUser.setEmail(user.getEmail());

            currUser = this.userRepository.save(currUser);
            ResUserDto updateUserDto = convertToResUserDto(currUser);
            return new ResponseDto<>(200, "Cập nhật người dùng", updateUserDto);
        } else {
            throw new ValidInforException("Không tồn tại người dùng");
        }
    }

    public ResUserDto fetchUserById(long id) {
        Optional<User> optional = this.userRepository.findById(id);
        if (optional.isPresent()) {
            ResUserDto resUserDto = convertToResUserDto(optional.get());
            return resUserDto;
        }
        return null;
    }

    public ResUserDto handleGetUserByUsername(String username) {
        Optional<User> optional = this.userRepository.findByEmail(username);
        if (optional.isPresent()) {
            ResUserDto resUserDto = convertToResUserDto(optional.get());
            return resUserDto;
        }
        return null;
    }

    public boolean isEmailExist(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public ResUserDto convertToResUserDto(User user) {
        ResUserDto res = new ResUserDto();
        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setAddress(user.getAddress());
        res.setAge(user.getAge());
        res.setName(user.getName());
        res.setPhone(user.getPhone());
        res.setCreatedAt(user.getCreatedAt());
        res.setUpdatedAt(user.getUpdatedAt());
        return res;
    }
}
