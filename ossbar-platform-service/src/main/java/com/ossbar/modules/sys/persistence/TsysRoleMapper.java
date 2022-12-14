package com.ossbar.modules.sys.persistence;

import com.ossbar.core.baseclass.persistence.BaseSqlMapper;
import com.ossbar.modules.sys.domain.TsysRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Title: 角色管理 Description: Copyright: Copyright (c) 2017
 * Company:ossbar.co.,ltd
 * 
 * @author ossbar.co.,ltd
 * @version 1.0
 */
@Mapper
public interface TsysRoleMapper extends BaseSqlMapper<TsysRole> {
	
	/**
	 * 查询用户创建的角色ID列表
	 */
	List<String> selectRoleListByCreatorUserId(String createUserId);
	
	/**
	 * 删除角色数据
	 */
	int deleteBatchRole(Object[] id);
	
	/**
	 * 删除用户角色数据
	 */
	int deleteBatchUserRole(Object[] id);
	
	/**
	 * 删除角色对应的资源数据
	 */
	int deleteBatchRolePrivilege(Object[] id);
	
	/**
	 * 删除角色对应的数据权限
	 */
	int deleteBatchRoleDataPrivilege(Object[] id);

	/**
	 *
	 * @param roleName
	 * @return
	 */
	TsysRole checkRoleNameUnique(String roleName);
}
