package app.feedback.post.dto;

import java.time.format.DateTimeFormatter;

public record CalendarDto(
        String createdDate,
        int dayOfMonth,
        Long count
) {
    public static CalendarDto of(PostCountProjection postCountProjection) {
        return new CalendarDto(
                postCountProjection.getCreatedDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
                postCountProjection.getCreatedDate().getDayOfMonth(),
                postCountProjection.getCount()
        );
    }
}
