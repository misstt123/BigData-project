package practice

import org.apache.spark.rdd.RDD

import scala.collection.mutable.ListBuffer

/**
 * 需求：倒排索引
 * 原始数据
 * cx1|a,b,c,d,e,f
 * cx2|c,d,e,f
 * cx3|a,b,c,f
 * cx4|a,b,c,d,e,f
 * cx5|a,b,e,f
 * cx6|a,b,c,d
 * cx7|a,b,c,f
 * cx8|d,e,f
 * cx9|b,c,d,e,f
 * 结果数据
 * d|cx1,cx2,cx4,cx6,cx8,cx9
 * e|cx1,cx2,cx4,cx5,cx8,cx9
 * a|cx1,cx3,cx4,cx5,cx6,cx7
 * b|cx1,cx3,cx4,cx5,cx6,cx7,cx9
 * f|cx1,cx2,cx3,cx4,cx5,cx7,cx8,cx9
 * c|cx1,cx2,cx3,cx4,cx6,cx7,cx9
 */
object ReverseIndex {
  def main(args: Array[String]): Unit = {
    val sc = util.Util.sc
    val rdd = sc.textFile("././data/index.txt")
    val rdd1 = rdd.flatMap(x => {
      val data = x.split("|")
      val key = data(0)
      var list = ListBuffer()
      for (value <- data(1).split(",")) {


      }
      list
    }).reduceByKey((x, y) => x + "," + y)
      .foreach(line => {
        println(line._1 + "|" + line._2)
      })


  }


}
