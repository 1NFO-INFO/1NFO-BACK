package com.example.INFO.domain.board.domain;

import com.example.INFO.domain.user.model.entity.UserEntity;
import com.example.INFO.global.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id", nullable = false, unique = true)
    private Long id;

    @NotNull
    @Size(min = 1, max = 50)
    private String title;

    @Lob
    private String content;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Category category_name;

    private String post_image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    public void update(String title, String content, Category categoryName, String postImage) {
        if (title != null) {
            this.title = title;
        }
        if (content != null) {
            this.content = content;
        }
        if (categoryName != null) {
            this.category_name = categoryName;
        }
        if (postImage != null) {
            this.post_image = postImage;
        }
    }

    public int getLikeCount() {
        return likes == null ? 0 : likes.size();
    }
}
