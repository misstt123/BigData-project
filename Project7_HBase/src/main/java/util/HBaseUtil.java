package util;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;

import java.io.IOException;
import java.util.Map;
import java.util.NavigableMap;

/**
 * 连接mysql
 */
public class HBaseUtil {
    /**
     * @return获取连接
     * @author lyh-god on
     * @Description: TODO
     */
    public static Connection getConnection() throws IOException {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "node01:2181,node02:2181,node03:2181");
        conf.set("hbase.zookeeper.property.clientPort", "2181");
        return ConnectionFactory.createConnection(conf);

    }

    /**
     * @return void
     * @author lyh-god on 2020/3/21
     * @Description: 创建表
     */
    public static void createTable(Connection connection, String tableNameString, String columnFamily) throws IOException {
        Admin admin = connection.getAdmin();
        TableName tableName = TableName.valueOf(tableNameString);
        HTableDescriptor table = new HTableDescriptor(tableName);
        HColumnDescriptor family = new HColumnDescriptor(columnFamily);
        table.addFamily(family);
        if (admin.tableExists(tableName)) {

            admin.disableTable(tableName);
            admin.deleteTable(tableName);
        }
        admin.createTable(table);


    }

    /**
     * @return org.apache.hadoop.hbase.client.Put
     * @author lyh-god on 2020/3/21
     * @Description: 获取插入HBase的操作put
     */
    public static Put createPut(String rowKeyString, byte[] familyName, String columnName, String columnValue) {
        Put put = new Put(rowKeyString.getBytes());
        put.addColumn(familyName, columnName.getBytes(), columnValue.getBytes());
        return put;

    }

    /**打印hbase查询结果
    * @author lyh-god on 2020/3/21
    * @Description: 打印Hbase查询结果
    * @return void
    */
    public static void print(Result result){
        byte[] row = result.getRow(); //行键
        NavigableMap<byte[], NavigableMap<byte[], NavigableMap<Long, byte[]>>> map = result.getMap();
        for (Map.Entry<byte[], NavigableMap<byte[], NavigableMap<Long, byte[]>>> familyEntry : map.entrySet()) {
            byte[] familyBytes = familyEntry.getKey(); //列族
            for (Map.Entry<byte[], NavigableMap<Long, byte[]>> entry : familyEntry.getValue().entrySet()) {
                byte[] column = entry.getKey(); //列
                for (Map.Entry<Long, byte[]> longEntry : entry.getValue().entrySet()) {
                    Long time = longEntry.getKey(); //时间戳
                    byte[] value = longEntry.getValue(); //值
                    System.out.println(String.format("行键rowKey=%s,列族columnFamily=%s,列column=%s,时间戳timestamp=%d,值value=%s", new String(row), new String(familyBytes), new String(column), time, new String(value)));
                }
            }
        }
    }






}
