package com.payment.ticket.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Stock {

    private int stock;

    public Stock(int stock) {
        validate(stock);
        this.stock = stock;
    }

    public void validate(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException();
        }
    }

    public Stock decrease() {
        return new Stock(this.stock - 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock1 = (Stock) o;
        return stock == stock1.stock;
    }

    @Override
    public int hashCode() {
        return Objects.hash(stock);
    }
}
