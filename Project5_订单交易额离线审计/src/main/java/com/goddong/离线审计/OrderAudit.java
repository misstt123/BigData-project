package com.goddong.离线审计;

import com.goddong.utils.HiveUtil;
import com.goddong.utils.JDBCUtil;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:用于交易审计
 * @Author lyh-god
 * @Date 2020/3/15
 **/
public class OrderAudit {
    public static void main(String[] args) throws SQLException {
        audit("2016-12-11 17:17");
    }

    /**
     * @return void
     * @author lyh-god on 2020/3/15
     * @Description: 审计mysql中与hive的交易
     */
    private static void audit(String minute) throws SQLException {

        String sql = "select balance from order where time=?";
        ArrayList<Object> params = new ArrayList<Object>();
        params.add(minute);
        List<Object> balanceList = JDBCUtil.queryRow(sql, params);
        BigDecimal mysqlBalance = new BigDecimal(balanceList.get(0).toString());
        System.out.println("实时计算：" + mysqlBalance);

        BigDecimal hiveBalance = HiveUtil.querySale(minute);

        System.out.println("落地数据：" + hiveBalance);
        System.out.println("审计结果是否相等" + mysqlBalance.equals(hiveBalance));

    }


}
