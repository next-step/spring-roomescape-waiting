package roomescape.apply.reservationtime.infra;

import roomescape.apply.reservationtime.domain.ReservationTime;
import roomescape.apply.reservationtime.domain.ReservationTimeRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;


public class InMemoryReservationTimeRepository implements ReservationTimeRepository {

    private final Map<Long, ReservationTime> map = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong();

    @Override
    public ReservationTime save(ReservationTime reservationTime) {
        Long id = reservationTime.getId();
        if (id == null) {
            id = idCounter.incrementAndGet();
            reservationTime.changeId(id);
        }
        map.put(id, reservationTime);
        return reservationTime;
    }

    @Override
    public List<ReservationTime> findAll() {
        return new ArrayList<>(map.values());
    }

    @Override
    public void deleteById(Long id) {
        map.remove(id);
    }


    @Override
    public Optional<Long> findIdById(long id) {
        return map.containsKey(id) ? Optional.of(id) : Optional.empty();
    }

    @Override
    public Optional<ReservationTime> findOneById(long themeId) {
        return Optional.ofNullable(map.get(themeId));
    }
}
