package app.feedback.auth;

import app.feedback.auth.dto.Authentication;
import app.feedback.auth.dto.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    public static final String AUTHORIZATION = "authorization";

    private final AuthServiceImpl authService;
    private final AutoLoginManager autoLoginManager;


    @PostMapping("/login")
    public String login(final @ModelAttribute LoginRequest request,
                        final HttpServletResponse httpServletResponse,
                        final HttpSession session) {
        final Authentication authentication = authService.login(request);

        if (request.isAuto()) {
            autoLoginManager.setAutoCookie(session, httpServletResponse, authentication);
        }

        session.setAttribute(AUTHORIZATION, authentication);
        return "redirect:/posts";
    }

    @PostMapping("/logout")
    public String logout(final HttpSession session,
                         final HttpServletRequest request,
                         final HttpServletResponse response) {
        session.removeAttribute(AUTHORIZATION);
        autoLoginManager.removeAutoLogin(request, response);
        return "redirect:/members/signup";
    }

    @GetMapping("/loginForm")
    public String logoutForm() {
        return "form/login";
    }

    @GetMapping("/signupForm")
    public String signForm() {
        return "form/signup";
    }
}
