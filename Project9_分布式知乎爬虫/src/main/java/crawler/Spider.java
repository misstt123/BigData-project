package crawler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import zhihu_spider.domain.Page;
import zhihu_spider.domain.User;
import zhihu_spider.download.DownLoad;
import zhihu_spider.download.DownLoadImpl;
import zhihu_spider.process.Process;
import zhihu_spider.process.ProcessImpl;
import zhihu_spider.store.StoreImpl;
import zhihu_spider.store.store;
import zhihu_spider.utils.JedisUtil;
import zhihu_spider.utils.ThreadUtil;
import zhihu_spider.utils.UserUtil;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description:
 * @Author lyh-god
 * @Date 2020/3/23
 **/
public class Spider {

    static Logger logger = LoggerFactory.getLogger(Spider.class.getSimpleName());
    //创建一个初始化线程数量为5的线程池
    private static ExecutorService threadPool = Executors.newFixedThreadPool(5);
    private DownLoad downLoadIn;
    private Process processIn;
    private store storeIn;

    public void setStoreIn(store storeIn) {
        this.storeIn = storeIn;
    }

    public void setDownLoadInter(DownLoad downLoadIn) {
        this.downLoadIn = downLoadIn;
    }

    public void setProcessInter(Process processIn) {
        this.processIn = processIn;
    }

    public static void main(String[] args) {
        start();
    }


    /**
     * @return void
     * @author lyh-god on 2020/3/23
     * @Description: 启动爬虫
     */
    public static void start() {
        final Spider spider = new Spider();
        spider.setDownLoadInter(new DownLoadImpl());
        spider.setProcessInter(new ProcessImpl());
        spider.setStoreIn(new StoreImpl());
        System.out.println("请输入一个要爬取的知乎话题:");
        Scanner scanner = new Scanner(System.in);
        String topic = scanner.nextLine();
        String url = "https://www.zhihu.com/search?type=topic&q=" + topic;
        logger.info("爬虫开始运行");
        // 下载话题精华问题页
        final Page page = spider.download(url);
        // 解析话题精华问题页
        spider.process(page);
        while (true) {
            Jedis jedis = JedisUtil.getJedis();
            final String userUrl = jedis.rpop(JedisUtil.urlkey);
            JedisUtil.returnResource(jedis);
            if (userUrl != null) {
                threadPool.execute(new Runnable() {
                    public void run() {
                        if (userUrl.endsWith("following") || userUrl.endsWith("follower")) {
                            UserUtil.processUser(userUrl);

                        } else {

                            User user = UserUtil.processUser(userUrl);
                            if (user != null) {
                                spider.store(user);//存储
                            } else {
                                logger.info("很奇怪，user为null");
                            }
                        }
                    }
                });

            } else {
                logger.info("没有url了，休息一会...");
                ThreadUtil.sleep(5);
            }
        }


    }

    /**
     * @return void
     * @author lyh-god on 2020/3/23
     * @Description: 保存解析的用户信息
     */
    private void store(User user) {
        storeIn.store(user);
    }

    /**
     * @return void
     * @author lyh-god on 2020/3/23
     * @Description: 解析爬取的原始内容
     */
    private void process(Page page) {
        processIn.process(page);
    }

    /**
     * @return zhihu_spider.domain.Page
     * @author lyh-god on 2020/3/23
     * @Description: 下载
     */
    private Page download(String url) {
        return downLoadIn.download(url);

    }


}
