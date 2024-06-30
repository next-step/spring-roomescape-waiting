package roomescape.adapter.out.persistence;

import static roomescape.adapter.mapper.ThemeMapper.mapToDomain;
import static roomescape.adapter.mapper.ThemeMapper.mapToEntity;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import roomescape.adapter.mapper.ThemeMapper;
import roomescape.adapter.out.ThemeEntity;
import roomescape.adapter.out.persistence.repository.ThemeRepository;
import roomescape.application.port.out.ThemePort;
import roomescape.domain.Theme;

@Repository
public class ThemePersistenceAdapter implements ThemePort {

  private final ThemeRepository themeRepository;

  public ThemePersistenceAdapter(ThemeRepository themeRepository) {
    this.themeRepository = themeRepository;
  }

  @Override
  public Theme saveTheme(Theme theme) {
    ThemeEntity themeEntity = themeRepository.save(mapToEntity(theme));

    return mapToDomain(themeEntity);
  }

  @Override
  public List<Theme> findThemes() {
    return themeRepository.findAll()
                          .stream()
                          .map(ThemeMapper::mapToDomain)
                          .toList();
  }

  @Override
  public void deleteTheme(Long id) {
    themeRepository.deleteById(id);
  }

  @Override
  public Integer countThemeById(Long id) {
    return themeRepository.countById(id);
  }

  @Override
  public Optional<Theme> findThemeById(Long id) {
    return themeRepository.findById(id)
                          .map(ThemeMapper::mapToDomain);
  }
}
