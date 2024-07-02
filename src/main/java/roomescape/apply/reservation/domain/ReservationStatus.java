package roomescape.apply.reservation.domain;

public enum ReservationStatus {
    WAITING("예약 대기"),
    RESERVED("예약"),
    CANCELED("예약 취소");

    private final String value;

    ReservationStatus(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
