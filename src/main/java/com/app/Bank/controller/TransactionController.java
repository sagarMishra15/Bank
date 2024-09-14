package com.app.Bank.controller;

import com.app.Bank.common.Constants;
import com.app.Bank.dao.Account.AccountRepository;
import com.app.Bank.dao.transaction.TransactionRepository;
import com.app.Bank.dto.TransactionDTO;
import com.app.Bank.model.Account;
import com.app.Bank.model.Transaction;
import com.app.Bank.model.User;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
//@RequestMapping("/transaction")
public class TransactionController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @Operation(summary = "Transaction | Customer Access")
    @PostMapping("/transaction")
    public ResponseEntity<?> transaction(@Valid @RequestBody TransactionDTO body){
        User auth = GetAuth.getAuth();
        if(auth.getRole() == Constants.Role.CUSTOMER){
            Optional<Account> account = accountRepository.findByAccNo(body.getBeneAccNo());
//            System.out.println("--------"+account.get().getUsername()+":"+account.get().getBalance()+"-----------");
            if(account.isEmpty()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Beneficiary Account Not Found");
            }
            if(!account.get().getIfsc().equals(body.getBeneIfsc()))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Beneficiary IFSC Not Found");

            if(account.get().getStatus()==(Constants.Status.IN_ACTIVE))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account is In-Active");
//            System.out.println("-------"+auth.getId()+"-------");
            Optional<Account> currUser = accountRepository.findAccountsByUserId(auth.getId());
            if (currUser.isPresent()) {
                if (body.getTpin()!=(currUser.get().getTpin())) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect TPIN");
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
            }

            double currUserBal = currUser.get().getBalance();
            double beneUserBal = account.get().getBalance();
            double charges = 0.1*body.getAmount();

            Transaction transaction = new Transaction();
            transaction.setAccId(currUser.get().getId());
            transaction.setUserId(currUser.get().getUserId());
            transaction.setBeneAccNo(body.getBeneAccNo());
            transaction.setBeneIfsc(body.getBeneIfsc());
            transaction.setAmount(body.getAmount());
            transaction.setBeneName(body.getBeneName());
            transaction.setRemarks(body.getRemarks());
            transaction.setMop(body.getMop());
            //after minus, in currUserBal: currUserBal - (body.getAmt + charges)
            if (currUser.isPresent()) {
//                System.out.println("--------"+currUser.get().getUsername()+":"+currUser.get().getBalance()+"-----------");
                currUser.get().setBalance(currUserBal - (body.getAmount() + charges));
            }
            //set in bene acc: beneUserBal + body.getAmt
            if (account.isPresent())
                account.get().setBalance(beneUserBal+body.getAmount());

            transaction.setStatus(Constants.TxnStatus.SUCCESS);
            transaction.setCharge(charges);

            accountRepository.save(account.get());
            accountRepository.save(currUser.get());
            transactionRepository.save(transaction);
            return ResponseEntity.status(HttpStatus.OK).body("Transaction Completed Successfully");
        }else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized Access, User must be a CUSTOMER");
        }
    }
}
