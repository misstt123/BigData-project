package com.goddong.Mapreduce;

import com.goddong.utils.JDBCUtil;
import com.goddong.utils.TimeUtil;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.kafka.KafkaSpout;
import org.apache.storm.kafka.SpoutConfig;
import org.apache.storm.kafka.ZkHosts;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import scala.collection.immutable.Range;
import scala.math.Ordering;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;

/**
 * @Description:处理实时交易统计以及排名榜（Top10）
 * @Author lyh-god
 * @Date 2020/3/14
 **/
public class StormKafkaProcessor {


    public static void main(String[] args) throws InterruptedException {

        String topologyName = "TSAS";
        ZkHosts zkHosts = new ZkHosts("node01:2181,node02:2181,node03:2181");
        String topic = "trademx";
        String zkRoot = "/storm";
        String id = "tsaPro";
        SpoutConfig spoutConfig = new SpoutConfig(zkHosts,
                topic, zkRoot, id);
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("kafka", new KafkaSpout(spoutConfig));
        builder.setBolt("AccBolt", new Accbolt().shuffleGrouping
                ("kafka"));
        builder.setBolt("ToDbBolt", new ToDbBolt()
                .shuffleGrouping("AccBolt"));
        Config config = new Config();
        config.setDebug(false);

        if (args.length == 0) {

            LocalCluster localCluster = new LocalCluster();
            localCluster.submitTopology(topologyName, config
                    , builder.createTopology());
            Thread.sleep(1000 * 3600);
            localCluster.killTopology(topologyName);
            localCluster.shutdown();
        } else {
            StormSubmitter.submitTopology(topologyName,
                    config, builder.createTopology());
        }
    }


    /**
     * @author lyh-god on 2020/3/14
     * @Description: AccBolt用于接收SpOUT发送的消息
     * @return
     */
    public static class AccBolt extends BaseRichBolt {
        private OutputCollector collector;
        BigDecimal balance = new BigDecimal(0);
        String markTime = null;


        @Override
        public void prepare(Map<String, Object> stormConf, TopologyContext context, OutputCollector collector) {
            this.collector = collector;
        }

        /**
         * @return void
         * @author lyh-god on 2020/3/14
         * @Description: storm处理逻辑
         */
        @Override
        public void execute(Tuple input) {

            byte[] binary = input.getBinary(0);
            String msg = new String(binary);

            String[] split = msg.split("\t");
            String date = TimeUtil.stamp2Date(split[0]);
            String subdate = date.substring(0, date.length() - 3);

            if (markTime != null && !subdate.equals(markTime)) {
                Values tuple = new Values(markTime, balance);
                collector.emit(tuple);
                balance = new BigDecimal(0);
            }

            markTime = subdate;
            BigDecimal value = new BigDecimal(
                    Double.parseDouble(split[3].toString()) * Double.parseDouble(split[4].toString())
            );
            balance = balance.add(value);
            this.collector.ack(input);


        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declare(
                    new Fields("time", "balance")
            );
        }
    }


    /**
     * @author lyh-god on 2020/3/14
     * @Description: 用于接收一分钟之内的商品总和
     * @return
     */
    public static class ToDbBolt extends BaseRichBolt {

        private OutputCollector collector;
        BigDecimal balance = new BigDecimal(0);
        String markTime = null;

        @Override
        public void prepare(Map<String, Object> stormConf, TopologyContext context, OutputCollector collector) {
            this.collector = collector;
        }

        @Override
        public void execute(Tuple input) {
            markTime = input.getStringByField("time");
            balance = new BigDecimal(input.getShortByField("balance"));
            String sql = "insert into trade(id,time,balance) values(null,?,?)";
            ArrayList<Object> params = new ArrayList<>();
            params.add(markTime);
            params.add(balance);
            JDBCUtil.update(sql, params)
            this.collector.ack(input);

        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {


        }
    }


}
