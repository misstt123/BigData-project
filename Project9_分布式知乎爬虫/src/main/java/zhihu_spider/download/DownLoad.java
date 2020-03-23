package zhihu_spider.download;

import zhihu_spider.domain.Page;

public interface DownLoad {
    public Page download(String url);
}
