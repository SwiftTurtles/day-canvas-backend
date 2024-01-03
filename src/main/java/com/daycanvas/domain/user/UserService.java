package com.daycanvas.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public Long save(User user) {
        return repository.save(user).getId();
    }

    public Long login(String email, String password) {
        // @Todo 추후 변경
        Optional<User> userOptional = repository.findByEmail(email);

        if(userOptional.isPresent()) {
            User user = userOptional.get();

            try {
                if (user.getPassword().equals(password)) {
                    return user.getId();
                }
                else {
                    return null;
                }
            } catch (NullPointerException e) {
                return null;
            }
        }
        else {
            return null;
        }
    }

    public void delete(Long userId) {
        try {
            repository.deleteById(userId);
        } catch (EmptyResultDataAccessException ex) {
            // 삭제할 대상이 없는 경우에 대한 예외 처리
            throw new RuntimeException("User with ID " + userId + " not found for deletion.");
        } catch (DataAccessException ex) {
            // 그 외 데이터베이스 관련 예외 처리
            throw new RuntimeException("Error deleting user with ID " + userId, ex);
        }
    }

}
