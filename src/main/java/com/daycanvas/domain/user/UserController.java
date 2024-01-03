package com.daycanvas.domain.user;

import com.daycanvas.dto.login.LoginDto;
import com.daycanvas.dto.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService service;
    private final ModelMapper modelMapper;

    @PostMapping("/join")
    public ResponseEntity<Long> join (UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        Long userId = service.saveUser(user);
        return ResponseEntity.ok(userId);
    }

    @PostMapping("/login")
    public ResponseEntity<Long> login (LoginDto loginDto) {
        Long userId = service.login(loginDto.getEmail(), loginDto.getPassword());
        if (userId != -1){
            return ResponseEntity.ok(userId);
        }
        return ResponseEntity.badRequest().body(-1L);
    }


}
