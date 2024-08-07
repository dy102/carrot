package app.feedback.post.service;

import app.feedback.ai.AIChat;
import app.feedback.ai.AIService;
import app.feedback.ai.dto.AIResponse;
import app.feedback.common.exception.CustomErrorCode;
import app.feedback.common.exception.CustomException;
import app.feedback.common.exception.NotFoundException;
import app.feedback.member.domain.Member;
import app.feedback.member.domain.MemberRepository;
import app.feedback.member.domain.Role;
import app.feedback.post.PostWriterValidator;
import app.feedback.post.domain.Post;
import app.feedback.post.domain.PostRepository;
import app.feedback.post.dto.CalendarDto;
import app.feedback.post.dto.PostCalendarResponse;
import app.feedback.post.dto.PostCountProjection;
import app.feedback.post.dto.PostCreateUpdateRequest;
import app.feedback.post.dto.PostResponse;
import app.feedback.post.dto.PostSimpleDto;
import app.feedback.post.dto.PostsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static app.feedback.common.exception.CustomErrorCode.FORBIDDEN;
import static app.feedback.common.exception.CustomErrorCode.INCONSISTENCY;
import static app.feedback.common.exception.CustomErrorCode.POST_NOT_FOUND;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final ImageService imageService;
    private final AIService aiService;
    private final PostWriterValidator postWriterValidator;

    @Override
    public Long create(String userId, PostCreateUpdateRequest request, MultipartFile image) {
        Optional<Member> memberOptional = memberRepository.findByEmail(userId);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            String imagePath = imageService.saveImage(image);
            Post post = new Post(
                    member,
                    request.title(),
                    request.contents(),
                    imagePath
            );
            postRepository.save(post);
            return post.getId();
        } else {
            throw new CustomException(CustomErrorCode.INCONSISTENCY);
        }
    }

    @Override
    public PostResponse find(String memberId, Long postId) throws CustomException {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            boolean isOwner = false;
            if (memberId.equals(post.getWriter().getEmail())) {
                isOwner = true;
            }
            return PostResponse.of(isOwner, post);
        } else {
            throw new NotFoundException();
        }
    }

    @Override
    public PostsResponse read() {
        List<Post> posts = postRepository.findAll();
        return getPostsResponse(posts);
    }

    private PostsResponse getPostsResponse(List<Post> posts) {
        List<PostSimpleDto> responses = new ArrayList<>();
        for (Post post : posts) {
            responses.add(PostSimpleDto.of(post));
        }
        return new PostsResponse(responses);
    }

    @Override
    public PostsResponse readMyPost(String name, String date) {
        LocalDate localDate = LocalDate.parse(date);
        List<Post> posts = postRepository.findAllByEmailAndDate(name, localDate);
        return getPostsResponse(posts);
    }

    @Override
    public PostCalendarResponse getCalendar(String name, Integer year, Integer month) {
        Optional<Member> memberOptional = memberRepository.findByName(name);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            List<PostCountProjection> countedPostsGroupByDate
                    = postRepository.countPostsGroupByDate(member.getEmail());

            List<CalendarDto> calendarDtos = countedPostsGroupByDate.stream()
                    .map(CalendarDto::of)
                    .toList();

            return getPostCalendarResponse(year, month, calendarDtos);
        } else {
            throw new CustomException(INCONSISTENCY);
        }
    }

    private PostCalendarResponse getPostCalendarResponse(Integer year, Integer month, List<CalendarDto> calendarDtos) {
        LocalDate date = getLocalDate(year, month);//null을 고려함
        date = date.minusDays(date.getDayOfMonth() - 1);// 1일로 만들기 ->  반복문에 사용될 것임.

        LocalDate nowDate = date;//date는 반복문에 사용되므로, 현재 date의 상태에 대해 보관할 것.

        //만들어둔 date를 반복문에 사용
        List<CalendarDto> calendars = getCalendarDtosContainingAllDays(date, calendarDtos);
        return PostCalendarResponse.of(nowDate, calendars);
    }

    //기존 dtos는 post된 날짜에 대한 객체만 담고있음 -> 전체 날짜를 돌면서 빈 날짜를 채운다.
    private List<CalendarDto> getCalendarDtosContainingAllDays(
            LocalDate date, List<CalendarDto> calendarDtos
    ) {
        Month month = date.getMonth();
        List<CalendarDto> calendars = new ArrayList<>();
        while (date.getMonth().equals(month)) {
            CalendarDto calendarDto = containDate(date, calendarDtos);
            if (calendarDto != null) {
                calendars.add(calendarDto);
            } else {
                calendars.add(
                        new CalendarDto(
                                date.format(DateTimeFormatter.ISO_LOCAL_DATE),
                                date.getDayOfMonth(),
                                0L
                        )
                );
            }
            date = date.plusDays(1);
        }
        return calendars;
    }

    private LocalDate getLocalDate(Integer year, Integer month) {
        LocalDate date;
        if (year != null && month != null) {
            try {
                date = LocalDate.of(year, month, Month.of(month).maxLength());
            } catch (DateTimeException e) {     //2월 29일 error
                log.info("error={}", e);
                date = LocalDate.of(year, month, Month.of(month).maxLength() - 1);
            }
        } else if (year != null || month != null) {
            if (year != null) {
                date = LocalDate.of(year, LocalDate.now().getMonth(),
                        LocalDate.now().getMonth().maxLength());
            } else {
                date = LocalDate.of(LocalDate.now().getYear(), month,
                        Month.of(month).maxLength());
            }
        } else {
            date = LocalDate.now();
        }
        return date;
    }

    private CalendarDto containDate(LocalDate date, List<CalendarDto> calendarDtos) {
        for (CalendarDto calendarDto : calendarDtos) {
            if (LocalDate.parse(calendarDto.createdDate()).equals(date)) {
                return calendarDto;
            }
        }
        return null;
    }

    @Override
    public List<Integer> getYears() {
        int currentYear = Year.now().getValue();
        return IntStream.rangeClosed(currentYear - 5, currentYear)
                .boxed()
                .toList();
    }

    @Override
    public List<Integer> getMonths() {
        return Arrays.stream(Month.values())
                .map(Month::getValue)
                .toList();
    }

    @Override
    public void delete(String email, Long postId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        Optional<Member> memberOptional = memberRepository.findByEmail(email);
        if (postOptional.isPresent() && memberOptional.isPresent()) {
            Post post = postOptional.get();
            Member member = memberOptional.get();
            if (email.equals(post.getWriter().getEmail()) || member.getRole().equals(Role.ADMIN)) {
                postRepository.delete(post);
            } else {
                throw new CustomException(FORBIDDEN);
            }
        } else {
            throw new CustomException(POST_NOT_FOUND);
        }
    }

    @Override
    public AIResponse getAIChat(Long postId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            if (post.getAiChat() == null) {
                AIChat aiChat = aiService.getAIResponse(post.getTitle() + post.getContents());
                post.setAiChat(aiChat);
                postRepository.save(post);
                return AIResponse.of(aiChat);
            } else {
                return AIResponse.of(post.getAiChat());
            }
        } else {
            throw new CustomException(POST_NOT_FOUND);
        }
    }

    @Override
    public void update(String email, Long postId,
                       PostCreateUpdateRequest updateRequest, MultipartFile multipartFile) {
        postWriterValidator.validateAdminOrMe(email, postId);
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            if (post.getAiChat() == null) {
                throw new CustomException(CustomErrorCode.AI_NULL);
            }
            post.update(
                    updateRequest.title(),
                    updateRequest.contents(),
                    imageService.saveImage(multipartFile)
            );
            post.setAiChat(null);
            postRepository.save(post);
        } else {
            throw new CustomException(POST_NOT_FOUND);
        }
    }
}
