package app.feedback.auth;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AutoLoginRepository extends JpaRepository<AutoLogin, String> {
}
