package com.ardkyer.rion.service;

import com.ardkyer.rion.entity.*;
import com.ardkyer.rion.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@Transactional
public class ReplyService {
    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private NotificationService notificationService;

    public Reply addReply(Long commentId, String content, User user) {
        Comment parentComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        Reply reply = new Reply();
        reply.setParentComment(parentComment);
        reply.setContent(content);
        reply.setUser(user);
        reply.setCreatedAt(LocalDateTime.now());

        Reply savedReply = replyRepository.save(reply);

        // 댓글 작성자에게 알림 생성
        notificationService.createReplyNotification(parentComment.getUser(), savedReply);

        return savedReply;
    }
}