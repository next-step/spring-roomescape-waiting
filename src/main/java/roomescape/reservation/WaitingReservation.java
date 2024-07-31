package roomescape.reservation;

public class WaitingReservation {

    private Reservation reservation;

    private Long rank;

    public WaitingReservation(Reservation reservation, Long rank) {
        this.reservation = reservation;
        this.rank = rank;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public Long getRank() {
        return rank;
    }
}
