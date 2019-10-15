package weed3demo.config.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;

/**
 * Redis 使用类
 * Created by noear on 2017/1/1.
 */
public class RedisX {
    private JedisPool _jedisPool;
    public RedisX(String server, String user, String password, int db) {
        JedisPoolConfig config = new JedisPoolConfig();

        config.setMaxTotal(300);
        config.setMaxIdle(200);
        config.setTestOnBorrow(false);
        config.setTestOnReturn(false);

        String[] ss = server.split(":");

        if(TextUtils.isEmpty(password)){
            password = null;
        }

        _jedisPool = new JedisPool(config, ss[0], Integer.parseInt(ss[1]), 200, password, db);
    }

    public RedisUsing open(){
        Jedis jx = _jedisPool.getResource();
        return new RedisUsing(jx);
    }


    public class RedisUsing{
        private RedisUsing(Jedis c){
            client = c;
        }

        private Jedis client;
        private String _key;
        private int _seconds;

        public RedisUsing key(String key){
            _key = key;
            return this;
        }

        public RedisUsing expire(int seconds) {
            _seconds = seconds;
            return this;
        }

        public Boolean exists() {
            return client.exists(_key);
        }
        public Boolean delete() {
            return client.del(_key) > 0;
        }

        public void close(){
            client.close();
        }

        //------

        public RedisUsing set(String val){
            client.set(_key, val);
            client.expire(_key, _seconds);

            return this;
        }

        public String get(){
            return client.get(_key);
        }

        public Boolean hashHas(String field){
            return client.hexists(_key,field);
        }

        public RedisUsing hashSet(String field,String val){
            client.hset(_key,field,val);
            client.expire(_key, _seconds);

            return this;
        }

        public RedisUsing hashSet(String field,long val){
            client.hset(_key,field,val+"");
            client.expire(_key, _seconds);

            return this;
        }

        public long hashIncr(String field,long num){
            long val = client.hincrBy(_key,field, num);
            client.expire(_key, _seconds);

            return val;
        }

        public String hashGet(String field) {
            return client.hget(_key, field);
        }

        public  RedisUsing listAdd(String val){
            client.lpush(_key, val);
            client.expire(_key, _seconds);

            return this;
        }

        public List<String> listGet(int start, int end){
            return client.lrange(_key,start,end);
        }


    }

}