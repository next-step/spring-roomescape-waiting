package roomescape.domain;

import java.util.Objects;

public class Reservation {

  private final Long id;
  private final String date;
  private final ReservationTime time;
  private final Theme theme;
  private final Member member;

  public Reservation(Long id, String date, ReservationTime time, Theme theme, Member member) {
    this.id = id;
    this.date = date;
    this.time = time;
    this.theme = theme;
    this.member = member;
  }

  public Long getId() {
    return id;
  }

  public String getDate() {
    return date;
  }

  public ReservationTime getTime() {
    return time;
  }

  public Theme getTheme() {
    return theme;
  }

  public Member getMember() {
    return member;
  }

  public Reservation addId(Long index) {
    return new Reservation(index, this.date, this.time, this.theme, this.member);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Reservation that = (Reservation) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "Reservation{" +
      "id=" + id +
      ", date='" + date + '\'' +
      ", time=" + time +
      ", theme=" + theme +
      '}';
  }

  public static Reservation of(Long id, String date, ReservationTime time, Theme theme, Member member) {
    return new Reservation(id, date, time, theme, member);
  }

  public Reservation addReservationTimeAndTheme(ReservationTime reservationTime, Theme theme, Member member) {
    return new Reservation(this.id, this.date, reservationTime, theme, member);
  }
}
