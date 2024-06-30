package roomescape.adapter.out;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import roomescape.domain.Theme;

@Entity(name = "theme")
public class ThemeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private String description;
  private String thumbnail;

  @OneToMany(mappedBy = "theme")
  private List<ReservationEntity> reservationEntities = new ArrayList<>();

  public ThemeEntity() {
  }

  public ThemeEntity(Long id, String name, String description, String thumbnail) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.thumbnail = thumbnail;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public String getThumbnail() {
    return thumbnail;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ThemeEntity that = (ThemeEntity) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  public static ThemeEntity of(Theme theme) {
    return new ThemeEntity(theme.getId(), theme.getName(), theme.getDescription(), theme.getThumbnail());
  }
}
