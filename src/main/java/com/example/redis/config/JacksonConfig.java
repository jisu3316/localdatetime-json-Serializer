package com.example.redis.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonConfig {
    @Bean
    public SimpleModule jsonMapperJava8DateTimeModule() {
        SimpleModule module = new SimpleModule();

        module.addSerializer(LocalDate.class, new JsonSerializer<LocalDate>() {
            @Override
            public void serialize(
                    LocalDate localDate, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
                    throws IOException {
                jsonGenerator.writeString(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(localDate));
            }
        });


        module.addSerializer(LocalTime.class, new JsonSerializer<LocalTime>() {
            @Override
            public void serialize(
                    LocalTime localTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
                    throws IOException {
                jsonGenerator.writeString(DateTimeFormatter.ofPattern("kk:mm:ss").format(localTime));
            }
        });


        module.addSerializer(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
            @Override
            public void serialize(
                    LocalDateTime localDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
                    throws IOException {
                jsonGenerator.writeString(DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss").format(localDateTime));
            }
        });

        return module;
    }
}