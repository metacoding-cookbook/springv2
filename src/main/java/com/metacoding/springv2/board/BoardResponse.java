package com.metacoding.springv2.board;

import java.util.List;
import com.metacoding.springv2.reply.Reply;

public class BoardResponse {
    public record DTO(
            Integer id,
            String title,
            String content) {
        public DTO(Board board) {
            this(
                    board.getId(),
                    board.getTitle(),
                    board.getContent());
        }
    }  

    public record DetailDTO(
        Integer boardId,
        String title,
        String content,
        Integer userId,
        String username,
        Boolean isOwner, // 게시글 작성자 여부 체크
        List<ReplyDTO> replies) {
    
        public DetailDTO(Board board, Integer sessionUserId) {
            this(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getUser().getId(),
                board.getUser().getUsername(),
                sessionUserId.equals(board.getUser().getId()),
                board.getReplies().stream()
                        .map(reply -> new ReplyDTO(reply, sessionUserId))
                        .toList());
        }
    
        public record ReplyDTO(
            Integer id,
            String username,
            String comment,
            Boolean isOwner) { // 댓글 작성자 여부 체크
        
            public ReplyDTO(Reply reply, Integer sessionUserId) {
                this(
                    reply.getId(),
                    reply.getUser().getUsername(),
                    reply.getComment(),
                    sessionUserId.equals(reply.getUser().getId()));
            }
        }
    }
}
