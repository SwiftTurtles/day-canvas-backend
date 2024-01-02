package com.daycanvas.domain.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void saveAndFindUser() {
        // Given
        User user = new User();
        user.setEmail("test@gmail.com");
        user.setNickname("tester");
        user.setPassword("#1234");

        // When
        userRepository.save(user);

        // Then
        assertNotNull(user.getId(), "User Id should not be null after saving");

        // saved user from database
        User savedUser = userRepository.findById(user.getId()).orElse(null);
        assertNotNull(savedUser, "Saved user should not be null");

        assertThat(user.getNickname()).isEqualTo(savedUser.getNickname());
        assertThat(user.getEmail()).isEqualTo(savedUser.getEmail());
        assertThat(user.getPassword()).isEqualTo(savedUser.getPassword());
    }
}
