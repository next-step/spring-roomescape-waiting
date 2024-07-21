package roomescape.waiting.application;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import roomescape.reservation.dto.MyReservationResponse;
import roomescape.reservation.exception.PastDateReservationException;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.repository.ThemeRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.domain.repository.ReservationTimeRepository;
import roomescape.user.domain.User;
import roomescape.user.domain.repository.UserRepository;
import roomescape.waiting.domain.ReservationWaiting;
import roomescape.waiting.domain.repository.ReservationWaitingRepository;
import roomescape.waiting.dto.ReservationWaitingRequest;
import roomescape.waiting.dto.ReservationWaitingResponse;

@Service
@Transactional(readOnly = true)
public class ReservationWaitingService {

    private final ReservationWaitingRepository reservationWaitingRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final UserRepository userRepository;

    public ReservationWaitingService(ReservationWaitingRepository reservationWaitingRepository,
                                     ReservationTimeRepository reservationTimeRepository,
                                     ThemeRepository themeRepository,
                                     UserRepository userRepository) {
        this.reservationWaitingRepository = reservationWaitingRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ReservationWaitingResponse addWaiting(ReservationWaitingRequest request, Long userId) {
        User waitingUser = getUser(userId);
        ReservationTime findReservationTime = reservationTimeRepository.findById(request.time())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 예약 시간입니다."));
        Theme findTheme = themeRepository.findById(request.theme())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 테마입니다."));

        if (LocalDate.now().isAfter(request.date())) {
            throw new PastDateReservationException();
        }

        if (reservationWaitingRepository.existsByUserAndDateAndTimeAndTheme(waitingUser, request.date(), findReservationTime, findTheme)) {
            throw new IllegalArgumentException("이미 예약하셨습니다.");
        }

        ReservationWaiting reservationWaiting = request.toReservationWaiting(waitingUser, findReservationTime, findTheme);
        ReservationWaiting savedWaiting = reservationWaitingRepository.save(reservationWaiting);
        return ReservationWaitingResponse.from(savedWaiting);
    }

    public List<MyReservationResponse> getMyWaiting(Long userId) {
        User user = getUser(userId);
        List<ReservationWaiting> reservationWaitings = reservationWaitingRepository.findAllByUser(user);
        return reservationWaitings.stream()
                .map(MyReservationResponse::from)
                .toList();
    }

    public void deleteWaiting(Long waitingId, Long userId) {
        User user = getUser(userId);
        ReservationWaiting reservationWaiting = reservationWaitingRepository.findById(waitingId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 예약 대기 중인 내역입니다."));
        reservationWaiting.validateWaitingBy(user);

        reservationWaitingRepository.delete(reservationWaiting);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 사용자입니다."));
    }
}
