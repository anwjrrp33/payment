package com.payment.order.domain;

import com.payment.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long ticketId;

    private OrderStatus orderStatus;

    public Order(Long ticketId) {
        this.ticketId = ticketId;
        this.orderStatus = OrderStatus.ORDER;
    }
}
