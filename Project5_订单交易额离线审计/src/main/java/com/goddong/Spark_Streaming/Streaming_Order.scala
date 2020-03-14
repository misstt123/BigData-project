package com.goddong.Spark_Streaming

import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}


/**
 * 从订单服务器获取订单，服务器每秒生成一条订单，
 * 简化订单格式(商品码\t单价\t购买数量)
 * 1、实时显示总销售额
 * 2、打印最近1分钟、5分钟、15分钟的销售总额(其实一样)
 * 3、打印最近1小时内销售总额前10的商品
 */
object Streaming_Order {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setAppName("Streaming_Order")
    conf.setMaster("local[3]")
    val ssc: StreamingContext = new StreamingContext(conf, Seconds(1))
    ssc.checkpoint("hdfs://node01:8020/streaming_order")
    val ts = ssc.socketTextStream("192.168.138.131", 5656,
      storageLevel = StorageLevel.MEMORY_AND_DISK)
    val ds: DStream[(String, Double)] = ts.map(x => {
      val result: Array[String] = x.split("\t")
      (result(0), result(1).toDouble * result(2).toInt)
    })

    ds.map(x => println("当前销售总额" + x._2)).print();
    ds.reduceByKeyAndWindow((x, y) => x + y, Seconds(60), Seconds(1))
      .map(X => X._2).reduce(_ + _).print()
    ds.reduceByKeyAndWindow(_ + _, Seconds(60 * 5), Seconds(1))
      .map(x => println(x._2))
    ds.reduceByKeyAndWindow(_ + _, Seconds(60 * 15), Seconds(1))
      .map(_._2).reduce(_ + _).print()

    ds.reduceByKeyAndWindow(_ + _, Seconds(60 * 60), Seconds(1))
    ds.foreachRDD(_.sortBy(_._2).take(10).foreach(println(_)))
    ssc.start()
    ssc.awaitTermination()
  }
}
