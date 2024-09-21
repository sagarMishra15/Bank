package com.app.Bank.dao.Account;

import com.app.Bank.common.Constants;
import com.app.Bank.model.Account;
import com.app.Bank.model.MyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService{

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public ResponseEntity<?> UnauthorizedAccess() {
        MyResponse<Account> rs = new MyResponse<>();
        rs.setData(null);
        rs.setMessage("Admin Access Only");
        rs.setStatusCode(HttpStatus.UNAUTHORIZED);
        ResponseEntity<MyResponse<Account>> re = new ResponseEntity<>(rs, HttpStatus.UNAUTHORIZED);
        return re;
    }
    @Override
    public ResponseEntity<MyResponse<Account>> updateAccount(Account account) {
        Optional<Account> existingAccount = accountRepository.findByAccNo(account.getAccNo());
        if(existingAccount.isPresent()){
            account.setId(existingAccount.get().getId());
            Account data = accountRepository.save(account);
            MyResponse<Account> rs = new MyResponse<>();
            rs.setData(existingAccount.get());
            rs.setMessage("Data Updated Successfully");
            rs.setStatusCode(HttpStatus.OK);
            ResponseEntity<MyResponse<Account>> re = new ResponseEntity<>(rs, HttpStatus.OK);
            return re;
        }
        else {
            MyResponse<Account> rs = new MyResponse<>();
            rs.setData(null);
            rs.setMessage("Account Not Found");
            rs.setStatusCode(HttpStatus.NOT_FOUND);
            ResponseEntity<MyResponse<Account>> re = new ResponseEntity<>(rs, HttpStatus.NOT_FOUND);
            return re;
        }
    }

    @Override
    public ResponseEntity<MyResponse<String>> deleteAccount(String accountNumber) {
        Optional<Account> id = accountRepository.findByAccNo(accountNumber);
        if(id.isPresent()){
            accountRepository.deleteById(id.get().getId());
            MyResponse<String> rs = new MyResponse<>();
            rs.setData(null);
            rs.setMessage("Data Deleted Successfully");
            rs.setStatusCode(HttpStatus.OK);
            ResponseEntity<MyResponse<String>> re = new ResponseEntity<>(rs, HttpStatus.OK);
            return re;
        } else {
            MyResponse<String> rs = new MyResponse<>();
            rs.setData(null);
            rs.setMessage("Data Not Found");
            rs.setStatusCode(HttpStatus.NOT_FOUND);
            ResponseEntity<MyResponse<String>> re = new ResponseEntity<>(rs, HttpStatus.NOT_FOUND);
            return re;
        }
    }

    @Override
    public ResponseEntity<?> activeAndInactiveAccount(String accNo) {
        Optional<Account> account = accountRepository.findByAccNo(accNo);
        if(account.isPresent()){
            if(account.get().getStatus().equals(Constants.Status.IN_ACTIVE)){
                account.get().setStatus(Constants.Status.ACTIVE);
                accountRepository.save(account.get());
                return ResponseEntity.status(HttpStatus.OK).body("Account Activated Successfully");
            }
            else {
                account.get().setStatus(Constants.Status.IN_ACTIVE);
                accountRepository.save(account.get());
                return ResponseEntity.status(HttpStatus.OK).body("Account In-Activated Successfully");
            }
        }
        else {
            MyResponse<String> rs = new MyResponse<>();
            rs.setData(null);
            rs.setMessage("Account Not Found");
            rs.setStatusCode(HttpStatus.NOT_FOUND);
            ResponseEntity<MyResponse<String>> re = new ResponseEntity<>(rs, HttpStatus.NOT_FOUND);
            return re;
        }
    }

    @Override
    public Page<Account> getAllAccounts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size); //PageRequest.of(page, size, Sort.by("id").descending());
        return accountRepository.findAll(pageable);
    }

    @Override
    public ResponseEntity<?> checkBalance(String accNo) {
        Optional<Account> user = accountRepository.findByAccNo(accNo);
        if (user.isPresent()){
            return ResponseEntity.ok("Account Balance: "+user.get().getBalance());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account Not Found");
    }

    @Override
    public Page<Account> getFilteredAccounts(String username, String accNo,
                                             String ifsc, Constants.Branch branch,
                                             Constants.Status status, Pageable pageable) {
        return accountRepository.findAll(AccountSpecification.getAccountsByFilter(username,accNo,ifsc,branch,status),pageable);
    }
}
