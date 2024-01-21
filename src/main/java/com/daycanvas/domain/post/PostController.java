package com.daycanvas.domain.post;

import com.daycanvas.dto.post.DayImageMappingDto;
import com.daycanvas.dto.post.MonthlyPostResponseDto;
import com.daycanvas.dto.post.PostRequestDto;
import com.daycanvas.dto.post.PostResponseDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService service;
    private final ModelMapper modelMapper;

    @PostMapping("")
    public String create(@RequestBody PostRequestDto postRequestDto, @AuthenticationPrincipal OAuth2User principal) {
        Post post = modelMapper.map(postRequestDto, Post.class);
        Long postId = service.save(post, principal);
        return "redirect:/posts/" + postId;
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> read(@PathVariable Long postId) {
        Post post = service.findById(postId);
        PostResponseDto postDto = modelMapper.map(post, PostResponseDto.class);
        return ResponseEntity.ok(postDto);
    }

    @GetMapping("")
    public ResponseEntity<MonthlyPostResponseDto> readByMonth(@RequestParam(required = false) Integer year,
                                                              @RequestParam(required = false) Integer month, @AuthenticationPrincipal OAuth2User principal) {
        if (year == null) {
            year = LocalDateTime.now().getYear();
        }
        if (month == null) {
            month = LocalDateTime.now().getMonthValue();
        }
        List<DayImageMappingDto> imagePaths = service.findAllByMonth(year, month, principal);
        MonthlyPostResponseDto postDto = new MonthlyPostResponseDto(imagePaths);
        return ResponseEntity.ok(postDto);
    }

    @GetMapping("/all")
    public ResponseEntity<List<PostResponseDto>> getPostsByUserId(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            // 사용자가 인증되지 않았으므로 에러 핸들링 또는 리다이렉트 등을 수행
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Post> posts = service.findAll(principal);

        List<PostResponseDto> listPosts = posts.stream()
                .map(post -> modelMapper.map(post, PostResponseDto.class))
                .toList();

        return ResponseEntity.ok(listPosts);
    }

    @PatchMapping("")
    public String update(@RequestBody PostRequestDto postRequestDto, @AuthenticationPrincipal OAuth2User principal) {
        Post post = modelMapper.map(postRequestDto, Post.class);
        Long postId = service.update(post, principal);
        return "redirect:/posts/" + postId;
    }

    @DeleteMapping("")
    public String delete(@RequestParam(required = true) Long postId, @AuthenticationPrincipal OAuth2User principal) {
        service.delete(postId, principal);
        return "redirect:/posts?month=" + LocalDateTime.now().getMonthValue();
    }
}
