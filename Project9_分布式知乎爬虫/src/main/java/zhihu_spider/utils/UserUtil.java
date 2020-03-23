package zhihu_spider.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.omg.PortableInterceptor.INACTIVE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import zhihu_spider.domain.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description:
 * @Author lyh-god
 * @Date 2020/3/22
 **/
public class UserUtil {

    private static Logger logger = LoggerFactory.getLogger(UserUtil.class);

    public static User processUser(String userUrl) {
        User user = new User();
        String content = PageUtil.getContent(userUrl);
        if (content == null) {
            return user;
        }

        Pattern pattern;
        Matcher matcher;
        JSONObject jUser = null;

        String userID = null;
        pattern = Pattern.compile("following\"><a\\sclass=\"Tabs-link\"\\shref=\"/people/(.*?)/following");
        matcher = pattern.matcher(content);
        if (matcher.find()) {


            userID = matcher.group(1);
            Jedis jedis = JedisUtil.getJedis();

            jedis.lpush(JedisUtil.urlkey, "https://www.zhihu.com/people/" + userID + "/following");
            jedis.lpush(JedisUtil.urlkey, "https://www.zhihu.com/people/" + userID + "/followers");
            JedisUtil.returnResource(jedis);
        }
        //解析页面中接送格式的用户信息
        pattern = Pattern
                .compile("<div\\sid=\"data\"\\sstyle=\"display:none;\"\\sdata-state=\"(.*?)\"\\sdata-config=\"");
        matcher = pattern.matcher(content);
        if (matcher.find()) {
            String jsonStr = matcher.group(1).replaceAll("&quot;", "\"");
            JSONObject jobj = JSON.parseObject(jsonStr);
            jUser = JSON.parseObject(jobj.getString("entities"));
            jobj = JSON.parseObject(jUser.getString("users"));
            jUser = JSON.parseObject(jobj.getString(userID));

        }
        if (jUser == null) {
            return null;
        }
        //匹配姓名
        if (jUser.getString("name") != null) {
            user.setName(jUser.getString("name"));
        }
        //性别
        if (jUser.getString("gender") != null) {
            if (jUser.getString("gender").equals("1")) {
                user.setGender("男");
            } else if (jUser.getString("gender").equals("0")) {
                user.setGender("女");
            } else {
                user.setGender("未填写");
            }
        }
        //关注了
        if (jUser.getString("followingCount") != null) {

            user.setFollow(Integer.parseInt(jUser.getString("followingCount")));
        }
        //关注者
        if (jUser.getString("followerCount") != null) {
            user.setFollow(Integer.parseInt(jUser.getString("followerCount")));
        }
        //被赞数
        if (jUser.getString("voteupCount") != null) {
            user.setStarsNum(Integer.parseInt(jUser.getString("voteupCount")));
        }
        //被感谢数
        if (jUser.get("thankedCount") != null) {
            user.setThxNum(Integer.parseInt(jUser.getString("thankedCount")));
        }
        //被收藏数
        if (jUser.getString("favoritedCount") != null) {
            user.setSaveNum(Integer.parseInt(jUser.getString("favoritedCount")));
        }
        //匹配回答数量
        if (jUser.getString("answerCount") != null) {
            user.setAnswersNum(Integer.parseInt(jUser.getString("answerCount")));
        }
        //匹配行业
        JSONObject businessObj = JSON.parseObject(jUser.getString("business"));

        if (businessObj != null) {
            user.setBusiness(businessObj.getString("name"));
        }
        //居住地
        String locStr = jUser.getString("locations");
        pattern = Pattern.compile("\"name\":\"(.+?)\",.*?\"url");
        if (locStr != null) {

            matcher = pattern.matcher((locStr));
            if (matcher.find()) {
                user.setLocation(matcher.group(1));
            }
        }
        locStr = jUser.getString("educations");
        //匹配专业
        pattern = Pattern.compile("major.*?\"name\":\"(.+?)\",.*?\"url");
        matcher = pattern.matcher(locStr);
        if (matcher.find()) {
            user.setMajor(matcher.group(1));
        }
        //匹配大学
        pattern = Pattern.compile("school.*?\"name\":\"(.+?)\",.*?\"url");
        matcher = pattern.matcher(locStr);
        if (matcher.find()) {
            user.setSchool(matcher.group(1));
        }
        //匹配公司
        locStr = jUser.getString("employments");
        pattern = Pattern.compile("company.*?\"name\":\"(.+?)\",.*?\"url");
        matcher = pattern.matcher(locStr);
        if (matcher.find()) {
            user.setEmployment(matcher.group(1));
        }
        //匹配职位
        pattern=Pattern.compile("job.*?\"name\":\"(.+?)\",.*?\"url");

        matcher=pattern.matcher(locStr);
        if(matcher.find()){
            user.setPosition(matcher.group(1));
        }

        return user;

    }

}
