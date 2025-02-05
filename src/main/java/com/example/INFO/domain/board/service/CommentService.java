package com.example.INFO.domain.board.service;

import com.example.INFO.domain.board.domain.Board;
import com.example.INFO.domain.board.domain.Comment;
import com.example.INFO.domain.board.domain.repository.BoardRepository;
import com.example.INFO.domain.board.domain.repository.CommentRepository;
import com.example.INFO.domain.board.dto.req.CommentCreateRequest;
import com.example.INFO.domain.board.dto.res.CommentResponse;
import com.example.INFO.domain.user.model.entity.UserEntity;
import com.example.INFO.domain.user.repository.UserRepository;
import com.example.INFO.global.exception.NotFoundException;
import com.example.INFO.global.exception.UnauthorizedException;
import com.example.INFO.global.payload.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    // 댓글 생성
    @Transactional
    public CommentResponse createComment(Long userId, CommentCreateRequest request) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND.getCode(), "User not found"));
        Board board = boardRepository.findById(request.getBoardId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND.getCode(), "Board not found"));

        Comment parent = null;
        if (request.getParentId() != null) {
            parent = commentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND.getCode(),"Parent comment not found"));
        }

        Comment comment = Comment.builder()
                .board(board)
                .user(user)
                .content(request.getContent())
                .parent(parent)
                .build();

        Comment savedComment = commentRepository.save(comment);
        return mapToResponse(savedComment);
    }

    // 대댓글 생성
    @Transactional
    public CommentResponse createReply(Long userId, Long parentId, String content) {
        Comment parent = commentRepository.findById(parentId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND.getCode(), "Parent comment not found"));

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND.getCode(), "User not found"));

        Comment reply = Comment.builder()
                .board(parent.getBoard())
                .user(user)
                .content(content)
                .parent(parent)
                .build();

        Comment savedReply = commentRepository.save(reply);
        return mapToResponse(savedReply);
    }
    // 게시글의 모든 댓글 조회
    @Transactional(readOnly = true) // 트랜잭션 유지
    public List<CommentResponse> getCommentsWithReplies(Long boardId) {
        List<Comment> parentComments = commentRepository.findByBoardIdAndParentIsNull(boardId);
        return parentComments.stream()
                .map(this::mapToResponseWithReplies)
                .collect(Collectors.toList());
    }
    // 댓글 수정
    @Transactional
    public CommentResponse updateComment(Long userId, Long commentId, String content) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND.getCode(), "Comment not found"));

        if (!comment.getUser().getId().equals(userId)) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED.getCode(), "User not authorized to update this comment");
        }

        comment.updateContent(content);
        return mapToResponse(comment);
    }
    // Comment -> CommentResponse 매핑
    private CommentResponse mapToResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .boardId(comment.getBoard().getId())
                .userId(comment.getUser().getId())
                .content(comment.getContent())
                .createdTime(comment.getCreatedTime())
                .updatedTime(comment.getUpdatedTime())
                .likeCount(comment.getLikeCount())
                .replies(comment.getReplies() == null ?
                        new ArrayList<>() :
                        comment.getReplies().stream().map(this::mapToResponse).collect(Collectors.toList()))
                .build();
    }
    // 댓글과 대댓글 응답 매핑
    private CommentResponse mapToResponseWithReplies(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .boardId(comment.getBoard().getId())
                .userId(comment.getUser().getId())
                .content(comment.getContent())
                .createdTime(comment.getCreatedTime())
                .updatedTime(comment.getUpdatedTime())
                .likeCount(comment.getLikeCount())
                .replies(comment.getReplies().stream()
                        .map(this::mapToResponseWithReplies)
                        .collect(Collectors.toList()))
                .build();
    }
}
