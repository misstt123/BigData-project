package com.goddong.Hive;

import com.goddong.JDBC.JDBCUtil;
import groovy.sql.Sql;
import org.apache.hadoop.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author lyh-god
 * @Date 2020/3/13
 **/
public class HibeDemo {

    static String url = "jdbc:jdbc://node03:3306/hive";
    static String user = "root";
    static String password = "qwe123456";

    static {
        try {
            Class.forName("com.mysql.jdbc.driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException, URISyntaxException {
        Class.forName("org.apache.hadoop.hive.jdbc.driver");
        Connection conn = DriverManager.getConnection("jdbc:hive://node01:10000/hive", "root", "qwe123456");
        Statement stmt = conn.createStatement();
        ArrayList<String> list = queryDBTable();
        for (String table : list) {
            String sql = generateCreateTable(table);
            stmt.execute(sql);
        }

        //从mysql查出数据写入hive
        for (String table : list) {
            List<List<Object>> queryAll = queryData(table);
            String path = "D:/1.txt";
            FileWriter fileWriter = new FileWriter(new File(path), true);
            URI uri = new URI("hdfs://192.168.1.101:9000");
            FileSystem fileSystem = FileSystem.get(uri, new Configuration());
            // 4.2 、 将本地文件传至HDFS中
            FSDataOutputStream outputStream = fileSystem.create(new Path("/home/group4/mxlee/1205hive/logs/1.txt"));

            for (List<Object> list2 : queryAll) {
                String[] split = list2.toString().split(",");
                String id = split[0].substring(1);
                String province = split[1].substring(0, split[1].length() - 1);
                fileWriter.write(id + "\t" + province + "\r\n");
            }
            fileWriter.close();
            FileInputStream in = new FileInputStream(
                    new File(path)
            );
            IOUtils.copyBytes(in, outputStream, new Configuration(), true);
            System.out.println("上传成功");
        }
        String sql = "load data inpath '/home/user/log.txt' overwrite into table" +
                "hive.jdbc2_hive";
        stmt.executeQuery(sql);
        conn.close();


    }

    /**
     * @return java.util.List<java.util.List < java.lang.Object>>
     * @author lyh-god on 2020/3/13
     * @Description: mysql中查询表的所有数据
     */
    private static List<List<Object>> queryData(String table) {

        String sql = "select * from" + table;
        return JDBCUtil.queryAll(sql, null);
    }

    /**
     * @return java.lang.String
     * @author lyh-god on 2020/3/13
     * @Description: 生成表中的sql语句
     */
    private static String generateCreateTable(String table) throws SQLException {
        ArrayList<String> arrayList = queryTableInfo(table);
        StringBuilder stringBuilder = new StringBuilder(
                "create table Hive." + table + "("
        );
        for (String s : arrayList) {
            stringBuilder.append(StringUtils.replaceChars(

                    s, "\t", " "))
                    .append(",");
        }
        String sql = stringBuilder.substring(0, stringBuilder.length() - 1);
        sql += ")";

        return sql;
    }

    /**
     * @return java.util.ArrayList<java.lang.String>
     * @author lyh-god on 2020/3/13
     * @Description: 查看数据库表结构
     */
    private static ArrayList<String> queryTableInfo(String table) throws SQLException {
        Connection connection = DriverManager.getConnection(url, user, password);
        String sql = "select * from" + table;

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        ArrayList<String> arrayList = new ArrayList<String>();

        for (int i = 1; i < columnCount; i++) {
            String typeName = "String";
            String name = metaData.getColumnName(i);
            arrayList.add(name + " " + typeName);

        }
        connection.close();
        return arrayList;


    }

    /**
     * @return java.util.ArrayList<java.lang.String>
     * @author lyh-god on 2020/3/13
     * @Description: 遍历数据库，获得数据库中的所有表名
     */
    private static ArrayList<String> queryDBTable() throws SQLException {
        Connection connection = DriverManager.getConnection(url, user, password);
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet tables = metaData.getTables(null, null, null,
                new String[]{"TABLE"});
        int columnCount = tables.getMetaData().getColumnCount();
        ArrayList<String> arrayList = new ArrayList<String>();
        while (tables.next()) {
            arrayList.add(tables.getObject(3).toString());
        }

        connection.close();
        return arrayList;
    }


}
