package com.goddong.log_analysis;


import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * @Description:网站日志统计main函数
 * @Author lyh-god
 * @Date 2020/3/11
 **/
public class MR_logAnalysis extends Configured implements Tool {
    public static void main(String[] args) throws Exception {

        ToolRunner.run(new MR_logAnalysis(), args);
    }


    public int run(String[] strings) throws Exception {
        Job job = Job.getInstance(getConf(), "WLA_analysis");
        String inputPath = "./baidu.log";
        String outPutPath = "./result.log";
        Path path = new Path(outPutPath);
        path.getFileSystem(getConf()).delete(path, true);

        //远程运行要加上这一句
        //        job.setJarByClass(MR_logAnalysis.class);
        FileInputFormat.setInputPaths(job, inputPath);
        job.setMapperClass(WLA_Mapper.class);
        job.setMapOutputKeyClass(LongWritable.class);
        job.setMapOutputValueClass(Text.class);
        job.setReducerClass(WLA_Reducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        FileOutputFormat.setOutputPath(job, path);

        return job.waitForCompletion(true) ? 0 : 1;
    }
}
