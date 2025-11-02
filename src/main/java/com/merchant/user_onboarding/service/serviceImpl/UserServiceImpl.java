package com.merchant.user_onboarding.service.serviceImpl;

import com.merchant.user_onboarding.exceptions.AgeNotValidException;
import com.merchant.user_onboarding.exceptions.DateNotValidException;
import com.merchant.user_onboarding.exceptions.UserAlreadyExistsException;
import com.merchant.user_onboarding.exceptions.UserNotFoundException;
import com.merchant.user_onboarding.model.UserEntity;
import com.merchant.user_onboarding.repository.UserRepository;
import com.merchant.user_onboarding.service.UserService;
import com.merchant.user_onboarding.vos.UserVO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
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
        if(userRepository.existsByUserId(user.getUserId())) {
            throw new UserAlreadyExistsException("User with id: " + user.getUserId() + " already exists.");
        }

        UserEntity newUser = new UserEntity();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withResolverStyle(ResolverStyle.SMART);

        try {
            LocalDate date = LocalDate.parse(user.getDateOfBirth(), formatter);
            if(date.isAfter(LocalDate.now())) {
                throw new DateNotValidException("Date of Birth should be in the past");
            }
            if(date.isBefore(LocalDate.parse("1900-01-01"))) {
                throw new DateNotValidException("Date of Birth should be after 1900-01-01");
            }

        } catch(DateTimeParseException ex) {
            throw new DateNotValidException("Enter a valid Date of Birth in yyyy-MM-dd format");
        }

        try {
            LocalDate date = LocalDate.parse(user.getDateOfBirth());
            Period age = Period.between(date, LocalDate.now());
            Integer calculatedAge =  age.getYears();

            if(calculatedAge != user.getAge().intValue()) {
                throw new AgeNotValidException("DOB and age does not match");
            }

        } catch (AgeNotValidException ex) {
            throw new AgeNotValidException("Incorrect Age");
        }

        newUser.setUserId(user.getUserId());
        newUser.setUserName(user.getUserName());
        newUser.setAge(user.getAge());
        newUser.setDepartment(user.getDepartment());
        newUser.setDateOfBirth(LocalDate.parse(user.getDateOfBirth(), formatter));
        newUser.setRegisteredDate(LocalDateTime.now());
        newUser.setLastUpdated(LocalDateTime.now());


        userRepository.save(newUser);

        return "Success";
    }

    @Override
    public String updateUser(UserVO user, String id) {
        if(userRepository.existsByUserId(id)) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withResolverStyle(ResolverStyle.SMART);
            try {
                LocalDate date = LocalDate.parse(user.getDateOfBirth(), formatter);
                if(date.isAfter(LocalDate.now())) {
                    throw new DateNotValidException("Date of Birth should be in the past");
                }
                if(date.isBefore(LocalDate.parse("1900-01-01"))) {
                    throw new DateNotValidException("Date of Birth should be after 1900-01-01");
                }

            } catch(DateTimeParseException ex) {
                throw new DateNotValidException("Enter a valid Date of Birth");
            }

            try {
                LocalDate date = LocalDate.parse(user.getDateOfBirth());
                Period age = Period.between(date, LocalDate.now());
                Integer calculatedAge =  age.getYears();

                if(calculatedAge != user.getAge().intValue()) {
                    throw new AgeNotValidException("DOB and age does not match");
                }

            } catch (AgeNotValidException ex) {
                throw new AgeNotValidException("Incorrect Age");
            }

            UserEntity existingUser = userRepository.findByUserId(id);

            existingUser.setUserName(user.getUserName());
            existingUser.setAge(user.getAge());
            existingUser.setDepartment(user.getDepartment());
            existingUser.setDateOfBirth(LocalDate.parse(user.getDateOfBirth(), formatter));
            existingUser.setLastUpdated(LocalDateTime.now());

            userRepository.save(existingUser);

            return "Updated";
        } else {
            throw new UserNotFoundException("No user found!");
        }
    }

    @Override
    public String deleteUser(String id) {
        if(userRepository.existsByUserId(id)) {
            UserEntity existingUser = userRepository.findByUserId(id);
            userRepository.delete(existingUser);;

            return "Deleted";
        } else {
            throw new UserNotFoundException("No user found!");
        }
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


    @Override
    public byte[] saveUserDetails(Optional<String> keyword, Optional<Long> age, Optional<String> dapartment, Optional<String> from, Optional<String> to) {

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



            return generateExcelFile(usersVO);
        } else {
            throw new UserNotFoundException("No user found!");
        }

    }


    private byte[] generateExcelFile(List<UserVO> data) {

        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Data Sheet");

            Row headerRow = sheet.createRow(0);
            String[] headers = {"User Id", "User Name", "Age", "Department", "DOB", "Registered Dae", "Updated Date"};
            for(int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            int rowNum = 1;
            for(UserVO item : data) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(item.getUserId());
                row.createCell(1).setCellValue(item.getUserName());
                row.createCell(2).setCellValue(item.getAge());
                row.createCell(3).setCellValue(item.getDepartment());
                row.createCell(4).setCellValue(item.getDateOfBirth());
                row.createCell(5).setCellValue(item.getRegisteredDate());
                row.createCell(6).setCellValue(item.getUpdatedDate());
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close();
            return outputStream.toByteArray();
        } catch (IOException ex) {
            System.err.println("Error while writing to file");
        }
        return null;
    }

}
