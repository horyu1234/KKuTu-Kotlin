package com.horyu1234.kkutu_game;

import com.google.gson.Gson;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

@SpringBootApplication
public class KKutuGameApplication {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(KKutuGameApplication.class, args);
    }

    @Bean
    public Gson getGson() {
        return new Gson();
    }
}
