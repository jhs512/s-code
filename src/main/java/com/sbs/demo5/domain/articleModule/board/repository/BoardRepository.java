package com.sbs.demo5.domain.articleModule.board.repository;


import com.sbs.demo5.domain.articleModule.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Optional<Board> findByCode(String boardCode);
}
