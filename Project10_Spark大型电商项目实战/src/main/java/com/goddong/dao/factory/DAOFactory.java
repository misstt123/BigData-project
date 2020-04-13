package com.goddong.dao.factory;

import com.goddong.dao.*;
import com.goddong.impl.*;

/**
 * @Description:DAO工厂类
 * @Author lyh-god
 * @Date 2020/4/13
 **/
public class DAOFactory {
    /**
     * 获取任务管理DAO
     */
    public static ITaskDAO getTaskDAO() {
        return new TaskDAOImpl();

    }

    /**
     * 获取session聚合统计DAO
     * @return
     */
    public static ISessionAggrStatDAO getSessionAggrStatDAO() {
        return new SessionAggrStatDAOImpl();
    }

    public static ISessionRandomExtractDAO getSessionRandomExtractDAO() {
        return new SessinoRandomExtractDAOImpl();
    }

    public static ISessionDetailDAO getSessionDetailDAO() {
        return new SessionDetailDAOImpl();
    }

    public static ITop10CategoryDAO getTop10CategoryDAO() {
        return new Top10CategoryDAOImpl();
    }

    public static ITop10SessionDAO getTop10SessionDAO() {
        return new Top10SessionDAOImpl();
    }


}

