package com.app.Bank.dao.Account;

import com.app.Bank.common.Constants;
import com.app.Bank.model.Account;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;


public class AccountSpecification {
    public static Specification<Account> getAccountsByFilter(String username, String accNo, String ifsc, Constants.Branch branch,
                                                             Constants.Status status) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(username!=null && !username.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("username"), "%" + username + "%"));
            }

            if(accNo!=null && !accNo.isEmpty()){
                predicates.add(criteriaBuilder.like(root.get("accNo"), "%" + accNo + "%"));
            }

            if (ifsc!=null && !ifsc.isEmpty()){
                predicates.add(criteriaBuilder.like(root.get("ifsc"), "%" + ifsc + "%"));
            }

            if(branch!=null && !branch.equals("")){
                predicates.add(criteriaBuilder.like(root.get("branch"), "%" + branch + "%"));
            }

            if(status!=null && !status.equals("")){
                predicates.add(criteriaBuilder.like(root.get("status"), "%" + status + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
