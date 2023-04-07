package com.payment.ticket.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TicketTest {

    @DisplayName("티켓 감소가 정상 동작한다.")
    @ParameterizedTest
    @CsvSource(value = { "1:0", "2:1", "3:2", "4:3", "5:4" }, delimiter = ':')
    void _1(int stock, int expectStock) {
        Ticket ticket = new Ticket("normalTitle", new Stock(stock));

        ticket.decrease();

        assertThat(ticket.getStock()).isEqualTo(new Stock(expectStock));
    }

    @DisplayName("티켓 감소가 0미만이면 에러가 발생한다.")
    @Test
    void _2() {
        Ticket emptyTicket = new Ticket("normalTitle", new Stock(0));

        assertThatThrownBy(() -> emptyTicket.decrease())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("제목이 빈 값이면 에러가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void _3(String invalidTitle) {
        assertThatThrownBy(() -> new Ticket(invalidTitle, new Stock(1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("제목은 빈 값이 될 수 없습니다");
    }
}