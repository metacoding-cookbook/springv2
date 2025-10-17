package com.metacoding.springv2.reply;

import com.metacoding.springv2.board.Board;
import com.metacoding.springv2.user.User;
import jakarta.validation.constraints.*;

public class ReplyRequest {
    public record SaveDTO(
            @Size(min = 1, max = 100, message = "댓글은 1자 이상 100자 이하로 입력해주세요") 
            String comment,
            Integer boardId
            ) {
        public Reply toEntity(User user, Board board) {
            return Reply.builder()
                    .comment(comment)
                    .user(user)
                    .board(board)
                    .build();
        }
    }
}