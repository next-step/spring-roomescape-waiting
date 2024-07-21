package roomescape.waiting.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;
import roomescape.user.domain.User;

class ReservationWaitingTest {

    @Test
    void 예약_대기자가_아니면_예외가_발생한다() {
        // given
        User waitingUser = new User(1L, "name", "email", "password", "USER");
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.now());
        Theme theme = new Theme(1L, "레벨1 탈출", "우테코 레벨1을 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        ReservationWaiting reservationWaiting = new ReservationWaiting(LocalDate.now(), waitingUser, reservationTime, theme);
        User user = new User(2L, "name", "email", "password", "USER");

        // when & then
        assertThatThrownBy(() -> reservationWaiting.validateWaitingBy(user))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 대기자가 아닙니다.");
    }
}
