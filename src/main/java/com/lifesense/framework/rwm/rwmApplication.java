package com.lifesense.framework.rwm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


@RestController
@EnableSwagger2
@EnableAutoConfiguration
@EnableGlobalMethodSecurity
@Configuration
@PropertySource("redis.properties")
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
        return "Hello World!";
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    String info() {
        Jedis jedis = new Jedis(redisHost);
        String value = jedis.info();
        return value;
    }

    @RequestMapping(value = "/keys/{keyword}", method = RequestMethod.GET)
    List<String> keys(@PathVariable String keyword) {
        Jedis jedis = new Jedis(redisHost);
        Set<String> redisKeys = jedis.keys(keyword);
        List<String> keysList = new ArrayList<>();
        Iterator<String> it = redisKeys.iterator();
        while (it.hasNext()) {
            String data = it.next();
            keysList.add(data);
        }
        return keysList;
    }

    @RequestMapping(value = "/type/{key}", method = RequestMethod.GET)
    String type(@PathVariable String key) {
        Jedis jedis = new Jedis(redisHost);
        String value = jedis.type(key);
        return "type: " + value;
    }

    @RequestMapping(value = "/get/{key}", method = RequestMethod.GET)
    String get(@PathVariable String key) {
        Jedis jedis = new Jedis(redisHost);
        String value = jedis.get(key);
        return key + ": " + value;
    }

    @RequestMapping(value = "/del/{key}", method = RequestMethod.GET)
    String del(@PathVariable String key) {
        Jedis jedis = new Jedis(redisHost);
        jedis.del(key);
        String value = jedis.get(key);
        return key + ": " + value;
    }

    @RequestMapping(value = "/set/{key}/{value}", method = RequestMethod.GET)
    String set(@PathVariable String key, @PathVariable String value) {
        Jedis jedis = new Jedis(redisHost);
        jedis.set(key, value);
        return key + ": " + value;
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(rwmApplication.class, args);
    }
}