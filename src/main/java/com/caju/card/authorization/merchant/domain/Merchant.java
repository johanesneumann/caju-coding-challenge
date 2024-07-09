package com.caju.card.authorization.merchant.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "merchants")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor()
@Getter
@Setter
public class Merchant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "location")
    private String location;
    @Column(name = "transaction_name")
    private String transactionName;
    @Column(name = "mcc_code")
    private String mcc;

}
