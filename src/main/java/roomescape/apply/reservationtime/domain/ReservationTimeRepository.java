package roomescape.apply.reservationtime.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReservationTimeRepository {

    ReservationTime save(ReservationTime reservationTime);

    List<ReservationTime> findAll();

    void deleteById(Long id);

    @Query("SELECT rt.id FROM ReservationTime rt WHERE rt.id = :id")
    Optional<Long> findIdById(@Param("id") long id);

    Optional<ReservationTime> findOneById(long timeId);
}
