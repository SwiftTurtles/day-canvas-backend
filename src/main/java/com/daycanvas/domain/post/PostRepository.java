package com.daycanvas.domain.post;

import com.daycanvas.dto.post.DayImageMappingDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * @param userId    Post-user_id
     * @param year      조회하려는 연도
     * @param month     조회하려는 월
     * @return          user_id, 연도, 월에 해당하는 day, imagePath 반환
     */
    @Query("SELECT new com.daycanvas.dto.post.DayImageMappingDto(DAY(p.writtenDate), p.imagePath) " +
            "FROM Post p WHERE p.user.id = :userId AND YEAR(p.writtenDate) = :year AND MONTH(p.writtenDate) = :month")
    List<DayImageMappingDto> findAllByMonthAndYear(@Param("userId") Long userId, @Param("year") int year, @Param("month") int month);

    /**
     * @param userId Post-user_id
     * @return Post 테이블의 user_id 필드로 조회 후 반환
     */
    List<Post> findByUserId(Long userId);
}
