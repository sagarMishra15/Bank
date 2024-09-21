package com.app.Bank.dao.user;

import com.app.Bank.common.Constants;
import com.app.Bank.dto.UserDTO;
import com.app.Bank.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    public User findByUsername(String username);

    public ResponseEntity<String> registerUser(User user);

    public ResponseEntity<?> updateUser(User user);

    Page<User> getAllUsers(int page, int size);

    Page<User> getFilteredUsers(String username, String mobile, Constants.Role role, Pageable pageable);
}
