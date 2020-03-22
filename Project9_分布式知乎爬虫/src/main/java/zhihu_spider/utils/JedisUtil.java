package zhihu_spider.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import sun.awt.ConstrainableGraphics;


/**
 * @Description:
 * @Author lyh-god
 * @Date 2020/3/22
 **/
public class JedisUtil {
    protected static Logger logger = LoggerFactory.getLogger(JedisUtil.class);
    public static final String HOST = "127.0.0.1";
    public static final int PORT = 6379;

    public static final String urlkey = "url";

    public static JedisPool jedisPool = null;

    private JedisUtil() {

    }


    /**
     * @return void
     * @author lyh-god on 2020/3/22
     * @Description: 初始化JedisPool
     */
    private static void initialPool() {
        if (jedisPool == null) {

            JedisPoolConfig conf = new JedisPoolConfig();
            conf.setMaxIdle(200);
            conf.setMaxTotal(2000);
            conf.setMaxWaitMillis(1000 * 10);
            conf.setTestOnBorrow(true);
            conf.setTestOnReturn(true);
            jedisPool = new JedisPool(conf, HOST, PORT, 10000);

        }

    }

    /**
     * @return
     * @author lyh-god on 2020/3/22
     * @Description: 在多线程环境同步初始化
     */
    private static synchronized void poolInit() {

        if (jedisPool == null) {
            initialPool();
        }
    }

    /**
     * @return
     * @author lyh-god on 2020/3/22
     * @Description: 同步获取Jedis实例
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

            logger.error("获取jedis出错：" + e);
        }
        return jedis;

    }

    /**
     * @return
     * @author lyh-god on 2020/3/22
     * @Description: 释放jedis资源
     */
    public static void returnResource(Jedis jedis) {
        if (jedis != null && jedisPool != null) {
            jedis.close();

        }

    }


}
