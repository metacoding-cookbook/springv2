package com.metacoding.springv2.admin;

import org.springframework.stereotype.Service;

import com.metacoding.springv2.board.*;
import com.metacoding.springv2.core.handler.ex.*;
import com.metacoding.springv2.auth.*;
import com.metacoding.springv2.user.*;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Transactional
    public void 관리자_게시글삭제(Integer boardId) {
        boardRepository.findById(boardId)
                .orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다"));
        boardRepository.deleteById(boardId);
    }

    @Transactional
    public AuthResponse.DTO 관리자_역할수정(Integer userId,AuthRequest.RolesDTO requestDTO){
        User findUser = userRepository.findById(userId)
        .orElseThrow(() -> new Exception404("회원을 찾을 수 없습니다"));
        if(findUser.getRoles().equals(requestDTO.roles())){
            throw new Exception403("동일한 역할로 수정할 수 없습니다");
        }
       findUser.updateRoles(requestDTO.roles());
       return new AuthResponse.DTO(findUser);
    }
}