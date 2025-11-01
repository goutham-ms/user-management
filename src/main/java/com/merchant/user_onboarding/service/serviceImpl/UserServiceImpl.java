package com.merchant.user_onboarding.service.serviceImpl;

import com.merchant.user_onboarding.exceptions.DateNotValidException;
import com.merchant.user_onboarding.exceptions.UserNotFoundException;
import com.merchant.user_onboarding.model.UserEntity;
import com.merchant.user_onboarding.repository.UserRepository;
import com.merchant.user_onboarding.service.UserService;
import com.merchant.user_onboarding.vos.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
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

        UserEntity newUser = new UserEntity();

        newUser.setUserId(user.getUserId());
        newUser.setUserName(user.getUserName());
        newUser.setAge(user.getAge());
        newUser.setDepartment(user.getDepartment());
        newUser.setDateOfBirth(LocalDate.parse(user.getDateOfBirth()));
        newUser.setRegisteredDate(LocalDateTime.now());
        newUser.setLastUpdated(LocalDateTime.now());


        userRepository.save(newUser);

        return "Success";
    }

    @Override
    public String updateUser(UserVO user) {
        if(userRepository.existsByUserId(user.getUserId())) {

            UserEntity existingUser = userRepository.findByUserId(user.getUserId());

            existingUser.setUserName(user.getUserName());
            existingUser.setAge(user.getAge());
            existingUser.setDepartment(user.getDepartment());
            existingUser.setDateOfBirth(LocalDate.parse(user.getDateOfBirth()));
            existingUser.setLastUpdated(LocalDateTime.now());

            userRepository.save(existingUser);

            return "Updated";
        }
        return "Error";
    }

    @Override
    public String deleteUser(UserVO user) {
        if(userRepository.existsByUserId(user.getUserId())) {
            UserEntity existingUser = userRepository.findByUserId(user.getUserId());
            userRepository.delete(existingUser);;

            return "Deleted";
        }
        return "Error";
    }

    @Override
    public List<UserVO> getUsers(Optional<String> keyword, Optional<Long> age, Optional<String> dapartment, Optional<String> from, Optional<String> to) {

        if(from.isPresent()) {
            if(to.isPresent()) {
                try {
                    LocalDate fromDate = LocalDate.parse(from.get());
                    LocalDate toDate = LocalDate.parse(to.get());
                    if(toDate.isBefore(fromDate)) {
                        throw new DateNotValidException("From-date should be before To-date");
                    }
                }catch(DateTimeParseException ex) {
                    throw new DateNotValidException("Enter a valid Date!");
                }


            } else {
                throw new DateNotValidException("To Date is also required");
            }
        }

        List<UserEntity> users = userRepository.findUsersByCriteria(keyword, age, dapartment, from, to);

        if(!users.isEmpty()) {

            List<UserVO> usersVO = users.stream()
                    .map(user -> new UserVO(user.getUserId(), user.getUserName(),
                            user.getAge(), user.getDepartment(), user.getDateOfBirth().toString(),
                            user.getRegisteredDate().toString(), user.getLastUpdated().toString()))
                    .collect(Collectors.toList());
            return usersVO;
        } else {
            throw new UserNotFoundException("No user found!");
        }

    }
}
