package com.goddong.dao;


import com.goddong.domain.SessionRandomExtract;

/**
 * session随机抽取模块DAO接口
 * @author lyh-god
 *
 */
public interface ISessionRandomExtractDAO {
	
	/**
	 * 插入session随机抽取
	 */
	void insert(SessionRandomExtract sessionRandomExtract);
	
}
