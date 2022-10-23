# ObjectMapper-LocalDateTime-JSON-Serializer

자바 8 이상의 버전에서 LocalDateTime 타입의 형태를 JSON으로 직렬화 후 레디스와 연동하는 연습 프로젝트.

스프링부트 2.5.xx 이상의 버전부터는 DB에서 datetime 타입의 컬럼 값을 가져 올때 자바의 LocalDateTime 타입으로 가져온다.
그 이후의 다시 JSON으로 직렬화할때 문제가 생긴다.

dto를 사용하면 @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") 등 어노테이션으로 String으로 변환해 해결 가능하지만
mybatis 같은 sqlmapper를 사용시 dto를 사용하지 않고 map으로 받아 온다면 포맷팅을 할 때 까다로워집니다.

위와 같은 것들을 Custom Serializer를 구현하면 해결 할 수 있다.

com.example.redis.config.CustomLocalDateTimeSerializer

```java
public class CustomLocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

    private static DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeString(formatter.format(value));
    }
}
```

위와 같이 설정 하고 ObjectMapper를 다음과 같이 설명해주면 의도한대로 직렬화되고 에러도 발생되지 않는다.

```java
    @Bean
public ObjectMapper objectMapper(){
        ObjectMapper mapper=new ObjectMapper();
        SimpleModule simpleModule=new SimpleModule();
        simpleModule.addSerializer(LocalDateTime.class,new CustomLocalDateTimeSerializer());
        mapper.registerModule(simpleModule);
        return mapper;
        }

```

package com.example.redis.config.RedisRepositoryConfig

```java
    @Bean
public RedisTemplate<String, Object> redisTemplate(){
        RedisTemplate<String, Object> redisTemplate=new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer());
        return redisTemplate;
        }


@Bean
public Jackson2JsonRedisSerializer jackson2JsonRedisSerializer(){
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer=new Jackson2JsonRedisSerializer<>(Object.class);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper());
        return jackson2JsonRedisSerializer;
        }
```

이제 직렬화한것을 레디스에 넣어야 하기 떄문에 ObjectMapper를 Jackson2JsonRedisSerializer의
setObjectMapper의 넣어주고 RedisTemplate의 setValueSerializer 의 jackson2JsonRedisSerializer()를 등록해 준다.

## 결과

com.example.redis.service.RedisService

```java
    @PostConstruct
public void init(){
        valueOperations=redisTemplate.opsForValue();
        listOperations=redisTemplate.opsForList();
        deleteAll("list");
        List<Map<String, Object>>boards=boardService.getBoards();
        setList("list",boards);
        getStringValue("list");
        }

public void setList(String key,List<Map<String, Object>>list){
        delete(key);
        listOperations.rightPushAll(key,list);
        }

public String getStringValue(String key){
        RedisOperations<String, Object> operations=redisTemplate.opsForList().getOperations();
        List<Object> list=operations.opsForList().range("list",0,-1);
        System.out.println("list = "+list);
        return null;
        }
```
### list = [{id=1, create_at=2022-10-23 20:25:17, title=제목, content=내용}, {id=2, create_at=2022-10-23 20:26:06, title=제목, content=내용}]


## 개발 환경

* Intellij IDEA Ultimate 2022.1.3
* Java 11
* Spring Boot 2.7.4

## 기술 세부 스택

Spring Boot

* Spring Data JPA
* Mybatis
* MySQL Driver
* Lombok
* Redis

참고 링크

* https://github.com/HomoEfficio/dev-tips/blob/master/Java8-LocalDateTime-Jackson-%EC%A7%81%EB%A0%AC%ED%99%94-%EB%AC%B8%EC%A0%9C.md
* https://docs.microsoft.com/ko-kr/dotnet/api/system.text.json.jsonserializer?view=net-6.0
* https://perfectacle.github.io/2018/01/16/jackson-local-date-time-serialize/
