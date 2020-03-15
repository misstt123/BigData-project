package practice

import org.apache.spark.SparkContext

/**
 * 需求：单词计数
 * 思路：
 * 1、先把所有单词分割
 * 2、再给所有单词赋值(key,1)
 * 3、再用reduceByKey进行汇总
 * Created by Administrator on 2016/12/15.
 */
object WordCount {
  def main(args: Array[String]): Unit = {
    val sc: SparkContext = util.Util.sc
    val rdd = sc.textFile("././data/1.txt")
    rdd.flatMap(_.split(","))
      .map((_, 1))
      .reduceByKey(_ + _).foreach(println(_))


  }


}
