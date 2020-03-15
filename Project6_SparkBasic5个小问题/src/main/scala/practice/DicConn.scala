package practice

import org.apache.spark.SparkContext

/**
 * 需求：
 * 文件sheet.txt内容如下
 * 00001	1
 * 00002	2
 * 文件product.txt内容如下
 * 1	皮鞋
 * 2	衣服
 * 希望输出结果
 * 00001	皮鞋
 * 00002	衣服
 */
object DicConn {
  def main(args: Array[String]): Unit = {
    val sc: SparkContext = util.Util.sc

    val sheetFile = sc.textFile("././data/sheet.txt")
    val productFile = sc.textFile("././data/product.txt")
    val sheetRdd = sheetFile.map(x => {
      val split = x.split("\t")
      (split(1), split(0))

    })
    val productRdd = productFile.map(x => {
      val s = x.split("\t")
      (s(0), s(1))
    })


    sheetRdd.join(productRdd).map(_._2).sortByKey()
      .foreach(println(_))


  }


}
