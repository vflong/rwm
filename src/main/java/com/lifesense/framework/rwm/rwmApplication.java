package com.lifesense.framework.rwm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.*;


@RestController
@EnableSwagger2
@EnableAutoConfiguration
@EnableGlobalMethodSecurity
public class rwmApplication {
    @Value("${spring.redis.host}")
    private String redisHost;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    String home() {
        return "Redis Web Manager!";
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    String info() {
        Jedis jedis = new Jedis(redisHost);
        return jedis.info();
    }

    @RequestMapping(value = "/keys", method = RequestMethod.GET)
    List<String> keys(@RequestParam(value = "keyword", defaultValue = "*") String keyword) {
        Jedis jedis = new Jedis(redisHost);
        Set<String> redisKeys = jedis.keys(keyword);
        List<String> keysList = new ArrayList<>();
        keysList.addAll(redisKeys);
        return keysList;
    }

    @RequestMapping(value = "/type", method = RequestMethod.GET)
    String type(@RequestParam(value = "key") String key) {
        Jedis jedis = new Jedis(redisHost);
        String value = jedis.type(key);
        return key + ": \n  type: " + value;
    }

    @RequestMapping(value = "/del", method = RequestMethod.GET)
    String del(@RequestParam(value = "key") String key) {
        Jedis jedis = new Jedis(redisHost);
        jedis.del(key);
        String value = jedis.get(key);
        return key + ": " + value;
    }

    @RequestMapping(value = "/set", method = RequestMethod.GET)
    String set(@RequestParam(value = "key") String key, @RequestParam(value="value") String value) {
        Jedis jedis = new Jedis(redisHost);
        jedis.set(key, value);
        return key + ": " + value;
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    String get(@RequestParam(value = "key", defaultValue = "foo") String key) {
        Jedis jedis = new Jedis(redisHost);
        String value = jedis.get(key);
        return key + ": " + value;
    }

    @RequestMapping(value = "/hgetall", method = RequestMethod.GET)
    String hgetall(@RequestParam(value = "key") String key) {
        Jedis jedis = new Jedis(redisHost);
        Map<String, String> value = jedis.hgetAll(key);
        return key + ": \n  " + value;
    }

    @RequestMapping(value = "/lrange", method = RequestMethod.GET)
    String lrange(@RequestParam(value = "key") String key) {
        Jedis jedis = new Jedis(redisHost);
        long keyLen = jedis.llen(key);
        List value = jedis.lrange(key, 0, keyLen);
        return key + ": \n  " + value;
    }

    @RequestMapping(value = "/smembers", method = RequestMethod.GET)
    String smembers(@RequestParam(value = "key") String key) {
        Jedis jedis = new Jedis(redisHost);
        Set value = jedis.smembers(key);
        return key + ": \n  " + value;
    }

    @RequestMapping(value = "/zrange", method = RequestMethod.GET)
    String zrange(@RequestParam(value = "key") String key) {
        Jedis jedis = new Jedis(redisHost);
        Set value = jedis.zrange(key, 0, -1);
        return key + ": \n  " + value;
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(rwmApplication.class, args);
    }
}