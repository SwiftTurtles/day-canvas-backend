package com.daycanvas.domain.post;

import com.daycanvas.domain.user.User;
import com.daycanvas.domain.user.UserService;
import com.daycanvas.dto.post.DayImageMappingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    @Value("${flask.api.url}")
    private String flaskApiUrl;
    private final RestTemplate restTemplate = new RestTemplate();
    private final UserService userService;

    private final PostRepository repository;

    public Long save(Post post, @AuthenticationPrincipal OAuth2User principal) {
        ResponseEntity<String> response = restTemplate.postForEntity(flaskApiUrl, post.getContent().getBytes(), String.class);
        User user = userService.findUserById(principal);
        post.setImagePath(response.getBody());
        post.setUser(user);
        user.addPost(post);

        return repository.save(post).getId();
    }

    public Post findById(Long postId) {
        try {
            Optional<Post> OptionalPost = repository.findById(postId);

            if (OptionalPost.isPresent()) {
                return OptionalPost.get();
            }
            else {
                throw new EmptyResultDataAccessException("Post not found with id: " + postId, 1);
            }
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("Post not found with id: " + postId);
        }
    }

    public List<DayImageMappingDto> findAllByMonth(int year, int month, @AuthenticationPrincipal OAuth2User principal) {
        return repository.findAllByMonthAndYear(getUserId(principal), year, month);
    }

    public List<Post> findAll(@AuthenticationPrincipal OAuth2User principal) {
        Long userId = getUserId(principal);
        return repository.findByUserId(userId);
    }

    public Long update(Post post, @AuthenticationPrincipal OAuth2User principal) {
        try {
            Post savedPost = getSavedPost(post.getId());

            validateUserPermission(savedPost.getUser().getId(), principal);

            savedPost.setContent(post.getContent());
            ResponseEntity<String> response = restTemplate.postForEntity(flaskApiUrl, post.getContent().getBytes(), String.class);
            savedPost.setImagePath(response.getBody());
            repository.save(savedPost);
            return savedPost.getId();
        } catch (EmptyResultDataAccessException ex) {
            throw new RuntimeException("Post not found for deletion.");
        } catch (DataAccessException ex) {
            // 그 외 데이터베이스 관련 예외 처리
            throw new RuntimeException("Error finding exist post", ex);
        }
    }

    public void delete(Long postId, @AuthenticationPrincipal OAuth2User principal) {
        try {
            Post savedPost = getSavedPost(postId);

            validateUserPermission(savedPost.getUser().getId(), principal);

            repository.deleteById(postId);
        } catch (EmptyResultDataAccessException ex) {
            // 삭제할 대상이 없는 경우에 대한 예외 처리
            throw new RuntimeException("Post with ID" + postId + " not found for deletion.");
        } catch (DataAccessException ex) {
            // 그 외 데이터베이스 관련 예외 처리
            throw new RuntimeException("Error deleting user with ID " + postId, ex);
        }
    }

    /**
     *
     * @param postId PostId
     * @return Repository로 부터 getPost => 찾지 못한다면 예외 throw
     */
    private Post getSavedPost(Long postId) {
        Optional<Post> optionalPost = repository.findById(postId);
        return optionalPost.orElseThrow(() -> new RuntimeException("Post not found for ID: " + postId));
    }

    /**
     * Post-userId == Login-userId
     * 로그인된 user와 post작성한 user 같은지 검증하는 method
     * @param userId Post에 저장되어 있는 Post-user_id 매개변수로 받는다
     * @param principal OAuth2User
     */
    private static void validateUserPermission(Long userId, OAuth2User principal) {
        Long loginUserId = getUserId(principal);
        if (!loginUserId.equals(userId)) {
            throw new RuntimeException("User does not have permission to perform this operation");
        }
    }

    /**
     * OAuth2User에서 담아놨던 user_id를 꺼내는 method
     * @param principal OAuth2User
     * @return userid
     */
    private static Long getUserId(OAuth2User principal) {
        return principal.getAttribute("user_id");
    }
}