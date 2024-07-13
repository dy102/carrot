package app.feedback.auth;

import app.feedback.auth.dto.Authentication;
import app.feedback.auth.dto.LoginRequest;

public interface AuthService {
    Authentication login(final LoginRequest loginRequest);
}
