package com.daycanvas.domain.post;

import com.daycanvas.domain.user.User;
import com.daycanvas.dto.post.MonthlyPostResponseDto;
import com.daycanvas.dto.post.PostRequestDto;
import com.daycanvas.dto.post.PostResponseDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService service;
    private final ModelMapper modelMapper;

    @PostMapping("/")
    public String create(@RequestBody PostRequestDto postRequestDto) {
        Post post = modelMapper.map(postRequestDto, Post.class);
        Long postId = service.save(post);
        return "redirect:/posts/" + postId;
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> read(@PathVariable int postId) {
        Post post = service.findById(postId);
        PostResponseDto postDto = modelMapper.map(post, PostResponseDto.class);
        return ResponseEntity.ok(postDto);
    }

    @GetMapping("/")
    public ResponseEntity<MonthlyPostResponseDto> readByMonth(@RequestParam int month) {
        List<String> imagePaths = service.findAllByMonth(month);
        MonthlyPostResponseDto postDto = new MonthlyPostResponseDto(imagePaths);
        return ResponseEntity.ok(postDto);
    }

    @PatchMapping("/")
    public String update(@RequestBody PostRequestDto postRequestDto) {
        Post post = modelMapper.map(postRequestDto, Post.class);
        Long postId = service.update(post);
        return "redirect:/posts/" + postId;
    }

    @DeleteMapping("/")
    public String delete(@RequestBody PostRequestDto postRequestDto) {
        service.delete(postRequestDto.getId());
        return "redirect:/posts?month=" + LocalDateTime.now().getMonthValue();
    }
}
