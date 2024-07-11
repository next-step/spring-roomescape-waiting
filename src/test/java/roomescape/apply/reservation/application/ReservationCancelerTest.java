package roomescape.apply.reservation.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.apply.member.application.MemberFinder;
import roomescape.apply.member.application.MemberRoleFinder;
import roomescape.apply.member.application.mock.MockPasswordHasher;
import roomescape.apply.member.domain.Member;
import roomescape.apply.member.domain.MemberRepository;
import roomescape.apply.member.infra.InMemoryMemberRepository;
import roomescape.apply.member.infra.InMemoryMemberRoleRepository;
import roomescape.apply.reservation.domain.Reservation;
import roomescape.apply.reservation.domain.ReservationRepository;
import roomescape.apply.reservation.domain.ReservationStatus;
import roomescape.apply.reservation.infra.InMemoryReservationRepository;
import roomescape.apply.reservationtime.application.ReservationTimeFinder;
import roomescape.apply.reservationtime.domain.ReservationTime;
import roomescape.apply.reservationtime.domain.ReservationTimeRepository;
import roomescape.apply.reservationtime.infra.InMemoryReservationTimeRepository;
import roomescape.apply.reservationwaiting.application.ReservationWaitingFinder;
import roomescape.apply.reservationwaiting.application.WaitingPositionCalculator;
import roomescape.apply.reservationwaiting.infra.InMemoryReservationWaitingRepository;
import roomescape.apply.theme.application.ThemeFinder;
import roomescape.apply.theme.domain.Theme;
import roomescape.apply.theme.domain.ThemeRepository;
import roomescape.apply.theme.infra.InMemoryThemeRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.support.MemberFixture.member;
import static roomescape.support.ReservationsFixture.*;

class ReservationCancelerTest {

    private ReservationCanceler reservationCanceler;
    private ReservationRepository reservationRepository;
    private ReservationTimeRepository reservationTimeRepository;
    private ThemeRepository themeRepository;
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        reservationRepository = new InMemoryReservationRepository();
        reservationTimeRepository = new InMemoryReservationTimeRepository(reservationRepository);
        themeRepository = new InMemoryThemeRepository();
        memberRepository = new InMemoryMemberRepository();

        var reservationWaitingRepository = new InMemoryReservationWaitingRepository();
        var waitingPositionCalculator = new WaitingPositionCalculator(reservationWaitingRepository);
        var reservationWaitingFinder = new ReservationWaitingFinder(waitingPositionCalculator, reservationWaitingRepository);

        var reservationRecorder = getReservationRecorder(reservationWaitingFinder);
        reservationCanceler = new ReservationCanceler(reservationRepository, reservationWaitingFinder, reservationRecorder);
    }

    @Test
    @DisplayName("기존 예약을 취소할 수 있다.")
    void cancelTest() {
        // given
        Member saveMember = memberRepository.save(member());
        ReservationTime saveReservationTime = reservationTimeRepository.save(reservationTime());
        Theme saveTheme = themeRepository.save(theme());
        Reservation saved = reservationRepository.save(reservation(saveReservationTime, saveTheme, "2099-01-01", saveMember.getId()));
        assertThat(reservationRepository.findAllFetchJoinThemeAndTime().size()).isNotZero();
        // when
        reservationCanceler.cancelReservation(saved.getId());
        // then
        final List<Reservation> allFetchJoinThemeAndTime = reservationRepository.findAllFetchJoinThemeAndTime();
        assertThat(allFetchJoinThemeAndTime).extracting("reservationStatus")
                .containsExactlyInAnyOrder(ReservationStatus.CANCELED);
    }

    private ReservationRecorder getReservationRecorder(ReservationWaitingFinder reservationWaitingFinder) {
        var memberFinder = new MemberFinder(new MockPasswordHasher(),
                memberRepository,
                new MemberRoleFinder(new InMemoryMemberRoleRepository()));
        return new ReservationRecorder(reservationRepository,
                new ReservationTimeFinder(reservationTimeRepository),
                new ThemeFinder(themeRepository),
                new ReservationFinder(reservationRepository, reservationWaitingFinder),
                memberFinder);
    }


}
