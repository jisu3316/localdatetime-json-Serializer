package com.example.redis.repository;

import com.example.redis.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {


    
    Board findBoardById(Long id);
}
