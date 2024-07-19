package app.feedback.post.dto;

import java.time.LocalDate;

public interface PostCountProjection {
    LocalDate getCreatedDate();

    Long getCount();

    default String string() {
        return String.format(
                "PostCountProjection(%s, %s, %s, %s)",
                this.getCreatedDate().toString(),
                this.getCount().toString()
        );
    }
}
