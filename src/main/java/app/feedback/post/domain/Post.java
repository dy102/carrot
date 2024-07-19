package app.feedback.post.domain;

import app.feedback.ai.AIChat;
import app.feedback.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "post")
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Member writer;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = true)
    private String image;

    @OneToOne
    private AIChat aiChat;

    @CreatedDate
    private LocalDateTime createdTime;

    @CreatedDate
    private LocalDate createdDate;

    protected Post() {
    }

    public Post(final Member writer,
                final String title,
                final String contents,
                final String image) {
        this.writer = writer;
        this.title = title;
        this.contents = contents;
        this.image = image;
    }
}
