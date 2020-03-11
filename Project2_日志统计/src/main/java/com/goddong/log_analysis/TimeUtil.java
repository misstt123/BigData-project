package com.goddong.log_analysis;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description:
 * @Author lyh-god
 * @Date 2020/3/11
 **/
public class TimeUtil {

    /**
     * @return java.lang.Long
     * @author lyh-god on 2020/3/11
     * @Description: 将字符串转换为long类型的时间戳
     */
    public static long transferDate(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            return -1;
        }
        long time = date.getTime();
        return time;


    }
}
