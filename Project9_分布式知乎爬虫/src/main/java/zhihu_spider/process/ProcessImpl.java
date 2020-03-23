package zhihu_spider.process;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zhihu_spider.domain.Page;
import zhihu_spider.download.DownLoadImpl;
import zhihu_spider.utils.UrlUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description:解析原始页面内容
 * @Author lyh-god
 * @Date 2020/3/23
 **/
public class ProcessImpl implements Process {
    private Logger logger = LoggerFactory.getLogger(ProcessImpl.class);

    public void process(Page page) {
        HtmlCleaner htmlCleaner = new HtmlCleaner();
        //获取根节点
        String content = page.getContent();
        //抓取话题精华问题页面url
        String url = page.getUrl();
        TagNode tagNode = htmlCleaner.clean(content);
        Object[] uObj;

        try {
            uObj = tagNode.evaluateXPath("//*[@id='zh-topic-top-page-list']/*/div/div/div[1]/div[3]/span/span[1]/a");
            if (uObj != null & uObj.length > 0) {
                logger.info("此页有" + uObj.length + "个用户");
                TagNode uNode;//最高票用户节点

                for (int i = 0; i < uObj.length; i++) {
                    uNode = (TagNode) uObj[i];
                    //解析node，获取最高票用户链接URL
                    String userHref = "https://www.zhihu.com" + uNode.getAttributeByName("href");
                    //用户url去重
                    UrlUtil.juageUrl(userHref);
                }
            } else {
                String topic = url.substring(url.lastIndexOf("=" + 1));
                Pattern pattern = Pattern.compile("<div\\sclass=\"name\"><a\\shref=\"(.*?)\"\\sclass=\"name-link\"\\sdata-highlight>"
                        + topic + "</a></div>");
                Matcher matcher = pattern.matcher(content);
                if (matcher.find()) {

                    String topicURL = "https://www.zhihu.com" + matcher.group(1) + "/top-answers";
                    DownLoadImpl downLoadPage = new DownLoadImpl();
                    Page userPage = null;
                    //下载精华问题第一页
                    userPage = downLoadPage.download(topicURL + "?page=" + 1);
                    process(userPage);
                } else {
                    logger.info("没有找到相关话题");
                }
            }


        } catch (XPatherException e) {
            logger.error("解析失败" + e.getMessage());
        }

    }
}
