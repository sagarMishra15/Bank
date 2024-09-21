package com.app.Bank.controller;

import com.app.Bank.common.Constants;
import com.app.Bank.dao.Account.AccountRepository;
import com.app.Bank.dao.Account.AccountService;
import com.app.Bank.dao.user.UserRepository;
import com.app.Bank.dto.AccountDTO;
import com.app.Bank.dto.Filter.AccountDTOFilter;
import com.app.Bank.model.Account;
import com.app.Bank.model.User;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@CrossOrigin
@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;


    @Operation(summary = "Add Account | Admin & Bank Manager Access")
    @PostMapping("/add-account")
    public ResponseEntity<?> addAccount(@Valid @RequestBody AccountDTO body){
        User auth = GetAuth.getAuth();
        if(auth.getRole()==Constants.Role.ADMIN || auth.getRole()==Constants.Role.BANK_MANAGER) {
            Account account = new Account();
            User user = userRepository.findByUsername(body.getUsername());
            if(user!=null && user.getRole().equals(Constants.Role.CUSTOMER)){
                account.setUsername(body.getUsername());
                account.setAccNo(body.getAccNo());
                account.setIfsc(body.getIfsc());
                account.setBranch(body.getBranch());
                account.setTpin(body.getTpin());
                account.setStatus(body.getStatus());
                account.setUserId(user.getId());
                account.setCreatedAt(LocalDateTime.now());
//                account.setUpdatedAt(LocalDateTime.now());
                accountRepository.save(account);
                return ResponseEntity.ok("Account added successfully");
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Username not found or User is not a CUSTOMER.");
            }
        }
        return accountService.UnauthorizedAccess();
    }

    @Operation(summary = "Update Account | Admin & Bank Manager Access")
    @PutMapping("/update-account")
    public ResponseEntity<?> updateAccount(@Valid @RequestBody AccountDTO body){
        User auth = GetAuth.getAuth();
        if(auth.getRole()==Constants.Role.ADMIN || auth.getRole()==Constants.Role.BANK_MANAGER) {
            Account account = new Account();
            User user = userRepository.findByUsername(body.getUsername());
            if(user!=null){
                account.setAccNo(body.getAccNo());
                account.setIfsc(body.getIfsc());
                account.setBranch(body.getBranch());
                account.setTpin(body.getTpin());
                account.setStatus(body.getStatus());
                account.setUserId(user.getId());
                account.setUpdatedAt(LocalDateTime.now());
                return accountService.updateAccount(account);
            }
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        else{
            return accountService.UnauthorizedAccess();
        }
    }

    //Active/Inactive Account
    @Operation(summary = "Active/Inactive Account | Admin & Bank Manager Access")
    @PatchMapping("/{accountNumber}")
    public ResponseEntity<?> activeAndInactiveAccount(@PathVariable String accountNumber){
        User auth = GetAuth.getAuth();
        if(auth.getRole()==Constants.Role.ADMIN || auth.getRole()==Constants.Role.BANK_MANAGER){
            return accountService.activeAndInactiveAccount(accountNumber);
        }
        return accountService.UnauthorizedAccess();
    }

    @Operation(summary = "Delete Account | Admin & Bank Manager Access")
    @DeleteMapping("/{accountNumber}")
    public ResponseEntity<?> deleteAccount(@Valid @PathVariable String accountNumber){
        User auth = GetAuth.getAuth();
        if(auth.getRole()==Constants.Role.ADMIN) {
            return accountService.deleteAccount(accountNumber);
        }
        return accountService.UnauthorizedAccess();
    }

    @Operation(summary = "Get all accounts | Admin & BANK MANAGER Access")
    @GetMapping("/accounts")
    public ResponseEntity<?> getAllAccounts(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size){
        User auth=GetAuth.getAuth();
        if(auth.getRole()== Constants.Role.ADMIN || auth.getRole()== Constants.Role.BANK_MANAGER){
            Page<Account> accountsPage = accountService.getAllAccounts(page, size);
            return ResponseEntity.ok(accountsPage);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized Access. User must be ADMIN or BANK_MANAGER");
    }

    @Operation(summary = "Check Account Balance | BANK MANAGER & CUSTOMER Access")
    @PostMapping("/check-balance")
    public ResponseEntity<?> checkBalance(@RequestParam String accNo){
        User auth=GetAuth.getAuth();
        if(auth.getRole()== Constants.Role.BANK_MANAGER){
            return accountService.checkBalance(accNo);
        }
        else if(auth.getRole() == Constants.Role.CUSTOMER){
            int userId = auth.getId();
            Optional<Account> currUser = accountRepository.findAccountsByUserId(userId);
//            Optional<Account> userAccount = accountRepository.findByAccNo(currUser.get().getAccNo());
            if(currUser.isPresent() && currUser.get().getAccNo().equals(accNo)){
                return accountService.checkBalance(currUser.get().getAccNo());
            }else {
//                System.out.println(currUser.get().getAccNo()+" "+accNo);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Try again with your correct account number.");
            }
        }
        else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized Access. User must be CUSTOMER or BANK_MANAGER");
    }

    @GetMapping("/account-filter")
    public Page<Account> filterAccounts(@ParameterObject AccountDTOFilter param,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "5") int size
//                                        @RequestParam Map<String, Object> params
                                        ){
        return accountService.getFilteredAccounts(param.getUsername(), param.getAccNo(), param.getIfsc(), param.getBranch(),param.getStatus(), PageRequest.of(page,size));
    }
}