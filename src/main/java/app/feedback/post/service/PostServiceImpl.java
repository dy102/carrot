package app.feedback.post.service;

import app.feedback.common.exception.CustomErrorCode;
import app.feedback.common.exception.CustomException;
import app.feedback.member.domain.Member;
import app.feedback.member.domain.MemberRepository;
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

import static app.feedback.common.exception.CustomErrorCode.INCONSISTENCY;
import static app.feedback.common.exception.CustomErrorCode.POST_NOT_FOUND;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final ImageService imageService;

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
    public PostResponse find(Long postId) throws CustomException {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            return PostResponse.of(post);
        } else {
            throw new CustomException(POST_NOT_FOUND);
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
    public PostsResponse readMyPost(String email, String date) {
        LocalDate localDate = LocalDate.parse(date);
        List<Post> posts = postRepository.findAllByEmailAndDate(email, localDate);
        return getPostsResponse(posts);
    }

    @Override
    public PostCalendarResponse getCalendar(String memberId, Integer year, Integer month) {
        Optional<Member> memberOptional = memberRepository.findByEmail(memberId);
        if (memberOptional.isPresent()) {
            List<PostCountProjection> countedPostsGroupByDate = postRepository.countPostsGroupByDate(memberId);

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
}
