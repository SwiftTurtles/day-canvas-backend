package com.daycanvas.domain.post;

import javax.persistence.*;

import com.daycanvas.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
@Table(name = "post")
@Getter @Setter @NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, insertable = false, updatable = false)
    private Long id;

    @CreationTimestamp
    @Column(name = "written_date", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime writtenDate;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "image_path", nullable = false)
    private String imagePath;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public void setUser(User user) {
        this.user = user;
        if (user != null) {
            user.getPosts().add(this);
        }
    }
}
