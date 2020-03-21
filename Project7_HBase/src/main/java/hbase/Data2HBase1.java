package hbase;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.BufferedMutator;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import util.HBaseUtil;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @Description: * 大量数据导入HBase，
 * * 三种方式比较及优化
 * * 第一种，单条导入，10W条记录耗时2-3分钟，单线程
 * * 第二种，批量导入，10w条记录耗时3s，100w条记录耗时25s，单线程(1亿条记录利用多线程)
 * * 第三种，利用BufferedMutator批量导入，10w条记录耗时2s，100w条记录耗时22s，单线程(1亿条记录利用多线程)
 * @Author lyh-god
 * @Date 2020/3/21
 **/
public class Data2HBase1 {
    public static void main(String[] args) throws IOException {
        Connection connection = HBaseUtil.getConnection();
//        singleRowImport(connection);
//        batchRowImport(connection);
        bmImport(connection);

        connection.close();

    }

    /**
     * @return void
     * @author lyh-god on 2020/3/21
     * @Description: 利用BufferedMutator批量导入
     */
    private static void bmImport(Connection connection) throws IOException {
        BufferedMutator t3 = connection.getBufferedMutator(TableName.valueOf("T3"));
        long startTime = System.currentTimeMillis();
        ArrayList<Put> puts = new ArrayList<>();
        for (int i = 0; i < 99999; i++) {
            puts.add(HBaseUtil.createPut(i + "", "f1".getBytes(), "c1", i + ""));
            if (i % 10000 == 0) {
                t3.mutate(puts);
                puts.clear();
            }

        }
        t3.mutate(puts);
        t3.close();

    }

    /**
     * @return void
     * @author lyh-god on 2020/3/21
     * @Description: 批量导入
     */
    private static void batchRowImport(Connection connection) throws IOException {
        Table table = connection.getTable(TableName.valueOf("t3"));
        long startTime = System.currentTimeMillis();
        ArrayList<Put> puts = new ArrayList<>();
        for (int i = 0; i < 99999; i++) {
            puts.add(HBaseUtil.createPut(i + "", "f1".getBytes(), "c1", i + ""));

            if (i % 10000 == 0) {
                table.put(puts);
                puts.clear();
            }
        }
        table.put(puts);
        table.close();
        System.out.println("共耗时： " + (System.currentTimeMillis() - startTime) + "ms");


    }

    /**
     * @return void
     * @author lyh-god on 2020/3/21
     * @Description: 单条数据导入
     */
    private static void singleRowImport(Connection connection) throws IOException {
        Table table = connection.getTable(TableName.valueOf("t3"));

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 99999; i++) {


            table.put(HBaseUtil.createPut(i + "", "f1".getBytes(), "c1", i + ""));
        }
        table.close();
        System.out.println("共耗时：" + (System.currentTimeMillis() - startTime) + "ms");

    }
}
