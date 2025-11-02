package com.merchant.user_onboarding.controller;

import com.merchant.user_onboarding.service.UserService;
import com.merchant.user_onboarding.vos.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@Validated
@Tag(name = "User", description = "API for managing Users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/get-users")
    @Operation(summary = "Get Users")
    public List<UserVO> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/create-user")
    @Operation(summary = "Create a new User")
    public ResponseEntity<String> createUser(@Valid @RequestBody UserVO user) {
        userService.addUser(user);
        return new ResponseEntity<>("New User Created!", HttpStatus.OK);
    }

    @PutMapping("/update-user/{id}")
    @Operation(summary = "Update a User")
    public ResponseEntity<String> updateUser(@Valid @RequestBody UserVO user, @PathVariable String id) {
        userService.updateUser(user, id);
        return new ResponseEntity<>("User Updated!", HttpStatus.OK);
    }

    @DeleteMapping("/delete-user/{id}")
    @Operation(summary = "Delete a User")
    public ResponseEntity<String> deleteUser(@Valid @PathVariable String id) {
        userService.deleteUser(id);
        return new ResponseEntity<>("User Deleted!", HttpStatus.OK);
    }

    @GetMapping("/find-user")
    @Operation(summary = "Get filtered Users")
    public ResponseEntity<?> getUser(@RequestParam(required = false, defaultValue = "")Optional<String>searchParam,
                                     @RequestParam(required=false) Optional<Long> ageParam,
                                     @RequestParam(required = false) Optional<String> departmentParam,
                                     @RequestParam(required = false) Optional<String> fromParam,
                                     @RequestParam(required = false) Optional<String> toParam) {

        return new ResponseEntity<>(userService.getUsers(searchParam, ageParam, departmentParam, fromParam, toParam), HttpStatus.OK);
    }

    @GetMapping("/export-excel")
    @Operation(summary = "Export user details as excel file")
    public ResponseEntity<?> saveUserDetails(@RequestParam(required = false, defaultValue = "")Optional<String>searchParam,
                                             @RequestParam(required=false) Optional<Long> ageParam,
                                             @RequestParam(required = false) Optional<String> departmentParam,
                                             @RequestParam(required = false) Optional<String> fromParam,
                                             @RequestParam(required = false) Optional<String> toParam) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "users.xlsx");

        byte[] excelData = userService.saveUserDetails(searchParam, ageParam, departmentParam, fromParam, toParam);

        return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
    }

}
