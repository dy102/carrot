package app.feedback.post.service;

import app.feedback.ai.dto.AIResponse;
import app.feedback.common.exception.CustomException;
import app.feedback.post.dto.PostCalendarResponse;
import app.feedback.post.dto.PostCreateUpdateRequest;
import app.feedback.post.dto.PostResponse;
import app.feedback.post.dto.PostsResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    Long create(String userId, PostCreateUpdateRequest request, MultipartFile image);

    PostResponse find(String memberId, Long postId) throws CustomException;

    PostsResponse read();

    PostCalendarResponse getCalendar(String memberId, Integer year, Integer month);

    PostsResponse readMyPost(String email, String date);

    List<Integer> getYears();

    List<Integer> getMonths();

    void delete(String email, Long postId);

    AIResponse getAIChat(Long postId);
}
