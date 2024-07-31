package roomescape.theme.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.error.exception.ThemeNotExistsException;
import roomescape.error.exception.ThemeReferenceException;
import roomescape.reservation.repository.ReservationRepository;

import java.util.List;
import roomescape.theme.Theme;
import roomescape.theme.dto.ThemeRequest;
import roomescape.theme.dto.ThemeResponse;
import roomescape.theme.repository.ThemeRepository;

@Service
@Transactional(readOnly = true)
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public ThemeService(ThemeRepository themeRepository,
                        ReservationRepository reservationRepository) {
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<ThemeResponse> findThemes() {
        return themeRepository.findAll().stream()
            .map(ThemeResponse::new)
            .toList();
    }

    @Transactional
    public Theme saveTheme(ThemeRequest request) {
        return themeRepository.save(
            new Theme(request.getName(), request.getDescription(), request.getThumbnail()));
    }

    public ThemeResponse findTheme(Long id) {
        Theme theme = themeRepository.findById(id).orElseThrow(ThemeNotExistsException::new);
        return new ThemeResponse(theme);
    }

    @Transactional
    public void deleteTheme(long id) {
        Theme theme = themeRepository.findById(id).orElseThrow(ThemeNotExistsException::new);

        if (reservationRepository.findByTheme(theme).isPresent()) {
            throw new ThemeReferenceException();
        }

        themeRepository.deleteById(id);
    }
}
