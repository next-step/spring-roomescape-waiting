package roomescape.test.application;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.error.exception.ReservationAlreadyExistsException;
import roomescape.member.Member;
import roomescape.member.MemberRole;
import roomescape.member.repository.MemberRepository;
import roomescape.reservation.Reservation;
import roomescape.reservation.ReservationStatus;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.service.ReservationService;
import roomescape.reservationtime.ReservationTime;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.theme.Theme;
import roomescape.theme.repository.ThemeRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    @Mock
    private ThemeRepository themeRepository;

    @Mock
    private MemberRepository memberRepository;
    private ReservationService reservationService;

    private final Long memberId = 1L;
    private final String memberName = "member";
    private final Long timeId = 1L;
    private final String time = "10:00";
    private final Long themeId = 1L;
    private final String themeName = "theme";
    private final Long reservationId = 1L;

    private final Member member = new Member(memberId, "email", "password", memberName, MemberRole.MEMBER);
    private final ReservationTime reservationTime = new ReservationTime(timeId, LocalTime.parse(time));
    private final Theme theme = new Theme(themeId, themeName, "description", "thumbnail");
    private final LocalDate reservationDate = LocalDate.now().plusDays(1);
    private final String reservationDateString = reservationDate.toString();
    private final Reservation reservation = new Reservation(member, reservationDateString,
        reservationTime, theme, ReservationStatus.RESERVATION);

    @BeforeEach
    void setUp() {
        reservationService = new ReservationService(reservationRepository,
            reservationTimeRepository, themeRepository, memberRepository);
        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
        given(reservationTimeRepository.findById(timeId)).willReturn(Optional.of(reservationTime));
        given(themeRepository.findById(themeId)).willReturn(Optional.of(theme));
    }

    @AfterEach
    void verifyNumberOfInvocations() {
        verify(memberRepository, times(1)).findById(anyLong());
        verify(reservationTimeRepository, times(1)).findById(anyLong());
        verify(themeRepository, times(1)).findById(anyLong());
        verify(reservationRepository, times(1)).findByDateAndReservationTimeAndTheme(any(), any(), any());
    }

    @Test
    @DisplayName("예약을 등록한다")
    void saveReservation() {
        given(reservationRepository.save(reservation))
            .willReturn(
                new Reservation(reservationId, member, LocalDate.now().plusDays(1),
                    reservationTime, theme, ReservationStatus.RESERVATION));

        ReservationResponse response = reservationService.saveReservation(memberId,
            new ReservationRequest(reservationDateString, timeId, themeId));

        assertThat(response.getTime()).isEqualTo(time);
        assertThat(response.getName()).isEqualTo(memberName);
        assertThat(response.getThemeName()).isEqualTo(themeName);
    }

    @Test
    @DisplayName("이미 예약된 예약을 등록할 경우 실패한다")
    void saveReservationAlreadyExists() {
        Reservation savedReservation = new Reservation(reservationId, member, reservationDate,
            reservationTime, theme, ReservationStatus.RESERVATION);
        given(reservationRepository.findByDateAndReservationTimeAndTheme(reservationDate,
            reservationTime, theme))
            .willReturn(List.of(savedReservation));

        assertThrows(ReservationAlreadyExistsException.class,
            () -> reservationService.saveReservation(memberId, new ReservationRequest(
                reservationDateString, timeId, themeId)));
    }

    @Test
    @DisplayName("대기 예약을 등록한다.")
    void saveWaitingReservation() {
        Member otherMember = new Member(2L, "email", "password", "otherMember", MemberRole.MEMBER);
        Reservation savedReservation =
            new Reservation(reservationId, otherMember, reservationDate, reservationTime, theme, ReservationStatus.RESERVATION);
        given(reservationRepository.findByDateAndReservationTimeAndTheme(reservationDate, reservationTime, theme))
            .willReturn(List.of(savedReservation));
        given(reservationRepository.save(reservation))
            .willReturn(
                new Reservation(reservationId, member, LocalDate.now().plusDays(1),
                    reservationTime, theme, ReservationStatus.WAITING));

        ReservationResponse response = reservationService.saveWaitingReservation(memberId,
            new ReservationRequest(reservationDateString, timeId, themeId));

        assertThat(response.getTime()).isEqualTo(time);
        assertThat(response.getName()).isEqualTo(memberName);
        assertThat(response.getThemeName()).isEqualTo(themeName);
    }
}
