package com.study.springboot.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
public class Delivery {
    @Id @GeneratedValue
    @Column(name = "DELIVERY_ID")
    private Long id;
    
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;
    
    @Embedded
    private Address address;
    
    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;
}
