package com.goddong.dao;


import com.goddong.domain.SessionDetail;

/**
 * session明细接口
 * @author lyh-god
 *
 */
public interface ISessionDetailDAO {
	
	/**
	 * 插入一条session明细数据
	 * @param sessionDetail
	 */
	void insert(SessionDetail sessionDetail);

}
