package com.payment.ticket.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StockTest {

    @DisplayName("재고가 0미만이면 감소하면 에러가 발생한다.")
    @Test
    void _1() {
        Stock stock = new Stock(0);

        assertThatThrownBy(() -> stock.decrease())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("재고가 0미만으로 생성하면 에러가 발생한다.")
    @Test
    void _2() {
        assertThatThrownBy(() -> new Stock(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }
}