package com.example.redis.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisService {
    @Autowired
    private RedisTemplate redisTemplate;
    private ValueOperations<String, String> valueOperations;
    private ListOperations<String, Map<String, Object>> listOperations;
    @Autowired
    private BoardService boardService;

    @PostConstruct
    public void init () {
        valueOperations = redisTemplate.opsForValue ();
        listOperations = redisTemplate.opsForList ();
//        boardService.insert();
        deleteAll("list");
//        List<Map<String, Object>> list = boardService.getList();
        List<Map<String, Object>> boards = boardService.getBoards();
        setList("list", boards);
        getStringValue("list");

    }

    public String getStringValue(String key) {
        RedisOperations<String, Object> operations = redisTemplate.opsForList().getOperations();
        List<Object> list = operations.opsForList().range("list", 0, -1);
        System.out.println("list = " + list);

        System.out.println("operations: " + operations.opsForList().range("list", 0, -1));  // Redis Data List 출력
        return null;
    }



    public void deleteAll (String... patterns) {
        for (String pattern : patterns) {
            Set<String> keys = redisTemplate.keys (pattern);
            redisTemplate.delete (keys);
        }
    }

    public void setList (String key, List<Map<String, Object>> list) {
        delete (key);
        listOperations.rightPushAll (key, list);
    }

    public void setList (String key, List<Map<String, Object>> list, long timeout) {
        delete (key);
        listOperations.rightPushAll (key, list);
        listOperations.getOperations ().expire (key, timeout, TimeUnit.SECONDS);
    }

    public boolean delete (String key) {
        return redisTemplate.delete (key);
    }




}
