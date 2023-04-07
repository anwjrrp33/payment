package com.payment.ticket.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private Stock stock;

    public Ticket(String title, Stock stock) {
        Assert.hasText(title, "title은 empty가 될 수 없습니다");
        this.title = title;
        this.stock = stock;
    }

    public void decrease() {
        this.stock = this.stock.decrease();
    }
}
