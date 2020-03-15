package com.goddong.模拟生成订单;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @Description:
 * @Author lyh-god
 * @Date 2020/3/15
 **/
public class JedisUtil {

    protected static Logger logger = LoggerFactory.getLogger(JedisUtil.class);

    public static final String host = "node01";
    public static final int port = 6379;

    private static JedisPool jedisPool = null;

    /**
     * @return void
     * @author lyh-god on 2020/3/15
     * @Description: 初始化JedisPool
     */
    private static void initialPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(100);
        config.setMinIdle(20);
        config.setMaxTotal(1000);
        config.setMaxWaitMillis(1000 * 50);
        config.setTestOnBorrow(true);
        jedisPool = new JedisPool(config, host, port);

    }

    /**
     * @return void
     * @author lyh-god on 2020/3/15
     * @Description: 多线程同步环境
     */

    private static synchronized void poolInit() {
        if (jedisPool == null) {
            initialPool();
        }
    }

    /**
     * @return redis.clients.jedis.Jedis
     * @author lyh-god on 2020/3/15
     * @Description: 同步获取jedis实例
     */

    public synchronized static Jedis getJedis() {

        if (jedisPool == null) {
            poolInit();
        }
        Jedis jedis = null;

        try {
            if (jedisPool != null) {
                jedis = jedisPool.getResource();
            }
        } catch (Exception e) {
            logger.error("获取jedis出错" + e);
        } finally {
            returnResource(jedis);
        }
        return jedis;

    }

    /**
     * @return void
     * @author lyh-god on 2020/3/15
     * @Description: 释放jedis资源
     */
    private static void returnResource(Jedis jedis) {

        jedis.close();
    }

    /**
     * @return
     * @author lyh-god on 2020/3/15
     * @Description: TODO
     */
    public static void returnBrokenJedis(Jedis jedis) {
        if (jedis != null && jedisPool != null) {
            jedisPool.returnBrokenResource(jedis);
        }
        jedis = null;
    }

}
