package zhihu_spider.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description:
 * @Author lyh-god
 * @Date 2020/3/22
 **/
public class ThreadUtil {
    static Logger logger = LoggerFactory.getLogger(ThreadUtil.class);

    public static void sleep(int seconds) {

        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {

            logger.error(e.getMessage());
        }

    }

}
