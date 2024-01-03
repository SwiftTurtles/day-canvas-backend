package com.daycanvas.domain.post;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository repository;

    public Long save(Post post) {
        post.setImagePath("image_path"); // @Todo image path -> AI Model 호출 <추후>
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

    public List<String> findAllByMonth(int month) {
        return null;
    }

    public Long update(Post post) {
        try {
            Optional <Post> OptionalPost = repository.findById(post.getId());

            if (OptionalPost.isPresent()) {
                Post savedPost = OptionalPost.get();
                savedPost.setContent(post.getContent());
                savedPost.setImagePath("updated image Path");   // @Todo img 생성 ai 호출로 추푸 변경
                repository.save(savedPost);
                return savedPost.getId();
            }
        } catch (EmptyResultDataAccessException ex) {
            throw new RuntimeException("Post not found for deletion.");
        } catch (DataAccessException ex) {
            // 그 외 데이터베이스 관련 예외 처리
            throw new RuntimeException("Error finding exist post", ex);
        }
        return null;
    }

    public void delete(Long postId) {
        try {
            repository.deleteById(postId);
        } catch (EmptyResultDataAccessException ex) {
            // 삭제할 대상이 없는 경우에 대한 예외 처리
            throw new RuntimeException("Post with ID" + postId + " not found for deletion.");
        } catch (DataAccessException ex) {
            // 그 외 데이터베이스 관련 예외 처리
            throw new RuntimeException("Error deleting user with ID " + postId, ex);
        }
    }

}
