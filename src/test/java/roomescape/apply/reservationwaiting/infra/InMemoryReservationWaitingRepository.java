package roomescape.apply.reservationwaiting.infra;

import roomescape.apply.reservationwaiting.domain.ReservationWaiting;
import roomescape.apply.reservationwaiting.domain.ReservationWaitingRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryReservationWaitingRepository implements ReservationWaitingRepository {

    private final Map<Long, ReservationWaiting> map = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong();

    @Override
    public ReservationWaiting save(ReservationWaiting reservationWaiting) {
        Long id = reservationWaiting.getId();
        if (id == null) {
            id = idCounter.incrementAndGet();
            reservationWaiting.changeId(id);
        }
        map.put(id, reservationWaiting);
        return reservationWaiting;
    }

    @Override
    public List<ReservationWaiting> findAllByMemberId(Long memberId) {
        return map.values().stream()
                .filter(it -> it.getMemberId().equals(memberId))
                .toList();
    }

    @Override
    public long countByThemeIdAndDateAndTimeIdAndWaitingTimeLessThan(long themeId,
                                                                     String date,
                                                                     long timeId,
                                                                     LocalDateTime waitingTime
    ) {
        return map.values().stream()
                           .filter(it -> it.getTheme().getId() == themeId)
                           .filter(it -> it.getReservationDate().equals(date))
                           .filter(it -> it.getTime().getId() == timeId)
                           .filter(it -> it.getWaitingTime().isBefore(waitingTime))
                           .count();
    }
}
