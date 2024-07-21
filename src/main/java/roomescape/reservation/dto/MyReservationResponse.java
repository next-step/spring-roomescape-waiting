package roomescape.reservation.dto;

import roomescape.reservation.domain.Reservation;
import roomescape.waiting.domain.ReservationWaiting;

public record MyReservationResponse(Long id, String theme, String date, String time, String status) {

    public static MyReservationResponse from(Reservation reservation) {
        return new MyReservationResponse(
                reservation.getId(),
                reservation.getTheme().getName(),
                reservation.getDate().toString(),
                reservation.getTime().getStartAt().toString(),
                "예약"
        );
    }

    public static MyReservationResponse from(ReservationWaiting reservationWaiting) {
        return new MyReservationResponse(
                reservationWaiting.getId(),
                reservationWaiting.getTheme().getName(),
                reservationWaiting.getDate().toString(),
                reservationWaiting.getTime().getStartAt().toString(),
                "예약대기"
        );
    }
}
