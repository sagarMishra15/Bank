package com.app.Bank.dto.Filter;

import com.app.Bank.common.Constants;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class AccountDTOFilter {
    String username;
    String accNo;
    String ifsc;
    Constants.Branch branch;
    Constants.Status status;

    public Map<String, Object> toMap(){
        Map<String, Object> map=new HashMap<>();
        map.put("username",username);
        map.put("accNo",accNo);
        map.put("ifsc",ifsc);
        map.put("branch",branch);
        map.put("status",status);
        return map;
    }
}
