package app.feedback.post.dto;

import java.time.LocalDate;
import java.util.List;

public record PostCalendarResponse(
        int startDay,
        int totalDays,
        int year,
        int month,
        List<CalendarDto> calendarDtos
) {
    public static PostCalendarResponse of(LocalDate date, List<CalendarDto> calendarDtos) {
        return new PostCalendarResponse(
                date.getDayOfWeek().getValue(),
                date.getMonth().maxLength(),
                date.getYear(),
                date.getMonth().getValue(),
                calendarDtos);
    }
}
