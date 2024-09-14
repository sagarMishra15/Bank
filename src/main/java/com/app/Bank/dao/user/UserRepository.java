package com.app.Bank.dao.user;

import com.app.Bank.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    public User findById(int id);

    public User findByUsername(String username);
}
