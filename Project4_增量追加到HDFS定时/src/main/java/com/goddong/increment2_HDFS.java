package com.goddong;

import sun.management.FileSystem;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.nio.file.FileSystemAlreadyExistsException;
import java.sql.*;

/**
 * @Description:
 * @Author lyh-god
 * @Date 2020/3/13
 **/
public class increment2_HDFS {
    private static final String url = "jdbc:mysql://node03:3306/test?useUnicode=true&characterEncoding=utf-8";
    private static final String username = "root";
    private static final String password = "qwe123456";
    private FileSystem fileSystem = null;
    static int hehe = 0;

    static {
        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static String URIPATH = "hdfs://node01:9000";

    public static void main(String[] args) {
        FileSystem = getInstance();
        Jedis jedis = new Jedis("192.168.1.107", 6379);
        String num = jedis.get("mark");
        jedis.close();
        int count = 0;
        if (num != null) {
            count = Integer.parseInt(num);

        }
        String sql = "select * from test1 limit ?,99999999";
        queryRS(sql, count);
    }

    /**
     * @return void
     * @author lyh-god on 2020/3/13
     * @Description: 从mysql中读取更新的数据，返回ResultSet
     */
    private static void queryRS(String sql, int count) {
        Connection connection = null;
        ResultSet resultset = null;
        StringBuilder sb = new StringBuilder();

        try {
            connection = DriverManager.getConnection(url, user, password);
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, count);
            resultset = ps.executeQuery();
            // 2、解析rs
            int columnNum = resultset.getMetaData().getColumnCount();
            sb.delete(0, sb.length());
            int id;
            String name;
            String line;
            int num = 0;
            while (resultset.next()) {
                id = resultset.getInt(1);
                name = resultset.getString(2);
                line = id + "\t" + name;
                sb.append(line).append("\r\n");
                num++;
                if (num == 1024 * 10) {
                    // 写入hdfs
                    byte[] bytes = sb.toString().getBytes();
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
                    put(fileSystem, inputStream, bytes);
                    // StringBuilder清空
                    sb.delete(0, sb.length());
                }
            } // end while
            byte[] bytes = sb.toString().getBytes();
            ByteArrayInputStream is = new ByteArrayInputStream(bytes);
            put(fileSystem, is, bytes);
            mark2Redis(count + num);
            num = 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } // end if
        } // end finally

    }

    private static Object getInstance() {

        Configuration config = new Configuration();
        config.set("dfs.support.append", "true");
        config.set("dfs.client.block.write.replace-datanode-on-failure.policy", "NEVER");
        config.set("dfs.client.block.write.replace-datanode-on-failure.enable", "true");
        URI uri = new URI(URIPATH);
        return FileSystem.get(uri, config);
    }

    /**
    * @author lyh-god on 2020/3/13
    * @Description: 从redis中打印读取标记
    * @return void
    */
    private static void mark2Redis(int num) {
        Jedis jedis = new Jedis("192.168.1.107", 6379);
        jedis.set("mark", num + "");
        jedis.close();
    }


}
