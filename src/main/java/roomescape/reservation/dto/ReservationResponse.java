package roomescape.reservation.dto;

import roomescape.reservation.Reservation;

public class ReservationResponse {

    private Long id;

    private String name;

    private String date;

    private String time;

    private String themeName;

    private ReservationResponse(Long id, String name, String date, String time, String themeName) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.themeName = themeName;
    }

    public static ReservationResponse of(Reservation reservation) {
        return new ReservationResponse(reservation.getId(),
            reservation.getMember().getName(),
            reservation.getDate().toString(),
            reservation.getReservationTime().getStartAt().toString(),
            reservation.getTheme().getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getThemeName() {
        return themeName;
    }
}
