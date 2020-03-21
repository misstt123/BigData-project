package hbase;

import com.sun.jdi.PathSearchingVirtualMachine;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.yarn.webapp.hamlet.Hamlet;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @Description: * HBase解析数据，创建表
 * * members表有5个字段，分别是id、name、address、sDate、eDate。
 * * 测试数据如下，
 * * 2000|cc|上海|2013-04-11|2014-11-18
 * * 2001|Franky|北京|2007-04-11|2010-03-30
 * * 2002|陈慧|上海|2009-04-11|2016-04-30
 * * 2003|Linda7|深圳|2003-04-11|2004-04-25
 * * 2004|Liz|上海|2013-10-11|2015-06-12
 * * 2005|bibalily|广州|2002-04-11|2004-04-25
 * * 2006|加斐|深圳|2012-04-11|2016-05-03
 * * 2007|蒋艳铮|上海|2005-04-11|2007-04-02
 * * 2008|张渠|北京|2000-04-11|2004-04-25
 * * 2009|骆嫣|上海|2006-04-11|2007-04-25
 * * 要求：
 * * 1.创建表members、列族f1，然后插入数据
 * * 思路：
 * * 1.创建HBase表
 * * 2.从磁盘加载数据文件
 * * 3.向HBase写入数据
 * @Author lyh-god
 * @Date 2020/3/21
 **/
public class Data2HBase {
    public static void main(String[] args) throws IOException {
        Connection connection = getConnection();
        String tableNameString = "d2h_table";
        String columnFamily = "d2h_family";
        createTable(connection, tableNameString, columnFamily);
        BufferedReader reader = new BufferedReader(new FileReader(new File("hbasedata.txt")));
        String line = null;
        while ((line = reader.readLine()) != null) {
            String[] split = line.split("\\|");
            put2Table(connection,
                    tableNameString, columnFamily, split);

        }

        reader.close();
        connection.close();

        System.out.println("解析成功并成功导入");
    }


    /**
     * @return org.apache.hadoop.hbase.client.Connection
     * @author lyh-god on 2020/3/21
     * @Description: 获取连接
     */
    private static Connection getConnection() throws IOException {

        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "node01:2181,node02:2181,node03:2181");
        conf.set("hbase.zookeeper.property.clientPort", "2181");
        ConnectionFactory.createConnection(conf);
        return ConnectionFactory.createConnection(conf);
    }

    /**
     * @return
     * @author lyh-god on 2020/3/21
     * @Description: TODO
     */
    private static void createTable(Connection connection, String tableNameString, String columnFamily) throws
            IOException {
        Admin admin = connection.getAdmin();
        TableName tableName = TableName.valueOf(tableNameString); //d2h (data to HBase)
        HTableDescriptor table = new HTableDescriptor(tableName);
        HColumnDescriptor family = new HColumnDescriptor(columnFamily);
        table.addFamily(family);
        //判断表是否已经存在
        if (admin.tableExists(tableName)) {
            admin.disableTable(tableName);
            admin.deleteTable(tableName);
        }
        admin.createTable(table);
    }


    /**
     * @return
     * @author lyh-god on 2020/3/21
     * @Description: 按行 插入Hbase
     */
    private static void put2Table(Connection connection, String tableNameString, String columnFamily, String[] split) throws
            IOException {
        String rowKey = split[0];
        String name = split[1];
        String address = split[2];
        String sDate = split[3];
        String eDate = split[4];
        Table table = connection.getTable(TableName.valueOf(tableNameString));
        Put put = new Put(Bytes.toBytes(rowKey));
//        put.add(columnFamily.getBytes(), "name",name);
//        put.add(Bytes.toBytes(columnFamily), Bytes.toBytes("address"), Bytes.toBytes(address));
//        put.add(Bytes.toBytes(columnFamily), Bytes.toBytes("sDate"), Bytes.toBytes(sDate));
//        put.add(Bytes.toBytes(columnFamily), Bytes.toBytes("eDate"), Bytes.toBytes(eDate));
        table.put(put);
        //关闭表
        table.close();
    }

}
