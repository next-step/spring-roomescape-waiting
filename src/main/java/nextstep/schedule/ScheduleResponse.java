package nextstep.schedule;

import nextstep.theme.ThemeResponse;

import java.time.LocalDate;
import java.time.LocalTime;

public class ScheduleResponse {
    private Long id;
    private ThemeResponse themeResponse;
    private LocalDate date;
    private LocalTime time;

    public ScheduleResponse(Long id, ThemeResponse themeResponse, LocalDate date, LocalTime time) {
        this.id = id;
        this.themeResponse = themeResponse;
        this.date = date;
        this.time = time;
    }

    public static ScheduleResponse from(Schedule schedule) {
        return new ScheduleResponse(
                schedule.getId(),
                ThemeResponse.of(schedule.getTheme()),
                schedule.getDate(),
                schedule.getTime()
        );
    }

    public Long getId() {
        return id;
    }

    public ThemeResponse getThemeResponse() {
        return themeResponse;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }
}