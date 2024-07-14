package app.feedback.member;

import app.feedback.auth.AuthService;
import app.feedback.auth.Authorized;
import app.feedback.auth.dto.Authentication;
import app.feedback.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final AuthService authService;
    private static final String RESPONSE = "response";

    @PostMapping
    public String create(
            @ModelAttribute final MemberCreateRequest memberCreateRequest
    ) {
        memberService.create(memberCreateRequest);
        return "form/login";
    }

    @GetMapping
    public String read(@Authorized final Authentication authentication,
                       final Model model) {
        authService.validateAdmin(authentication);
        MembersResponse response = memberService.read();
        model.addAttribute(RESPONSE, response);
        return "form/members";
    }

    @GetMapping("/{memberId}")
    public String find(@Authorized final Authentication authentication,
                       @PathVariable final String memberId,
                       Model model) {
        authService.validateAdminOrMe(authentication, memberId);
        MemberResponse response = memberService.find(memberId);
        model.addAttribute(RESPONSE, response);
        return "form/members/{memberId}";
    }

    @GetMapping("/simple/{memberId}")
    public String findSimple(@PathVariable final String memberId, Model model) {
        MemberSimpleResponse response = memberService.findSimple(memberId);
        model.addAttribute(RESPONSE, response);
        return "form/members/simple";
    }

    @PutMapping("/{memberId}")
    public String update(@Authorized final Authentication authentication,
                         @PathVariable final String memberId,
                         @RequestBody final MemberUpdateRequest memberUpdateRequest,
                         final Model model) {
        authService.validateAdminOrMe(authentication, memberId);
        memberService.update(memberId, memberUpdateRequest);
        MemberResponse response = memberService.find(memberId);
        model.addAttribute(RESPONSE, response);
        return "form/members/{memberId}";
    }

    @DeleteMapping("/{memberId}")
    public String delete(@Authorized final Authentication authentication,
                         @PathVariable final String memberId) {
        authService.validateAdminOrMe(authentication, memberId);
        memberService.delete(memberId);
        return "form/members";
    }

    @GetMapping("/signup")
    public String signupForm() {
        return "form/signup";
    }
}
