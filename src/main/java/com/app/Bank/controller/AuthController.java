package com.app.Bank.controller;

import com.app.Bank.common.Constants;
import com.app.Bank.dao.Account.AccountService;
import com.app.Bank.dao.user.UserService;
import com.app.Bank.dto.AuthenticationRequest;
import com.app.Bank.dto.AuthenticationResponse;
import com.app.Bank.dto.UserDTO;
import com.app.Bank.jwt_config.JwtUtil;
import com.app.Bank.model.User;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@CrossOrigin
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private JwtUtil jwtUtil;

    @Operation(summary = "User Registration")
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserDTO body) {
//        User auth = GetAuth.getAuth();
//        if(auth.getRole().equals(Constants.Role.ADMIN) ) {

            User user = new User();
            user.setName(body.getName());
            user.setUsername(body.getUsername());
            user.setPassword(passwordEncoder.encode(body.getPassword()));
            user.setPan(body.getPan());
            user.setAadhar(body.getAadhar());
            user.setMobile(body.getMobile());
            user.setDob(body.getDob());
            user.setAddress(body.getAddress());
            user.setRole(body.getRole());
            user.setBranch(body.getBranch());
            user.setGender(body.getGender());
            user.setCreatedAt(LocalDateTime.now());
            return userService.registerUser(user);
        }
//        return accountService.UnauthorizedAccess();
//    }

    @Operation(summary = "User Login")
    @PostMapping("/authentication")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest){
        try {
//            System.out.println("inside try==========");
            User currentUser = userService.findByUsername(authenticationRequest.getUsername());
//            System.out.println("database user: "+currentUser.getUsername()+" "+currentUser.getPassword());
//            System.out.println("current user: "+authenticationRequest.getUsername()+" "+ passwordEncoder.encode(authenticationRequest.getPassword()));
            if(authenticationRequest.getUsername().equals(currentUser.getUsername()) &&
                    passwordEncoder.matches(authenticationRequest.getPassword(),currentUser.getPassword())){
                final User userDetails = userService.findByUsername(authenticationRequest.getUsername());
                final String jwtToken = jwtUtil.generateToken(userDetails);
                final String role = String.valueOf(userDetails.getRole());
                return ResponseEntity.ok(new AuthenticationResponse(role, jwtToken));//"User: "+userDetails.getRole()                +"\nJWT Token: "+
            }else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Incorrect Username or Password");
            }
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Authentication failed, Incorrect Username or Password");
        }
    }
}
