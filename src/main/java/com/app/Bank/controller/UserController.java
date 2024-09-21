package com.app.Bank.controller;

import com.app.Bank.common.Constants;
import com.app.Bank.dao.Account.AccountService;
import com.app.Bank.dao.user.UserRepository;
import com.app.Bank.dao.user.UserService;
import com.app.Bank.dto.Filter.UserDTOFilter;
import com.app.Bank.dto.UserDTO;
import com.app.Bank.model.User;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@CrossOrigin
//@RequestMapping("/user")
public class UserController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private UserRepository userRepository;

    @Operation(summary = "Update User | Admin Access")
    @PutMapping("/user/update-user")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserDTO body){
        User auth = GetAuth.getAuth();
        if(auth.getRole()== Constants.Role.ADMIN) {
            User user = userRepository.findByUsername(body.getUsername());
//            System.out.println("ussssssssssser"+user.getUsername());
            if(user!=null) {
                user.setName(body.getName());
                user.setPassword(passwordEncoder.encode(body.getPassword()));
                user.setMobile(body.getMobile());
                user.setDob(body.getDob());
                user.setAddress(body.getAddress());
                user.setRole(body.getRole());
                user.setBranch(body.getBranch());
                user.setUpdatedAt(LocalDateTime.now());
                return userService.updateUser(user);
            }
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Username not found");
        }
        return accountService.UnauthorizedAccess();
    }

    @Operation(summary = "Get all users | Admin & BANK_MANAGER Access")
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size){
        User auth=GetAuth.getAuth();
        if(auth.getRole()== Constants.Role.ADMIN || auth.getRole()== Constants.Role.BANK_MANAGER){
            Page<User> usersPage = userService.getAllUsers(page, size);
            return ResponseEntity.ok(usersPage);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized Access. User must be ADMIN or BANK_MANAGER");
    }

    @Operation(summary = "Get filtered users | Admin, BANK_MANAGER & CUSTOMER Access")
    @GetMapping("/user-filter")
    public Page<User> filterUsers(
            @ParameterObject UserDTOFilter params,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return userService.getFilteredUsers(params.getUsername(), params.getMobile(), params.getRole(), PageRequest.of(page, size));
    }
}
