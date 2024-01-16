package com.daycanvas.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService service;

    @GetMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam Long userId) {
        service.delete(userId);
        return ResponseEntity.ok("User deleted successfully");
    }
}
