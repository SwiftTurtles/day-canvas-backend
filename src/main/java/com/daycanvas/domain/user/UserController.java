package com.daycanvas.domain.user;

import com.daycanvas.dto.login.LoginDto;
import com.daycanvas.dto.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService service;
    private final ModelMapper modelMapper;

    @PostMapping("/join")
    public ResponseEntity<Long> join (@RequestBody UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        Long userId = service.save(user);
        return ResponseEntity.ok(userId);
    }

    @PostMapping("/login")
    public ResponseEntity<Long> login (@RequestBody  LoginDto loginDto) {
        Long userId = service.login(loginDto.getEmail(), loginDto.getPassword());

        if (userId != null) {
            return ResponseEntity.ok(userId);
        }
        else {
            // 로그인 실패 시에 대한 처리
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam Long userId) {
        service.delete(userId);
        return ResponseEntity.ok("User deleted successfully");
    }
}
