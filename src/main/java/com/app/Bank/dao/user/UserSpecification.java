package com.app.Bank.dao.user;

import com.app.Bank.common.Constants;
import com.app.Bank.model.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class UserSpecification {
    public static Specification<User> getUsersByFilter(String username, String mobile,Constants.Role role) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (username != null && !username.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("username"), "%" + username + "%"));
            }

            if(mobile != null && !mobile.isEmpty()){
                predicates.add(criteriaBuilder.like(root.get("mobile"),"%"+mobile+"%"));
            }

            if (role != null && !role.equals("")) {
                predicates.add(criteriaBuilder.equal(root.get("role"), role));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
