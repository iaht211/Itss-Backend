package com.example.itss.service;

import com.example.itss.domain.dto.response.user.ResUpdateUserDto;
import com.example.itss.domain.dto.response.ResUserDto;
import com.example.itss.domain.dto.response.ResponseDto;
import com.example.itss.domain.dto.response.ResultPaginationDto;
import com.example.itss.domain.model.User;
import com.example.itss.repository.UserRepository;
import com.example.itss.util.error.FomatException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseDto<ResUserDto> createUser(User user) throws FomatException {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new FomatException("Email đã tốn tại");
        }
        String hashPass = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPass);
        User savedUser = userRepository.save(user);

        ResUserDto resUserDto = this.convertToResUserDto(savedUser);
        return new ResponseDto<>(201, "Tạo tài khoản thành công", resUserDto);
    }

    public ResponseDto<List<ResUserDto>> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<ResUserDto> resUserDtos = users.stream()
                .map(this::convertToResUserDto)
                .collect(Collectors.toList());

        return new ResponseDto<>(200, "Tất cả người dùng", resUserDtos);
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
                        item.getAvatar(),
                        item.getPhone(),
                        item.getAge(),
                        item.getGender(),
                        item.getAddress(),
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

    public ResponseDto<ResUpdateUserDto> updateUser(ResUserDto user) throws FomatException {
        Optional<User> optional = this.userRepository.findById(user.getId());
        User currUser = optional.get();
        if (currUser != null) {
            currUser.setAddress(user.getAddress());
            currUser.setGender(user.getGender());
            currUser.setAge(user.getAge());
            currUser.setName(user.getName());
            currUser.setAvatar(user.getAvatar());
            currUser.setPhone(user.getPhone());

            currUser = this.userRepository.save(currUser);
            ResUpdateUserDto updateUserDto = convertToUpdateUserDto(currUser);
            return new ResponseDto<>(200, "Tất cả người dùng", updateUserDto);
        } else {
            throw new FomatException("Không tồn tại người dùng");
        }
    }

    public ResponseDto<Void> deleteUser(ResUserDto resUserDto) throws FomatException {
        Optional<User> optional = this.userRepository.findById(resUserDto.getId());
        if (!optional.isPresent()) {
            throw new FomatException("Không tồn tại người dùng");
        }
        this.userRepository.delete(optional.get());
        return new ResponseDto<>(204, "Xoá người dùng", null);
    }

    public User handleGetUserByEmail(String email) {
        Optional<User> optional = this.userRepository.findByEmail(email);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    public User handleGetUserByUsername(String email) {
        Optional<User> optional = this.userRepository.findByEmail(email);
        if (optional.isPresent()) {
            User user = optional.get();
            return user;
        }
        return null;
    }

    public ResponseDto<ResUserDto> handleGetUserById(Integer id) {
        Optional<User> optional = this.userRepository.findById(id);
        if (optional.isPresent()) {
            User user = optional.get();
            ResUserDto resUserDto = this.convertToResUserDto(user);
            return new ResponseDto<>(200, "Tất cả người dùng", resUserDto);
        }
        return null;
    }

    public User handleUpdateResfreshToken(String email, String refreshToken) {
        User user = this.handleGetUserByEmail(email);
        if (user != null) {
            user.setRefreshToken(refreshToken);
            return this.userRepository.save(user);
        } else
            return null;
    }

    public User getUserByRefreshTokenAndEmail(String token, String email) {
        User user = this.userRepository.findByRefreshTokenAndEmail(token, email);
        return user;
    }

    public ResUpdateUserDto convertToUpdateUserDto(User user) {
        ResUpdateUserDto res = new ResUpdateUserDto();
        res.setId(user.getId());
        res.setAddress(user.getAddress());
        res.setAge(user.getAge());
        res.setName(user.getName());
        res.setGender(user.getGender());
        res.setUpdatedAt(user.getUpdatedAt());
        res.setEmail(user.getEmail());
        res.setPhone(user.getPhone());
        return res;
    }

    public ResUserDto convertToResUserDto(User user) {
        ResUserDto res = new ResUserDto();
        res.setId(user.getId());
        res.setAddress(user.getAddress());
        res.setAge(user.getAge());
        res.setName(user.getName());
        res.setAvatar(user.getAvatar());
        res.setGender(user.getGender());
        res.setCreatedAt(user.getCreatedAt());
        res.setUpdatedAt(user.getUpdatedAt());
        res.setEmail(user.getEmail());
        return res;
    }
}
