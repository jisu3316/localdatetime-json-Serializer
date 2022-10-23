package com.example.redis.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Configuration
@EnableRedisRepositories
public class RedisRepositoryConfig {
    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${spring.redis.password}")
    private String password;

//    @Autowired
//    private JacksonConfig jacksonConfig;


    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(redisHost);
        redisStandaloneConfiguration.setPort(redisPort);
        redisStandaloneConfiguration.setPassword(password);
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer());
//        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        return redisTemplate;
    }


    @Bean
    public Jackson2JsonRedisSerializer jackson2JsonRedisSerializer() {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper());
        return jackson2JsonRedisSerializer;
    }


//    @Bean
//    public ObjectMapper objectMapper() {
//        ObjectMapper mapper = new ObjectMapper();
//        SimpleModule simpleModule = jacksonConfig.jsonMapperJava8DateTimeModule();
//        mapper.registerModule(simpleModule);
////                mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        return mapper;
//    }


    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(LocalDateTime.class, new CustomLocalDateTimeSerializer());
        mapper.registerModule(simpleModule);
//        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

//    @Bean
//    public ObjectMapper objectMapper() {
//        ObjectMapper mapper = new ObjectMapper();
//        SimpleModule module = new SimpleModule();
//        module.addSerializer(LocalDate.class, new JsonSerializer<LocalDate>() {
//            @Override
//            public void serialize(
//                    LocalDate localDate, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
//                    throws IOException {
//                jsonGenerator.writeString(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(localDate));
//            }
//        });
//
//
//        module.addSerializer(LocalTime.class, new JsonSerializer<LocalTime>() {
//            @Override
//            public void serialize(
//                    LocalTime localTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
//                    throws IOException {
//                jsonGenerator.writeString(DateTimeFormatter.ofPattern("kk:mm:ss").format(localTime));
//            }
//        });
//
//
//        module.addSerializer(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
//            @Override
//            public void serialize(
//                    LocalDateTime localDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
//                    throws IOException {
//                jsonGenerator.writeString(DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss").format(localDateTime));
//            }
//        });
//
//        mapper.registerModule(module);
//        return mapper;
//    }
}

