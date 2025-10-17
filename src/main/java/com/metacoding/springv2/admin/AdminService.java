package com.metacoding.springv2.admin;

import org.springframework.stereotype.Service;
import com.metacoding.springv2.board.*;
import com.metacoding.springv2.core.handler.ex.*;
import com.metacoding.springv2.auth.*;
import com.metacoding.springv2.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

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
    public AuthResponse.DTO 관리자_역할수정(Integer userId,UserRequest.RolesDTO requestDTO){
        User findUser = userRepository.findById(userId)
            .orElseThrow(() -> new Exception404("회원을 찾을 수 없습니다"));
    
		// 중복 체크 , SET 는 순서 상관없이 비교 가능
        Set<String> currentRoles = new HashSet<>(Arrays.asList(findUser.getRoles().split(",")));
        Set<String> requestedRoles = new HashSet<>(Arrays.asList(requestDTO.roles().split(",")));       
        if (currentRoles.equals(requestedRoles)) {
            throw new Exception403("동일한 역할로 수정할 수 없습니다");
        }
        findUser.updateRoles(requestDTO.roles());
        return new AuthResponse.DTO(findUser);
    }
}