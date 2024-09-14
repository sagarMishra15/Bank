package com.app.Bank.model;

import com.app.Bank.common.Constants.*;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private int accId; //acc id of person who is sending
    @Column(nullable = false)
    private int userId; //user id of person who is sending
    @Column(nullable = false)
    private String beneAccNo;
    @Column(nullable = false)
    private String beneIfsc;
    @Column(nullable = false)
    private double amount;
    private String beneName;
    private String remarks;
    private double charge;
    @Enumerated(EnumType.STRING)
    private Mop mop; //Mode of Payment
//    private String latlong; //check
    @Enumerated(EnumType.ORDINAL)
    private TxnStatus status;
    @CreationTimestamp
    private LocalDateTime createdAt;
}
