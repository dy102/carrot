package app.feedback.auth;

import app.feedback.auth.dto.Authentication;
import app.feedback.auth.dto.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    public static final String AUTHORIZATION = "authorization";

    private final AuthService authService;
    private final AutoLoginManager autoLoginManager;


    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody final LoginRequest request,
                                      final HttpServletResponse httpServletResponse,
                                      final HttpSession session) {
        final Authentication authentication = authService.login(request);

        if (request.isAuto()) {
            autoLoginManager.setAutoCookie(session, httpServletResponse, authentication);
        }

        session.setAttribute(AUTHORIZATION, authentication);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(final HttpSession session,
                                       final HttpServletRequest request,
                                       final HttpServletResponse response) {
        session.removeAttribute(AUTHORIZATION);
        autoLoginManager.removeAutoLogin(request, response);
        return ResponseEntity.noContent().build();
    }

}
