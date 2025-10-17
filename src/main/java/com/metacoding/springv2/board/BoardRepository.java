package com.metacoding.springv2.board;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Integer> {

    @Query("SELECT b FROM Board b JOIN FETCH b.user LEFT JOIN FETCH b.replies r LEFT JOIN FETCH r.user WHERE b.id = :boardId")
    Optional<Board> findByIdJoinUserAndReplies(@Param("boardId") Integer boardId);

}