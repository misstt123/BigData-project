package zhihu_spider.download;

import zhihu_spider.domain.Page;
import zhihu_spider.utils.PageUtil;

/**
 * @Description:下载原始内容
 * @Author lyh-god
 * @Date 2020/3/23
 **/
public class DownLoadImpl implements DownLoad {
    public Page download(String url) {
        Page page = new Page();
        page.setContent(PageUtil.getContent(url));
        page.setUrl(url);
        return page;
    }
}
