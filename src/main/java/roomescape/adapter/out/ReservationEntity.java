package roomescape.adapter.out;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Objects;

@Entity(name = "reservation")
public class ReservationEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String date;

  @ManyToOne
  @JoinColumn(name = "time_id")
  private ReservationTimeEntity reservationTime;

  @ManyToOne
  @JoinColumn(name = "theme_id")
  private ThemeEntity theme;

  @ManyToOne
  @JoinColumn(name = "member_id")
  private MemberEntity member;

  public ReservationEntity() {
  }

  public ReservationEntity(Long id, String date, ReservationTimeEntity reservationTime, ThemeEntity theme,
    MemberEntity member) {
    this.id = id;
    this.date = date;
    this.reservationTime = reservationTime;
    this.theme = theme;
    this.member = member;
  }

  public Long getId() {
    return id;
  }

  public String getDate() {
    return date;
  }

  public ReservationTimeEntity getReservationTime() {
    return reservationTime;
  }

  public ThemeEntity getTheme() {
    return theme;
  }

  public MemberEntity getMember() {
    return member;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReservationEntity that = (ReservationEntity) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  public static ReservationEntity of(Long id, String date, ReservationTimeEntity time, ThemeEntity theme,
    MemberEntity member) {
    return new ReservationEntity(id, date, time, theme, member);
  }
}
