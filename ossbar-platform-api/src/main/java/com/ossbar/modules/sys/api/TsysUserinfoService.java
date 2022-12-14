package com.ossbar.modules.sys.api;

import com.ossbar.core.baseclass.domain.R;
import com.ossbar.modules.sys.domain.TsysUserinfo;
import com.ossbar.modules.sys.dto.user.SaveUserDTO;
import com.ossbar.modules.sys.dto.user.SaveUserRoleDTO;

import java.util.List;
import java.util.Map;

/**
 * <p>系统用户api</p>
 * <p>Title: TsysUserinfoServiceImpl.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>Company: 开源吧</p> 
 * @author huj
 * @date 2019年5月5日
 */
public interface TsysUserinfoService {

	/**
	 * 从参数中获取默认密码
	 * @return
	 */
	String getDefaultPasswordFormParameters();

	/**
	 * <p>根据条件查询记录(所有用户列表)</p>
	 * @author huj
	 * @data 2019年5月7日
	 * @param params 查询条件
	 * @return
	 */
	R query(Map<String, Object> params);
	
	/**
	 * <p>根据机构ID,查询用户列表</p>
	 * @author huj
	 * @data 2019年5月8日
	 * @param orgIds 机构ID 多个以逗号隔开
	 * @return 
	 */
	List<TsysUserinfo> queryList(String[] orgIds);

	/**
	 * <p>新增</p>
	 * @author huj
	 * @data 2019年5月7日
	 * @param user
	 * @return
	 */
	R save(SaveUserDTO user);
	
	/**
	 * <p>修改</p>
	 * @author huj
	 * @data 2019年5月7日
	 * @return
	 */
	R update(SaveUserDTO user);
	
	/**
	 * <p>删除</p>
	 * @author huj
	 * @data 2019年5月8日
	 * @param id 被删除用户的ID
	 * @return 
	 */
	R delete(String id);
	
	/**
	 * <p>批量删除用户</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param userIds 被删除用户的ID
	 * @return
	 */
	R deleteBatch(String[] userIds);
	
	/**
	 * <p>详细信息</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param id
	 * @return
	 */
	R view(String id);

	/**
	 * <p>分配角色，当前登录用户不能给自己分配角色</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param dto
	 * @return
	 */
	R grantRole(SaveUserRoleDTO dto);

	/**
	 * 获取用户已分配的角色
	 * @author huj
	 * @data 2019年6月14日
	 * @param ids
	 * @return
	 */
	R toGrantRole(String[] ids);
	
	/**
	 * <p>修改登录用户密码</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param userId 当前登陆用户ID
	 * @param password 旧密码
	 * @param newPassword 新密码
	 * @return
	 */
	R updatePassword(String userId, String password, String newPassword);
	
	/**
	 * <p>重置用户密码,自动忽略当前登录用户</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param userIdList
	 * @return
	 */
	R resetPassword(List<String> userIdList);

	/**
	 * 清除用户的所有权限，忽略当前登录用户
	 * @author huj
	 * @data 2019年5月6日
	 * @param userIds
	 * @return
	 */
	R clearPermissions(List<String> userIds);

	/**
	 * <p>查询用户的所有权限</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @param userId
	 * @return
	 */
	List<String> selectAllPerms(String userId);
	
	/**
	 * <p>查询用户的所有菜单ID</p>
	 * @author huj
	 * @data 2019年5月5日
	 * @param userId
	 * @return
	 */
	List<String> selectAllMenuId(String userId);
	
	/**
	 * 根据唯一用户名，查询系统用户
	 * @author huj
	 * @data 2019年5月5日
	 * @param username
	 * @return
	 */
	TsysUserinfo selectObjectByUserName(String username);
	
	/**
	 * <p>根据用户ID,查询信息</p>
	 * @author huj
	 * @data 2019年5月11日
	 * @param userId
	 * @return
	 */
	TsysUserinfo selectObjectByUserId(String userId);
	
	/**
	 * <p>查询所有用户</p>
	 * @author huj
	 * @data 2019年5月6日
	 * @return
	 */
	List<TsysUserinfo> getAllUserinfo();
	
	/**
	 * <p>更改状态</p>
	 * @author huj
	 * @data 2019年6月12日
	 * @param user
	 * @return
	 */
	R updataStatus(TsysUserinfo user);
	
	/**
	 * <p>更新排序号</p>
	 * @author huj
	 * @data 2019年7月1日
	 * @param params
	 * @return
	 */
	R updateSort(Map<String, Object> params);
	
	/**
	 * <p>获取最大排序号</p>
	 * @author huj
	 * @data 2019年7月1日
	 * @return
	 */
	int getMaxSort();
}
