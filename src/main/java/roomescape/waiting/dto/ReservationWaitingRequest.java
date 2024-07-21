package roomescape.waiting.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;
import roomescape.user.domain.User;
import roomescape.waiting.domain.ReservationWaiting;

public record ReservationWaitingRequest(@NotNull(message = "날짜는 필수 입력 값입니다.")
                                        LocalDate date,
                                        @NotNull(message = "테마 ID는 필수 입력 값입니다.")
                                        Long theme,
                                        @NotNull(message = "시간 ID는 필수 입력 값입니다.")
                                        Long time) {

    public ReservationWaiting toReservationWaiting(User waitingUser, ReservationTime reservationTime, Theme theme) {
        return new ReservationWaiting(date, waitingUser, reservationTime, theme);
    }
}
