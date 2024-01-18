package com.daycanvas.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public User findUserById(@AuthenticationPrincipal OAuth2User principal) {
        Long id = principal.getAttribute("user_id");
        try {
            return repository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User with ID " + id + " not found for deletion."));
        } catch (DataAccessException ex) {
            // 데이터베이스 관련 예외 처리
            throw new RuntimeException("Error deleting user with ID " + id, ex);
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
