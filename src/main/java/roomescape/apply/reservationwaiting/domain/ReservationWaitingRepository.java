package roomescape.apply.reservationwaiting.domain;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationWaitingRepository {
    ReservationWaiting save(ReservationWaiting reservationWaiting);
    @Query("SELECT rw FROM ReservationWaiting rw WHERE rw.member.id = :memberId")
    List<ReservationWaiting> findAllByMemberId(@Param("memberId") Long memberId);
    @Query("""
    SELECT
        COUNT(rw)
     FROM
        ReservationWaiting rw
     WHERE
        rw.themeId = :themeId
        AND rw.date = :date
        AND rw.timeId = :timeId
        AND rw.waitingTime < :waitingTime
    """)
    long countByThemeIdAndDateAndTimeIdAndWaitingTimeLessThan(@Param("themeId") long themeId,
                                                              @Param("date") String date,
                                                              @Param("timeId") long timeId,
                                                              @Param("waitingTime") LocalDateTime waitingTime);
}
