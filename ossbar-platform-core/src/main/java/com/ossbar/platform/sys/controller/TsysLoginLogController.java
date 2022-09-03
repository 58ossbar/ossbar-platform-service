package com.ossbar.platform.sys.controller;

import com.ossbar.core.baseclass.domain.R;
import com.ossbar.modules.sys.api.TsysLoginLogService;
import com.ossbar.platform.core.common.cbsecurity.log.SysLog;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>登陆日志</p>
 * <p>Title: TsysLoginLogController.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>Company: 湖南创蓝信息科技有限公司</p> 
 * @author huj
 * @date 2019年5月30日
 */
@RestController
@RequestMapping("/api/sys/loginlog")
public class TsysLoginLogController {

	@Reference(version = "1.0.0")
	private TsysLoginLogService tsysLoginLogService;
	
	/**
	 * <p>根据条件分页查询列表</p>
	 * @author huj
	 * @data 2019年5月30日
	 * @param params
	 * @return
	 */
	@GetMapping("/query")
	@PreAuthorize("hasAuthority('sys:tsysloginlog:query')")
	@SysLog("根据条件分页查询登陆日志列表")
	public R query(@RequestParam Map<String, Object> params) {
		return tsysLoginLogService.query(params);
	}
	
	/**
	 * <p>批量删除</p>
	 * @author huj
	 * @data 2019年5月30日
	 * @param ids
	 * @return
	 */
	@PostMapping("/deletes")
	@PreAuthorize("hasAuthority('sys:tsysloginlog:remove')")
	@SysLog("删除登陆日志信息")
	public R delete(@RequestBody String[] ids) {
		return tsysLoginLogService.delete(ids);
	}
	
}
