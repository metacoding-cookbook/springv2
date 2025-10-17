package com.metacoding.springv2.reply;

import com.metacoding.springv2.user.User;

public class ReplyResponse {
    public record DTO(
            Integer id,
            String comment,
            Integer userId,
            Integer boardId,
            String username,
            Boolean isOwner) { // 댓글 작성자 체크
        public DTO(Reply reply, User sessionUser) {
            this(
                    reply.getId(),
                    reply.getComment(),
                    reply.getUser().getId(),
                    reply.getBoard().getId(),
                    sessionUser.getUsername(),
                    reply.getUser().getId().equals(sessionUser.getId()));
        }
    }
}