package com.merchant.user_onboarding.service;

import com.merchant.user_onboarding.vos.UserVO;

import java.util.List;
import java.util.Optional;

public interface UserService {

    public List<UserVO> getAllUsers();
    public String addUser(UserVO user);
    public String updateUser(UserVO user, String id);
    public String deleteUser(String id);
    public List<UserVO> getUsers(Optional<String> keyword, Optional<Long> age, Optional<String> department, Optional<String> from, Optional<String> to);
    public byte[] saveUserDetails(Optional<String> keyword, Optional<Long> age, Optional<String> department, Optional<String> from, Optional<String> to);
}
