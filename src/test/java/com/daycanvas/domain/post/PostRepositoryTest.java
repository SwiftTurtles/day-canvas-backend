package com.daycanvas.domain.post;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Test
    public void saveAndFindPost() {
        // Given
        Post post = new Post();
        post.setContent("Test content");
        post.setImagePath("/images/test.jpg");
        post.setWriteDate(LocalDateTime.now());

        // When
        postRepository.save(post);

        // Then
        assertNotNull(post.getId(), "Post ID should not be null after saving");

        // saved post from the database
        Post savedPost = postRepository.findById(post.getId()).orElse(null);
        assertNotNull(savedPost, "Saved post should not be null");

        assertThat(post.getContent()).isEqualTo(savedPost.getContent());
        assertThat(post.getImagePath()).isEqualTo(savedPost.getImagePath());
    }

}

