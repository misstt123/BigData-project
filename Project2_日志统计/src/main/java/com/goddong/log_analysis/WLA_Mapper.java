package com.goddong.log_analysis;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.yarn.webapp.view.HtmlPage;

import java.io.IOException;

/**
 * @Description:用于网站日志分组
 * @Author lyh-god
 * @Date 2020/3/12
 **/
public class WLA_Mapper extends Mapper<LongWritable, Text, Text, Text> {
    /**
     * @return void
     * @author lyh-god on 2020/3/12
     * @Description: [2016-11-29 07:33:25 INFO ] (cn.baidu.core.inteceptor.LogInteceptor:55)
     * - [0	180.163.220.126	http://www.baidu.cn/spread	http://www.baidu.cn/spread]
     */
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String url = "(cn.baidu.core.inteceptor.LogInteceptor:55)";
        String baseUrl = "http://www.baidu.cn/";
        int baseLenth = baseUrl.length();
        String log = value.toString();
        String result;
        if (log.indexOf(baseUrl) != -1) {
            String[] head = log.split(url);
            String time = head[0].substring(1, 20);
            String[] footer = head[1].split("\t");
            String ip = footer[1];
            String full = footer[3];
            String page = null;
            if (full.length() - 1 > baseLenth) {
                page = full
                        .substring(baseLenth - 1, full.length());
            }
            result = time + "," + page;
            context.write(new Text(ip),new Text(result));
        }


    }
}
