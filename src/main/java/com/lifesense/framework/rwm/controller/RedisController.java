package com.lifesense.framework.rwm.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
public class RedisController {
    @Value("${spring.redis.host}")
    private String redisHostDefault;
    @Value("${spring.redis.host.1}")
    private String redisHostQA;
    @Value("${spring.redis.host.2}")
    private String redisHostQA2;
    String redisHost;

    String envChoice(String env) {
        switch (env) {
            case "qa":
                return redisHostQA;
            case "qa2":
                return redisHostQA2;
            default:
                return redisHostDefault;
        }
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    String info(@RequestParam(value = "env", defaultValue = "qa2") String env) {
        redisHost = envChoice(env);
        Jedis jedis = new Jedis(redisHost);
        return jedis.info();
    }

    @RequestMapping(value = "/keys", method = RequestMethod.GET)
    List<String> keys(@RequestParam(value = "keyword", defaultValue = "*") String keyword, @RequestParam(value = "env", defaultValue = "qa2") String env) {
        redisHost = envChoice(env);
        Jedis jedis = new Jedis(redisHost);
        Set<String> redisKeys = jedis.keys(keyword);
        List<String> keysList = new ArrayList<>(redisKeys);
        return keysList;
    }

    @RequestMapping(value = "/type", method = RequestMethod.GET)
    String type(@RequestParam(value = "key") String key, @RequestParam(value = "env", defaultValue = "qa2") String env) {
        redisHost = envChoice(env);
        Jedis jedis = new Jedis(redisHost);
        String value = jedis.type(key);
        return key + ": \n  type: " + value;
    }

    @RequestMapping(value = "/del", method = RequestMethod.GET)
    String del(@RequestParam(value = "key") String key, @RequestParam(value = "env", defaultValue = "qa2") String env) {
        redisHost = envChoice(env);
        Jedis jedis = new Jedis(redisHost);
        jedis.del(key);
        String value = jedis.get(key);
        return key + ": " + value;
    }

    @RequestMapping(value = "/set", method = RequestMethod.GET)
    String set(@RequestParam(value = "key") String key, @RequestParam(value="value") String value, @RequestParam(value = "env", defaultValue = "qa2") String env) {
        redisHost = envChoice(env);
        Jedis jedis = new Jedis(redisHost);
        jedis.set(key, value);
        return key + ": " + value;
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    String get(@RequestParam(value = "key", defaultValue = "foo") String key, @RequestParam(value = "env", defaultValue = "qa2") String env) {
        redisHost = envChoice(env);
        Jedis jedis = new Jedis(redisHost);
        String value = jedis.get(key);
        return key + ": " + value;
    }

    @RequestMapping(value = "/hgetall", method = RequestMethod.GET)
    String hgetall(@RequestParam(value = "key") String key, @RequestParam(value = "env", defaultValue = "qa2") String env) {
        redisHost = envChoice(env);
        Jedis jedis = new Jedis(redisHost);
        Map<String, String> value = jedis.hgetAll(key);
        return key + ": \n  " + value;
    }

    @RequestMapping(value = "/lrange", method = RequestMethod.GET)
    String lrange(@RequestParam(value = "key") String key, @RequestParam(value = "env", defaultValue = "qa2") String env) {
        redisHost = envChoice(env);
        Jedis jedis = new Jedis(redisHost);
        long keyLen = jedis.llen(key);
        List value = jedis.lrange(key, 0, keyLen);
        return key + ": \n  " + value;
    }

    @RequestMapping(value = "/smembers", method = RequestMethod.GET)
    String smembers(@RequestParam(value = "key") String key, @RequestParam(value = "env", defaultValue = "qa2") String env) {
        redisHost = envChoice(env);
        Jedis jedis = new Jedis(redisHost);
        Set value = jedis.smembers(key);
        return key + ": \n  " + value;
    }

    @RequestMapping(value = "/zrange", method = RequestMethod.GET)
    String zrange(@RequestParam(value = "key") String key, @RequestParam(value = "env", defaultValue = "qa2") String env) {
        redisHost = envChoice(env);
        Jedis jedis = new Jedis(redisHost);
        Set value = jedis.zrange(key, 0, -1);
        return key + ": \n  " + value;
    }
}