package com.lifesense.framework.rwm.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

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
    @Value("${spring.redis.host.3}")
    private String redisHostOnline;
    String redisHost;

    @Value("${spring.redis.port}")
    private String redisPortDefault;
    @Value("${spring.redis.port.1}")
    private String redisPortQA;
    @Value("${spring.redis.port.2}")
    private String redisPortQA2;
    @Value("${spring.redis.port.3}")
    private String redisPortOnline;
    String redisPort;

    String[] envChoice(String env) {
        switch (env) {
            case "qa":
                return new String[] {redisHostQA, redisPortQA};
            case "qa2":
                return new String[] {redisHostQA2, redisPortQA2};
            case "online":
                return new String[] {redisHostOnline, redisPortOnline};
            default:
                return new String[] {redisHostDefault, redisPortDefault};
        }
    }

    @RequestMapping(value = "/redis/info", method = RequestMethod.GET)
    String info(@RequestParam(value = "env", defaultValue = "qa2") String env) {
        redisHost = envChoice(env)[0];
        redisPort = envChoice(env)[1];
        if (env.equals("online")) {
            JedisCluster jedis = new JedisCluster(new HostAndPort(redisHost, Integer.valueOf(redisPort)));
            return jedis.info();
        }
        Jedis jedis = new Jedis(redisHost, Integer.valueOf(redisPort));
        return jedis.info();
    }

    @RequestMapping(value = "/redis/keys", method = RequestMethod.GET)
    List<String> keys(@RequestParam(value = "keyword", defaultValue = "*foo*") String keyword, @RequestParam(value = "env", defaultValue = "qa2") String env) {
        redisHost = envChoice(env)[0];
        redisPort = envChoice(env)[1];
        if (env.equals("online")) {
            List ret = new ArrayList<String>();
            ret.add("不支持的操作！！！");
            return ret;
        }
        Jedis jedis = new Jedis(redisHost, Integer.valueOf(redisPort));
        Set<String> redisKeys = jedis.keys(keyword);
        List<String> keysList = new ArrayList<>(redisKeys);
        return keysList;
    }

    @RequestMapping(value = "/redis/type", method = RequestMethod.GET)
    String type(@RequestParam(value = "key") String key, @RequestParam(value = "env", defaultValue = "qa2") String env) {
        redisHost = envChoice(env)[0];
        redisPort = envChoice(env)[1];
        if (env.equals("online")) {
            JedisCluster jedis = new JedisCluster(new HostAndPort(redisHost, Integer.valueOf(redisPort)));
            String value = jedis.type(key);
            return key + ": \n  type: " + value;
        }
        Jedis jedis = new Jedis(redisHost, Integer.valueOf(redisPort));
        String value = jedis.type(key);
        return key + ": \n  type: " + value;
    }

    @RequestMapping(value = "/redis/del", method = RequestMethod.GET)
    String del(@RequestParam(value = "key") String key, @RequestParam(value = "env", defaultValue = "qa2") String env) {
        redisHost = envChoice(env)[0];
        redisPort = envChoice(env)[1];
        if (env.equals("online")) {
            return "Online 环境禁止 del 操作！！！";
        }
        Jedis jedis = new Jedis(redisHost, Integer.valueOf(redisPort));
        jedis.del(key);
        String value = jedis.get(key);
        return key + ": " + value;
    }

    @RequestMapping(value = "/redis/set", method = RequestMethod.GET)
    String set(@RequestParam(value = "key") String key, @RequestParam(value="value") String value, @RequestParam(value = "env", defaultValue = "qa2") String env) {
        redisHost = envChoice(env)[0];
        redisPort = envChoice(env)[1];
        if (env.equals("online")) {
            JedisCluster jedis = new JedisCluster(new HostAndPort(redisHost, Integer.valueOf(redisPort)));
            jedis.set(key, value);
            return key + ": " + value;
        }
        Jedis jedis = new Jedis(redisHost, Integer.valueOf(redisPort));
        jedis.set(key, value);
        return key + ": " + value;
    }

    @RequestMapping(value = "/redis/get", method = RequestMethod.GET)
    String get(@RequestParam(value = "key", defaultValue = "foo") String key, @RequestParam(value = "env", defaultValue = "qa2") String env) {
        redisHost = envChoice(env)[0];
        redisPort = envChoice(env)[1];
        if (env.equals("online")) {
            JedisCluster jedis = new JedisCluster(new HostAndPort(redisHost, Integer.valueOf(redisPort)));
            String value = jedis.get(key);
            return key + ": " + value;
        }
        Jedis jedis = new Jedis(redisHost, Integer.valueOf(redisPort));
        String value = jedis.get(key);
        return key + ": " + value;
    }

    @RequestMapping(value = "/redis/hgetall", method = RequestMethod.GET)
    String hgetall(@RequestParam(value = "key") String key, @RequestParam(value = "env", defaultValue = "qa2") String env) {
        redisHost = envChoice(env)[0];
        redisPort = envChoice(env)[1];
        if (env.equals("online")) {
            JedisCluster jedis = new JedisCluster(new HostAndPort(redisHost, Integer.valueOf(redisPort)));
            Map<String, String> value = jedis.hgetAll(key);
            return key + ": \n  " + value;
        }
        Jedis jedis = new Jedis(redisHost, Integer.valueOf(redisPort));
        Map<String, String> value = jedis.hgetAll(key);
        return key + ": \n  " + value;
    }

    @RequestMapping(value = "/redis/lrange", method = RequestMethod.GET)
    String lrange(@RequestParam(value = "key") String key, @RequestParam(value = "env", defaultValue = "qa2") String env) {
        redisHost = envChoice(env)[0];
        redisPort = envChoice(env)[1];
        if (env.equals("online")) {
            JedisCluster jedis = new JedisCluster(new HostAndPort(redisHost, Integer.valueOf(redisPort)));
            long keyLen = jedis.llen(key);
            List value = jedis.lrange(key, 0, keyLen);
            return key + ": \n  " + value;
        }
        Jedis jedis = new Jedis(redisHost, Integer.valueOf(redisPort));
        long keyLen = jedis.llen(key);
        List value = jedis.lrange(key, 0, keyLen);
        return key + ": \n  " + value;
    }

    @RequestMapping(value = "/redis/smembers", method = RequestMethod.GET)
    String smembers(@RequestParam(value = "key") String key, @RequestParam(value = "env", defaultValue = "qa2") String env) {
        redisHost = envChoice(env)[0];
        redisPort = envChoice(env)[1];
        if (env.equals("online")) {
            JedisCluster jedis = new JedisCluster(new HostAndPort(redisHost, Integer.valueOf(redisPort)));
            Set value = jedis.smembers(key);
            return key + ": \n  " + value;
        }
        Jedis jedis = new Jedis(redisHost, Integer.valueOf(redisPort));
        Set value = jedis.smembers(key);
        return key + ": \n  " + value;
    }

    @RequestMapping(value = "/redis/zrange", method = RequestMethod.GET)
    String zrange(@RequestParam(value = "key") String key, @RequestParam(value = "env", defaultValue = "qa2") String env) {
        redisHost = envChoice(env)[0];
        redisPort = envChoice(env)[1];
        if (env.equals("online")) {
            JedisCluster jedis = new JedisCluster(new HostAndPort(redisHost, Integer.valueOf(redisPort)));
            Set value = jedis.zrange(key, 0, -1);
            return key + ": \n  " + value;
        }
        Jedis jedis = new Jedis(redisHost, Integer.valueOf(redisPort));
        Set value = jedis.zrange(key, 0, -1);
        return key + ": \n  " + value;
    }
}