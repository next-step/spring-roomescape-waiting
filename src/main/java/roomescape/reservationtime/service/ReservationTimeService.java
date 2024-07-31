package roomescape.reservationtime.service;

import java.time.LocalDate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.error.exception.ReservationTimeNotExistsException;
import roomescape.error.exception.ReservationTimeReferenceException;
import roomescape.error.exception.ThemeNotExistsException;
import roomescape.reservation.repository.ReservationRepository;

import java.util.List;
import roomescape.reservationtime.ReservationTime;
import roomescape.reservationtime.dto.AvailableTimeResponse;
import roomescape.reservationtime.dto.ReservationTimeRequest;
import roomescape.reservationtime.dto.ReservationTimeResponse;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.theme.Theme;
import roomescape.theme.repository.ThemeRepository;

@Service
@Transactional(readOnly = true)
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository,
                                    ReservationRepository reservationRepository,
                                    ThemeRepository themeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
        this.themeRepository = themeRepository;
    }

    @Transactional
    public ReservationTimeResponse saveReservationTime(ReservationTimeRequest request) {
        ReservationTime reservationTime = new ReservationTime(request.getStartAt());
        return ReservationTimeResponse.of(reservationTimeRepository.save(reservationTime));
    }

    public List<ReservationTimeResponse> findReservationTimes() {
        return reservationTimeRepository.findAll().stream()
            .map(ReservationTimeResponse::of)
            .toList();
    }

    @Transactional
    public void deleteReservationTime(Long id) {
        ReservationTime reservationTime = reservationTimeRepository.findById(id)
            .orElseThrow(ReservationTimeNotExistsException::new);

        if (reservationRepository.findByReservationTime(reservationTime).isPresent()) {
            throw new ReservationTimeReferenceException();
        }

        reservationTimeRepository.deleteById(id);
    }

    public List<AvailableTimeResponse> findAvailableReservationTimes(String date, Long themeId) {
        Theme theme = themeRepository.findById(themeId)
            .orElseThrow(ThemeNotExistsException::new);
        List<Long> ids = reservationRepository.findByDateAndTheme(LocalDate.parse(date), theme)
            .stream().map(reservation -> reservation.getReservationTime().getId())
            .toList();

        return reservationTimeRepository.findAll().stream()
            .map(reservationTime ->
                AvailableTimeResponse.from(reservationTime, ids.contains(reservationTime.getId())))
            .toList();
    }
}
