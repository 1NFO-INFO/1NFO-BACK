package com.example.INFO.domain.board.domain;


import com.example.INFO.domain.user.model.entity.UserEntity;
import com.example.INFO.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false, unique = true)
    private Long Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false) // Board와 연관관계 설정
    private Board board; // 댓글이 속한 게시글

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // User와 연관관계 설정
    private UserEntity user; // 댓글 작성자

    @Column(nullable = false)
    private String content; // 댓글 내용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> replies = new ArrayList<>();

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>(); // 좋아요와의 연관관계

    // 댓글의 좋아요 개수 반환
    public int getLikeCount() {
        return likes.size();
    }
    // content 필드 변경 메서드
    public Comment updateContent(String newContent) {
        this.content = newContent;
        return this;
    }

}
