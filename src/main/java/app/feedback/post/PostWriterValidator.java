package app.feedback.post;

import app.feedback.common.exception.CustomErrorCode;
import app.feedback.common.exception.CustomException;
import app.feedback.member.domain.Member;
import app.feedback.member.domain.MemberRepository;
import app.feedback.member.domain.Role;
import app.feedback.post.domain.Post;
import app.feedback.post.domain.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PostWriterValidator {
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    public void validateAdminOrMe(String email, Long postId) {
        Optional<Member> memberOptional = memberRepository.findByEmail(email);
        if (memberOptional.isPresent()) {
            if (validateAdmin(memberOptional)) return;
            Optional<Post> postOptional = postRepository.findById(postId);
            if (postOptional.isPresent()) {
                validateMe(postOptional, memberOptional);
            } else {
                throw new CustomException(CustomErrorCode.POST_NOT_FOUND);
            }
        } else {
            throw new CustomException(CustomErrorCode.UNAUTHORIZED);
        }
    }

    private void validateMe(Optional<Post> postOptional, Optional<Member> memberOptional) {
        if (postOptional.get().getWriter().getEmail()
                .equals(memberOptional.get().getEmail())) {
            return;
        } else {
            throw new CustomException(CustomErrorCode.FORBIDDEN);
        }
    }

    private boolean validateAdmin(Optional<Member> memberOptional) {
        if (memberOptional.get().getRole().equals(Role.ADMIN)) {
            return true;
        }
        return false;
    }
}
