package roomescape.adapter.out.persistence;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import roomescape.adapter.mapper.ReservationTimeMapper;
import roomescape.adapter.out.ReservationTimeEntity;
import roomescape.adapter.out.persistence.repository.ReservationTimeRepository;
import roomescape.application.port.out.ReservationTimePort;
import roomescape.domain.ReservationTime;

@Repository
public class ReservationTimePersistenceAdapter implements ReservationTimePort {

  private final ReservationTimeRepository reservationTimeRepository;

  public ReservationTimePersistenceAdapter(ReservationTimeRepository reservationTimeRepository) {
    this.reservationTimeRepository = reservationTimeRepository;
  }

  @Override
  public ReservationTime saveReservationTime(ReservationTime reservationTime) {
    ReservationTimeEntity reservationTimeEntity = reservationTimeRepository.save(
      ReservationTimeMapper.mapToEntity(reservationTime)
    );

    return ReservationTimeMapper.mapToDomain(reservationTimeEntity);
  }

  @Override
  public List<ReservationTime> findReservationTimes() {

    return reservationTimeRepository.findAll()
                                    .stream()
                                    .map(ReservationTimeMapper::mapToDomain)
                                    .toList();
  }

  @Override
  public void deleteReservationTime(Long id) {
    reservationTimeRepository.deleteById(id);
  }

  @Override
  public Optional<ReservationTime> findReservationTimeById(Long id) {
    return reservationTimeRepository.findById(id)
                                    .map(ReservationTimeMapper::mapToDomain);
  }

  @Override
  public Optional<ReservationTime> findReservationTimeByStartAt(String startAt) {
    return reservationTimeRepository.findByStartAt(startAt)
                                    .map(ReservationTimeMapper::mapToDomain);
  }

  @Override
  public List<ReservationTime> findAvailableReservationTimes(String date, Long themeId) {
    return reservationTimeRepository.findAvailableReservationTimes(date, themeId)
                                    .stream()
                                    .map(ReservationTimeMapper::mapToDomain)
                                    .toList();
  }
}
