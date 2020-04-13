package com.goddong.dao;


import com.goddong.domain.Task;

/**
 * 任务管理DAO接口
 * @author lyh-god
 *
 */
public interface ITaskDAO {
	
	/**
	 * 根据主键查询业务
	 */
	
	Task findById(long taskid);
	
}
