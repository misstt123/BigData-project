package util

import org.apache.spark.{SparkConf, SparkContext}

/**
 *
 */
object Util {
  def conf: SparkConf = {
    System.setProperty("hadoop.home.dir", "hdfs://node01/home/dictionary_link")
    System.setProperty("spark.master", "local")

    val conf = new SparkConf()
    conf.setAppName(Util.getClass.getSimpleName)
    conf
  }

  def sc = new SparkContext(conf)


  /**
   * 加载文件或文件夹（hdfs）
   *
   * @param path
   * @return
   */
  def loadFile(path: String) = {
    val rdd = Util.sc.textFile(path)
    rdd
  }

  /**
   *
   * @param path
   * @return
   */
  def loadWholeFile(path: String) = {
    val rdd = Util.sc.textFile(path)
    rdd
  }


}
