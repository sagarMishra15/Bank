package com.app.Bank.dao.Account;

import com.app.Bank.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    public Optional<Account> findByAccNo(String accountNumber);

    @Query("SELECT a FROM Account a WHERE a.userId = :userId")
    public Optional<Account> findAccountsByUserId(@Param("userId") int userId);
//    public String findByUsername(String username);
//    public String findByPassword(String password);
//    public int findByTpin(int tpin);
}
