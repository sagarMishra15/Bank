package com.app.Bank.model;

import com.app.Bank.common.Constants.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDateTime;

@Entity
@Table(name = "\"user\"")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
    private String pan;
    private String aadhar;
    private String mobile;
    private String address;
    private String gender;
    private Date dob;

    @Enumerated(EnumType.ORDINAL)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Branch branch;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime updatedAt;
}
