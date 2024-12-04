package com.example.itss.controller;

import com.example.itss.domain.dto.response.ResUserDto;
import com.example.itss.domain.dto.response.ResponseDto;
import com.example.itss.domain.dto.response.ResultPaginationDto;
import com.example.itss.domain.model.User;
import com.example.itss.service.UserService;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<ResponseDto<ResUserDto>> createUse(@Valid @RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));
    }

    @GetMapping("getAll")
    public ResponseEntity<ResponseDto<List<ResUserDto>>> getAllUser() {
        ResponseDto<List<ResUserDto>> response = userService.getAllUsers();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<ResultPaginationDto>> getUserPagination(
            @Filter Specification<User> spec,
            Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.getAllUsersPagination(spec, pageable));
    }

    @PutMapping
    public ResponseEntity<ResponseDto> updateUser(@RequestBody ResUserDto resUserDto) {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.updateUser(resUserDto));
    }

    @DeleteMapping
    public ResponseEntity<ResponseDto> deleteUser(@RequestBody ResUserDto resUserDto) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(this.userService.deleteUser(resUserDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<ResUserDto>> getUserById(@PathVariable("id") Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.handleGetUserById(id));
    }
}
