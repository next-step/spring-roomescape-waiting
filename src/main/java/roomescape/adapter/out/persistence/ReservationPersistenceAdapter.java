package roomescape.adapter.out.persistence;

import static roomescape.adapter.mapper.ReservationMapper.mapToDomain;
import static roomescape.adapter.mapper.ReservationMapper.mapToEntity;

import java.util.List;
import java.util.Optional;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import roomescape.adapter.mapper.ReservationMapper;
import roomescape.adapter.mapper.ReservationTimeMapper;
import roomescape.adapter.out.ReservationEntity;
import roomescape.adapter.out.persistence.repository.ReservationRepository;
import roomescape.application.port.out.ReservationPort;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

@Primary
@Repository
public class ReservationPersistenceAdapter implements ReservationPort {

  private final ReservationRepository reservationRepository;

  public ReservationPersistenceAdapter(ReservationRepository reservationRepository) {
    this.reservationRepository = reservationRepository;
  }

  @Override
  public List<Reservation> findReservations() {
    return reservationRepository.findAll()
                                .stream()
                                .map(ReservationMapper::mapToDomain)
                                .toList();

  }

  @Override
  public Optional<Reservation> findReservationByReservationTime(ReservationTime reservationTime) {
    return reservationRepository.findByReservationTime(ReservationTimeMapper.mapToEntity(reservationTime))
                         .map(ReservationMapper::mapToDomain);
  }

  @Override
  public Reservation saveReservation(Reservation reservation) {
    ReservationEntity reservationEntity = reservationRepository.save(mapToEntity(reservation));

    return mapToDomain(reservationEntity);
  }

  @Override
  public void deleteReservation(Long id) {
    reservationRepository.deleteById(id);
  }

  @Override
  public Integer countReservationById(Long id) {
    return reservationRepository.countById(id);
  }

  @Override
  public List<Reservation> findReservationsByMemberId(Long memberId) {
    return reservationRepository.findByMemberId(memberId)
                                .stream()
                                .map(ReservationMapper::mapToDomain)
                                .toList();
  }
}
