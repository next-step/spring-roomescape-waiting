package roomescape.reservationtime.dto;

import roomescape.reservationtime.ReservationTime;

public class ReservationTimeResponse {

    private Long id;

    private String startAt;

    private ReservationTimeResponse(Long id, String startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public static ReservationTimeResponse of(ReservationTime reservationTime) {
        return new ReservationTimeResponse(reservationTime.getId(),
            reservationTime.getStartAt().toString());
    }

    public Long getId() {
        return id;
    }

    public String getStartAt() {
        return startAt;
    }
}
