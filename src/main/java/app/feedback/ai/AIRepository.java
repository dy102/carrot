package app.feedback.ai;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AIRepository extends JpaRepository<AIChat, Long> {
}
