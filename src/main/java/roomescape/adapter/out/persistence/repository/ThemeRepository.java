package roomescape.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import roomescape.adapter.out.ThemeEntity;

public interface ThemeRepository extends JpaRepository<ThemeEntity, Long>{

  Integer countById(Long id);
}
