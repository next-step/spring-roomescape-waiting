package roomescape.reservationtime.dto;

import roomescape.reservationtime.ReservationTime;

public class AvailableTimeResponse {

    private Long id;

    private String startAt;

    private boolean alreadyBooked;

    private AvailableTimeResponse(Long id, String startAt, boolean alreadyBooked) {
        this.id = id;
        this.startAt = startAt;
        this.alreadyBooked = alreadyBooked;
    }

    public static AvailableTimeResponse from(ReservationTime reservationTime, boolean alreadyBooked) {
        return new AvailableTimeResponse(reservationTime.getId(),
            reservationTime.getStartAt().toString(), alreadyBooked);
    }

    public Long getId() {
        return id;
    }

    public String getStartAt() {
        return startAt;
    }

    public boolean isAlreadyBooked() {
        return alreadyBooked;
    }
}
