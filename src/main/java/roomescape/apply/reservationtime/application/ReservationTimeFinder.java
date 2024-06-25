package roomescape.apply.reservationtime.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.apply.reservation.application.ReservationFinder;
import roomescape.apply.reservationtime.application.exception.NotFoundReservationTimeException;
import roomescape.apply.reservationtime.domain.ReservationTime;
import roomescape.apply.reservationtime.domain.ReservationTimeRepository;
import roomescape.apply.reservationtime.ui.dto.AvailableReservationTimeResponse;
import roomescape.apply.reservationtime.ui.dto.ReservationTimeResponse;
import roomescape.support.checker.ReservationTimeRequestChecker;

import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class ReservationTimeFinder {

    private final ReservationFinder reservationFinder;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeFinder(ReservationFinder reservationFinder,
                                 ReservationTimeRepository reservationTimeRepository
    ) {
        this.reservationFinder = reservationFinder;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationTimeResponse> findAll() {
        return reservationTimeRepository.findAll()
                .stream()
                .sorted(ReservationTime::compareByStartTime)
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public ReservationTime findOneById(long timeId) {
        return reservationTimeRepository.findOneById(timeId).orElseThrow(NotFoundReservationTimeException::new);
    }

    public List<AvailableReservationTimeResponse> findAvailableTimesBy(String date, String themeId) {
        ReservationTimeRequestChecker.validateRequestParam(date, themeId);

        final List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
        final Set<Long> reservedTimeIds = reservationFinder.findAlreadyReservedTimeIdsBy(date, Long.parseLong(themeId));

        return reservationTimes.stream()
                .map(it -> AvailableReservationTimeResponse.from(it, reservedTimeIds.contains(it.getId())))
                .toList();
    }
}
