package com.app.Bank.dao.user;

import com.app.Bank.common.Constants;
import com.app.Bank.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;
    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public ResponseEntity<String> registerUser(User user) {

//        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        return ResponseEntity.ok("User Registered Successfully");
    }

    @Override
    public ResponseEntity<?> updateUser(User user) {
        userRepository.save(user);
        return ResponseEntity.ok("User Updated Successfully");
    }

    @Override
    public Page<User> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return userRepository.findAll(pageable);
    }

    @Override
    public Page<User> getFilteredUsers(String username, String mobile, Constants.Role role, Pageable pageable) {
        return userRepository.findAll(UserSpecification.getUsersByFilter(username, mobile, role), pageable);
    }
}
