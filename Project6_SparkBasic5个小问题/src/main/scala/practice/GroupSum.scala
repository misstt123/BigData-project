package practice

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

/**
 * 需求：
 * 原始数据
 * 2010-05-04 12:50,10,10,10
 * 2010-05-05 13:50,20,20,20
 * 2010-05-06 14:50,30,30,30
 * 2010-05-05 13:50,20,20,20
 * 2010-05-06 14:50,30,30,30
 * 2010-05-04 12:50,10,10,10
 * 2010-05-04 11:50,10,10,10
 * 结果数据
 * 2010-05-04 11:50,10,10,10
 * 2010-05-04 12:50,20,20,20
 * 2010-05-05 13:50,40,40,40
 * 2010-05-06 14:50,60,60,60
 * 思路：
 * 分组、计算
 * Created by Administrator on 2016/12/16.
 */
object GroupSum {
  def main(args: Array[String]): Unit = {
    import org.apache.spark
    val sc: SparkContext = util.Util.sc
    val rdd: RDD[String] = sc.textFile("././data/data.txt")
    val rdd2: RDD[(String, Array[String])] = rdd.map(x => {
      val split = x.split(",", 2)
      (split(0), split(1).split(","))
    })

    rdd2.reduceByKey((x, y) => {
      val i1 = x(0).toInt + y(0).toInt
      val i2 = x(1).toInt + y(1).toInt
      val i3 = x(2).toInt + y(2).toInt
      Array(i1 + "," + i2 + "," + i3)
    }).map(x => {
      x._1 + "," + x._2.mkString(",")
    }).foreach(println(_))

  }


}
