package com.merchant.user_onboarding.service.serviceImpl;

import com.merchant.user_onboarding.model.UserEntity;
import com.merchant.user_onboarding.repository.UserRepository;
import com.merchant.user_onboarding.service.UserService;
import com.merchant.user_onboarding.vos.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<UserVO> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();


        List<UserVO> usersVO = users.stream()
                .map(user -> new UserVO(user.getUserId(), user.getUserName(),
                        user.getAge(), user.getDepartment(), user.getDateOfBirth().toString(),
                        user.getRegisteredDate().toString(), user.getLastUpdated().toString()))
                .collect(Collectors.toList());

        return usersVO;
    }

    @Override
    public String addUser(UserVO user) {
        return "";
    }

    @Override
    public String updateUser(UserVO user) {
        return "";
    }

    @Override
    public String deleteUser(UserVO user) {
        return "";
    }
}
