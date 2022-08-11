package com.ossbar.modules.sys.persistence;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ossbar.core.baseclass.persistence.BaseSqlMapper;
import com.ossbar.modules.sys.domain.TsysUserinfo;

/**
 * Title: 系统用户 Description: Copyright: Copyright (c) 2017
 * Company:ossbar.co.,ltd
 * 
 * @author ossbar.co.,ltd
 * @version 1.0
 */
@Mapper
public interface TsysUserinfoMapper extends BaseSqlMapper<TsysUserinfo> {
	
	/**
	 * 查询用户的所有权限
	 * @param userId  用户ID
	 */
	List<String> selectAllPerms(String userId);
	
	/**
	 * 查询用户的所有菜单ID
	 */
	List<String> selectAllMenuId(String userId);
	
	/**
	 * 根据用户名，查询系统用户
	 */
	TsysUserinfo selectObjectByUserName(String username);

	/**
	 * 修改密码
	 */
	int updatePassword(Map<String, Object> map);
	/**
	 * 查询所有用户
	 */
	List<TsysUserinfo> getAllUserinfo();
}
