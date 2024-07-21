package roomescape.waiting.dto;

import roomescape.waiting.domain.ReservationWaiting;

public record ReservationWaitingResponse(Long id, String theme, String date, String time, String status) {

    public static ReservationWaitingResponse from(ReservationWaiting reservationWaiting) {
        return new ReservationWaitingResponse(
                reservationWaiting.getId(),
                reservationWaiting.getTheme().getName(),
                reservationWaiting.getDate().toString(),
                reservationWaiting.getTime().getStartAt().toString(),
                "예약대기"
        );
    }
}
