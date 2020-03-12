package com.goddong.log_analysis;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.sql.Time;

/**
 * @Description:处理分组后的数据
 * @Author lyh-god
 * @Date 2020/3/12
 **/
public class WLA_Reducer extends Reducer<Text, Text, Text, Text> {
    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        long firstTime = Long.MAX_VALUE;
        String startTime = null;
        String endTime = null;
        long lastTime = Long.MIN_VALUE;
        String firstPage = null;
        String lastPage = null;
        int count = 0;
        for (Text value : values) {
            count++;
            String[] split = value.toString().split(",");
            if (TimeUtil.transferDate(split[0]) < firstTime) {

                firstTime = TimeUtil.transferDate(split[0]);
                startTime = split[0].substring(11, 19);
                firstPage = split[1];

            }
            if (TimeUtil.transferDate(split[0]) > lastTime) {

                lastTime=TimeUtil.transferDate(split[0]);
                endTime=split[0].substring(11,19);
                lastPage=split[1];

            }
        }
        long time = 0;
        if ((lastTime - firstTime) % (1000 * 60) > 0) {
            time = (lastTime - firstTime) / (1000 * 60) + 1;
        } else {
            time = (lastTime - firstTime) / (1000 * 60);
        }
        String result = startTime + "\t" + firstPage + "\t" + endTime + "\t" + lastPage + "\t" + count + "\t" + time
                + "分钟";
        context.write(key, new Text(result));
    }



}
