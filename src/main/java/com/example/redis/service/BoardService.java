package com.example.redis.service;

import com.example.redis.dao.BoardDAO;
import com.example.redis.domain.Board;
import com.example.redis.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;

    private final BoardDAO boardDAO;


    public Board home() {
        return boardRepository.findBoardById(1l);
    }

    public List<Map<String, Object>> getList() {
        List<Board> all = boardRepository.findAll();
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String , Object> map = new HashMap<>();
        for (int i  = 0; i< all.size(); i ++) {
            String key = "board" + i;
            map.put(key, all.get(i));
            list.add(map);
        }

        return list;
    }

    public List<Map<String, Object>> getBoards() {
        List<Map<String, Object>> boards = boardDAO.boards();
        return boards;
    }

    @Transactional
    public void insert() {
        boardRepository.save(new Board("제목", "내용"));
    }
}
