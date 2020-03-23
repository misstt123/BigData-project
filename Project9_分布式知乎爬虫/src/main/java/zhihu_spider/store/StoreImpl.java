package zhihu_spider.store;

import zhihu_spider.domain.User;
import zhihu_spider.utils.JDBCUtil;

/**
 * @Description:
 * @Author lyh-god
 * @Date 2020/3/23
 **/
public class StoreImpl implements store {
    public void store(User user) {

        String sql = "insert into user(userid,name,gender,location,business,employment,position,school,major,answersNum,starsNum,thxNum,saveNum,follow,follower) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        JDBCUtil.update(sql, user);
    }
}
