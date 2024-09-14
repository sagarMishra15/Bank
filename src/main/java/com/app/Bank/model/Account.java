package com.app.Bank.model;

import com.app.Bank.common.Constants.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.time.LocalDateTime;

@Data
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    @Column(nullable = false)
    private double balance;
    @Column(nullable = false)
    private double holdBalance;
    @Column(unique = true)
    private String accNo;
    private String ifsc;
    private int tpin;
    @Enumerated(EnumType.STRING)
    private Branch branch;
    @Enumerated(EnumType.ORDINAL)
    private Status status;
    private int userId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime updatedAt;
}
