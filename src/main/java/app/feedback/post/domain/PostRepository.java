package app.feedback.post.domain;

import app.feedback.post.dto.PostCountProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("select p.createdDate as createdDate, " +
            "count(p) as count from Post p " +
            "where p.writer.email=:email " +
            "group by p.createdDate order by p.createdDate asc")
    List<PostCountProjection> countPostsGroupByDate(String email);

    @Query("select p from Post p " +
            "where p.writer.email=:email and p.createdDate=:date " +
            "order by p.createdTime desc")
    List<Post> findAllByEmailAndDate(String email, LocalDate date);
}
