package com.goddong.log_analysis;


import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * @Description:
 * @Author lyh-god
 * @Date 2020/3/11
 **/
public class MR_logAnalysis extends Configured implements Tool {
    public static void main(String[] args) throws Exception {

        ToolRunner.run(new MR_logAnalysis(), args);
    }


    public int run(String[] strings) throws Exception {
        return 0;
    }
}
