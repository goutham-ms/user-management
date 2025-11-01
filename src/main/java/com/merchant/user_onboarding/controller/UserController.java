package com.merchant.user_onboarding.controller;

import com.merchant.user_onboarding.service.UserService;
import com.merchant.user_onboarding.vos.UserVO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/get-users")
    public List<UserVO> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/create-user")
    public ResponseEntity<String> createUser(@Valid @RequestBody UserVO user) {
        userService.addUser(user);
        return new ResponseEntity<>("New User Created!", HttpStatus.OK);
    }

    @PutMapping("/update-user")
    public ResponseEntity<String> updateUser(@Valid @RequestBody UserVO user) {
        userService.updateUser(user);
        return new ResponseEntity<>("User Updated!", HttpStatus.OK);
    }

    @DeleteMapping("/delete-user")
    public ResponseEntity<String> deleteUser(@Valid @RequestBody UserVO user) {
        userService.deleteUser(user);
        return new ResponseEntity<>("User Deleted!", HttpStatus.OK);
    }
}
