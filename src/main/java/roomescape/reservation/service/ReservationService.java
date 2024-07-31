package roomescape.reservation.service;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.error.exception.*;
import roomescape.member.Member;
import roomescape.member.repository.MemberRepository;
import roomescape.reservation.Reservation;
import roomescape.reservation.ReservationStatus;
import roomescape.reservation.WaitingReservation;
import roomescape.reservation.dto.MyReservationResponse;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.ReservationTime;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.theme.Theme;
import roomescape.theme.repository.ThemeRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;

    public ReservationService(ReservationRepository reservationRepository,
        ReservationTimeRepository reservationTimeRepository, ThemeRepository themeRepository,
        MemberRepository memberRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
    }

    public List<ReservationResponse> findReservations() {
        return reservationRepository.findAll().stream()
            .map(ReservationResponse::of)
            .toList();
    }

    @Transactional
    public ReservationResponse saveReservation(Long memberId, ReservationRequest request) {
        Reservation reservation = createReservation(memberId, request,
            ReservationStatus.RESERVATION);

        if (!reservationRepository.findByDateAndReservationTimeAndTheme(reservation.getDate(),
            reservation.getReservationTime(), reservation.getTheme()).isEmpty()) {
            throw new ReservationAlreadyExistsException();
        }

        return ReservationResponse.of(reservationRepository.save(reservation));
    }

    @Transactional
    public ReservationResponse saveWaitingReservation(Long memberId, ReservationRequest request) {
        Reservation reservation = createReservation(memberId, request, ReservationStatus.WAITING);

        List<Reservation> reservations = reservationRepository.findByDateAndReservationTimeAndTheme(
            reservation.getDate(), reservation.getReservationTime(), reservation.getTheme());

        if (reservations.isEmpty()) {
            throw new ReservationNotExistsException();
        }

        if (reservations.stream().anyMatch(x -> x.hasSameMember(reservation.getMember()))) {
            throw new ReservationAlreadyExistsException();
        }

        reservationRepository.save(reservation);

        return ReservationResponse.of(reservation);
    }

    private Reservation createReservation(Long memberId, ReservationRequest request,
        ReservationStatus status) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(MemberNotExistsException::new);
        ReservationTime reservationTime = reservationTimeRepository.findById(request.getTimeId())
            .orElseThrow(ReservationTimeNotExistsException::new);
        Theme theme = themeRepository.findById(request.getThemeId())
            .orElseThrow(ThemeNotExistsException::new);

        Reservation reservation = new Reservation(member, request.getDate(), reservationTime, theme,
            status);

        if (reservation.isBeforeThan(LocalDateTime.now())) {
            throw new PastDateTimeException();
        }

        return reservation;
    }


    @Transactional
    public void deleteReservation(long id) {
        reservationRepository.deleteById(id);
    }

    public List<MyReservationResponse> findReservationsByMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(MemberNotExistsException::new);

        return reservationRepository.findByMember(member).stream()
            .map(r -> new WaitingReservation(r, reservationRepository.findRankByMember(
                r.getId(), r.getDate(), r.getReservationTime(), r.getTheme())))
            .map(MyReservationResponse::from)
            .toList();
    }
}
