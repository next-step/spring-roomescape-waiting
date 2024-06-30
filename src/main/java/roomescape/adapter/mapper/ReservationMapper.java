package roomescape.adapter.mapper;

import roomescape.adapter.out.ReservationEntity;
import roomescape.application.dto.ReservationCommand;
import roomescape.application.dto.ReservationMineResponse;
import roomescape.application.dto.ReservationResponse;
import roomescape.domain.Reservation;

public class ReservationMapper {

  private ReservationMapper() {
    throw new UnsupportedOperationException("생성 불가");
  }

  public static ReservationResponse mapToResponse(Reservation reservation) {
    return ReservationResponse.of(
      reservation.getId(),
      reservation.getMember()
                 .getEmail(),
      reservation.getDate(),
      reservation.getTime()
                 .getStartAt());
  }

  public static Reservation mapToDomain(ReservationCommand reservationCommand) {
    return Reservation.of(null, reservationCommand.date(), null, null, null);
  }

  public static Reservation mapToDomain(ReservationEntity reservationEntity) {
    return Reservation.of(reservationEntity.getId(), reservationEntity.getDate(),
      ReservationTimeMapper.mapToDomain(reservationEntity.getReservationTime()),
      ThemeMapper.mapToDomain(reservationEntity.getTheme()), MemberMapper.mapToDomain(reservationEntity.getMember()));
  }

  public static ReservationEntity mapToEntity(Reservation reservation) {
    return ReservationEntity.of(reservation.getId(), reservation.getDate(),
      ReservationTimeMapper.mapToEntity(reservation.getTime()), ThemeMapper.mapToEntity(reservation.getTheme()),
      MemberMapper.mapToEntity(reservation.getMember()));
  }

  public static ReservationMineResponse mapToMineResponse(Reservation reservation) {
    return ReservationMineResponse.of(reservation.getId(), reservation.getTheme()
                                                                      .getName(), reservation.getDate(),
      reservation.getTime()
                 .getStartAt(), "예약");
  }
}
