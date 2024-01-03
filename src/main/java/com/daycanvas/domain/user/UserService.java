package com.daycanvas.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public Long saveUser(User user) {
        return repository.save(user).getId();
    }

    public Long login(String email, String password) {
        // @Todo 추후 변경
        User user = repository.findByEmail(email).get();
        if (user.getPassword().equals(password)) {
            return user.getId();
        }
        else {
            return -1L;
        }
    }

}
