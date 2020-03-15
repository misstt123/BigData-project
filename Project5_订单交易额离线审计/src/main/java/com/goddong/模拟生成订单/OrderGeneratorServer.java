package com.goddong.模拟生成订单;

import kafka.producer.KeyedMessage;
import kafka.producer.Producer;
import kafka.producer.ProducerConfig;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

/**
 * @Description:用于模拟生成订单的服务器 往kafka和日志里打数据
 * @Author lyh-god
 * @Date 2020/3/15
 **/
public class OrderGeneratorServer {
    static final Logger logger = LoggerFactory.getLogger(
            OrderGeneratorServer.class);
    static ArrayList<String> goodsPriceList = null;
    static Random random = new Random();
    static Jedis jedis = null;
    static Producer<String, String> producer = null;
    static KeyedMessage<String, String> message = null;

    public static void main(String[] args) throws IOException {
        readDataFromFile();

    }

    /**
     * @return void
     * @author lyh-god on 2020/3/15
     * @Description: 从订单文件，将商品信息读入ArrayList
     */
    private static void readDataFromFile() throws IOException {
        InputStream resource = OrderGeneratorServer.class.getResourceAsStream("price900");
        List<String> readLines = IOUtils.readLines(resource);
        goodsPriceList = new ArrayList<String>();

        for (int i = 1; i < readLines.size(); i++) {
            String[] split = StringUtils.split(readLines.get(i));
            String code = split[1];
            String priceString = split[5];

            try {
                double price = Double.parseDouble(priceString);
                goodsPriceList.add(code + "\t" + priceString);
            } catch (NumberFormatException e) {
                logger.error("价格无法转换" + e);
            }

        }

    }

    private static Producer<String, String> getProducer() {
        Properties properties = new Properties();
        try {
            properties.load(OrderGeneratorServer.class.getResourceAsStream("price9000"));
            properties.put("serializer.class", "kafka,serializer.String");
            Producer<String, String> producer = new Producer<String, String>(new ProducerConfig(properties));
            return producer;

        } catch (IOException e) {
            logger.error("io异常" + e);
        }
        return null;

    }


}
