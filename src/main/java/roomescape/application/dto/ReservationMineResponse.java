package roomescape.application.dto;

public record ReservationMineResponse(Long reservationId, String theme, String date, String time, String status) {

  public static ReservationMineResponse of(Long id, String name, String date, String startAt, String status) {
    return new ReservationMineResponse(id, name, date, startAt, status);
  }
}
