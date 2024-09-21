package com.app.Bank.dto.Filter;

import com.app.Bank.common.Constants;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class UserDTOFilter {
    private String username;
    private String mobile;
    private Constants.Role role;

    public Map<String, Object> toMap(){
        Map<String, Object> map = new HashMap<>();
        map.put("username", this.username);
        map.put("mobile", this.mobile);
        map.put("role", this.role);
        return map;
    }
}
