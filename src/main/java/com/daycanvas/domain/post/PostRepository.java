package com.daycanvas.domain.post;

import com.daycanvas.dto.post.DayImageMappingDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT new com.daycanvas.dto.post.DayImageMappingDto(DAY(p.writtenDate), p.imagePath) " +
            "FROM Post p WHERE YEAR(p.writtenDate) = :year AND MONTH(p.writtenDate) = :month")
    List<DayImageMappingDto> findAllByMonthAndYear(@Param("year") int year, @Param("month") int month);

    /**
     * @param userId Post-user_id
     * @return Post 테이블의 user_id 필드로 조회 후 반환
     */
    List<Post> findByUserId(Long userId);
}
