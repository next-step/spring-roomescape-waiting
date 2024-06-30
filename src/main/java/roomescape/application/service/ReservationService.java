package roomescape.application.service;

import static roomescape.adapter.mapper.ReservationMapper.mapToDomain;
import static roomescape.adapter.mapper.ReservationMapper.mapToResponse;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.adapter.mapper.ReservationMapper;
import roomescape.application.dto.AdminReservationCommand;
import roomescape.application.dto.MemberInfo;
import roomescape.application.dto.ReservationCommand;
import roomescape.application.dto.ReservationMineResponse;
import roomescape.application.dto.ReservationResponse;
import roomescape.application.port.in.ReservationUseCase;
import roomescape.application.port.out.MemberPort;
import roomescape.application.port.out.ReservationPort;
import roomescape.application.port.out.ReservationTimePort;
import roomescape.application.port.out.ThemePort;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.NotFoundReservationException;
import roomescape.exception.ReservationTimeConflictException;
import roomescape.validator.DateTimeValidator;

@Transactional
@Service
public class ReservationService implements ReservationUseCase {

    private final ReservationPort reservationPort;
    private final ReservationTimePort reservationTimePort;
    private final ThemePort themePort;
    private final MemberPort memberPort;

    public ReservationService(ReservationPort reservationPort, ReservationTimePort reservationTimePort,
      ThemePort themePort,
      MemberPort memberPort) {
        this.reservationPort = reservationPort;
        this.reservationTimePort = reservationTimePort;
        this.themePort = themePort;
        this.memberPort = memberPort;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ReservationResponse> retrieveReservations() {
        return reservationPort.findReservations()
                              .stream()
                              .map(ReservationMapper::mapToResponse)
                              .toList();
    }

    @Override
    public ReservationResponse registerReservation(ReservationCommand reservationCommand) {
        ReservationTime reservationTime = reservationTimePort.findReservationTimeById(reservationCommand.timeId())
                                                             .orElseThrow(NotFoundReservationException::new);

        DateTimeValidator.previousDateTimeCheck(reservationCommand.date(), reservationTime.getStartAt());

        if (reservationPort.findReservationByReservationTime(reservationTime)
                           .isPresent()) {
            throw new ReservationTimeConflictException();
        }

        Theme theme = themePort.findThemeById(reservationCommand.themeId())
                               .orElseThrow(NotFoundReservationException::new);

        Member member = memberPort.findMemberByEmail(reservationCommand.name())
                                  .orElseThrow(NotFoundReservationException::new);

        Reservation reservation = mapToDomain(reservationCommand);

        reservation = reservation.addReservationTimeAndTheme(reservationTime, theme, member);

        return mapToResponse(reservationPort.saveReservation(reservation));
    }

    @Override
    public void deleteReservation(Long id) {
        if (reservationPort.countReservationById(id) == 0) {
            throw new NotFoundReservationException();
        }

        reservationPort.deleteReservation(id);
    }

    @Override
    public void registerAdminReservation(AdminReservationCommand adminReservationCommand, MemberInfo memberInfo) {
        ReservationTime reservationTime = reservationTimePort.findReservationTimeById(adminReservationCommand.timeId())
                                                             .orElseThrow(NotFoundReservationException::new);
        Theme theme = themePort.findThemeById(adminReservationCommand.themeId())
                               .orElseThrow(NotFoundReservationException::new);

        DateTimeValidator.previousDateTimeCheck(adminReservationCommand.date(), reservationTime.getStartAt());

        if (reservationPort.findReservationByReservationTime(reservationTime)
                           .isPresent()) {
            throw new ReservationTimeConflictException();
        }

        Member member = memberPort.findMemberByEmail(memberInfo.email())
                                  .orElseThrow(NotFoundReservationException::new);

        Reservation reservation = Reservation.of(null, adminReservationCommand.date(),
            reservationTime, theme, member);
        reservationPort.saveReservation(reservation);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ReservationMineResponse> retrieveMyReservations(Long memberId) {
        return reservationPort.findReservationsByMemberId(memberId)
                              .stream()
                              .map(ReservationMapper::mapToMineResponse)
                              .toList();
    }
}
