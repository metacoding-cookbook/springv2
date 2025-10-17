package com.metacoding.springv2.reply;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.metacoding.springv2.board.*;
import com.metacoding.springv2.core.handler.ex.*;
import com.metacoding.springv2.user.User;
import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ReplyService {
    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public ReplyResponse.DTO 댓글쓰기(ReplyRequest.SaveDTO requestDTO, User sessionUser) {
        Board findBoard = boardRepository.findById(requestDTO.boardId())
                .orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다"));
        Reply savedReply = replyRepository.save(requestDTO.toEntity(sessionUser, findBoard));
        return new ReplyResponse.DTO(savedReply, sessionUser);
    }

    @Transactional
    public void 댓글삭제(Integer replyId, Integer sessionUserId) {
        Reply findReply = replyRepository.findById(replyId)
                .orElseThrow(() -> new Exception404("댓글을 찾을 수 없습니다"));
        if (!findReply.getUser().getId().equals(sessionUserId))
            throw new Exception403("댓글을 삭제할 권한이 없습니다");
        replyRepository.deleteById(replyId);
    }
}
