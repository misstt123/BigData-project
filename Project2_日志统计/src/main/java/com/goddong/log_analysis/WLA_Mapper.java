package com.goddong.log_analysis;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * @Description:用于网站日志分组
 * @Author lyh-god
 * @Date 2020/3/12
 **/
public class WLA_Mapper  extends Mapper<LongWritable, Text,Text,Text> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {



    }
}
