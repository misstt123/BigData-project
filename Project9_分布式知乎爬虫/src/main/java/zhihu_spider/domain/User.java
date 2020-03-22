package zhihu_spider.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description:存储用户信息
 * @Author lyh-god
 * @Date 2020/3/22
 **/
@Getter
@Setter
@ToString
public class User {

    String userID;// 用户ID

    String name;// 用户姓名

    String gender;// 性别

    String location;// 居住地;

    String business;// 行业

    String employment;// 公司

    String position;// 职位;

    String school;// 大学

    String major;// 专业

    int answersNum;// 回答数量

    int starsNum;// 被赞同数

    int thxNum;// 被感谢数

    int saveNum;// 被收藏数

    int follow;// 关注了

    int follower;// 关注者
}

