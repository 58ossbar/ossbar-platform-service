package com.ossbar.modules.evgl.pkg.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.github.pagehelper.PageHelper;
import com.ossbar.common.cbsecurity.logs.annotation.SysLog;
import com.ossbar.common.exception.CreatorblueException;
import com.ossbar.common.utils.ConvertUtil;
import com.ossbar.common.utils.PageUtils;
import com.ossbar.common.utils.Query;
import com.ossbar.common.utils.ServiceLoginUtil;
import com.ossbar.core.baseclass.domain.R;
import com.ossbar.modules.common.DictService;
import com.ossbar.modules.common.GlobalEmpiricalValueGetType;
import com.ossbar.modules.evgl.pkg.api.TevglPkgCheckService;
import com.ossbar.modules.evgl.pkg.domain.TevglPkgCheck;
import com.ossbar.modules.evgl.pkg.domain.TevglPkgInfo;
import com.ossbar.modules.evgl.pkg.persistence.TevglPkgCheckMapper;
import com.ossbar.modules.evgl.pkg.persistence.TevglPkgInfoMapper;
import com.ossbar.modules.sys.domain.TsysDict;
import com.ossbar.platform.core.common.utils.UploadFileUtils;
import com.ossbar.utils.constants.Constant;
import com.ossbar.utils.tool.DateUtils;
import com.ossbar.utils.tool.Identities;
import com.ossbar.utils.tool.StrUtils;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:creatorblue.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Service(version = "1.0.0")
@RestController
@RequestMapping("/pkg/tevglpkgcheck")
public class TevglPkgCheckServiceImpl implements TevglPkgCheckService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglPkgCheckServiceImpl.class);
	@Autowired
	private TevglPkgCheckMapper tevglPkgCheckMapper;
	@Autowired
	private ConvertUtil convertUtil;
	@Autowired
	private ServiceLoginUtil serviceLoginUtil;
	@Autowired
	private UploadFileUtils uploadPathUtils;
	@Autowired
	private TevglPkgInfoMapper tevglPkgInfoMapper;
	@Autowired
	private DictService dictService;

	/**
	 * ????????????(??????List<Bean>)
	 * @param params
	 * @return R
	 */
	@SysLog(value="????????????(??????List<Bean>)")
	@GetMapping("/query")
	@SentinelResource("/pkg/tevglpkgcheck/query")
	public R query(@RequestParam Map<String, Object> params) {
		// ????????????????????????Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<TevglPkgCheck> tevglPkgCheckList = tevglPkgCheckMapper.selectListByMap(query);
		convertUtil.convertUserId2RealName(tevglPkgCheckList, "createUserId", "updateUserId");
		PageUtils pageUtil = new PageUtils(tevglPkgCheckList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	
	/**
	 * ????????????(??????List<Map<String, Object>)
	 * @param params
	 * @return R
	 */
	@SysLog(value="????????????(??????List<Map<String, Object>)")
	@GetMapping("/queryForMap")
	@SentinelResource("/pkg/tevglpkgcheck/queryForMap")
	public R queryForMap(@RequestParam Map<String, Object> params) {
		// ????????????????????????Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<Map<String, Object>> tevglPkgCheckList = tevglPkgCheckMapper.selectListMapByMap(query);
		convertUtil.convertUserId2RealName(tevglPkgCheckList, "create_user_id", "update_user_id");
		PageUtils pageUtil = new PageUtils(tevglPkgCheckList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * ??????
	 * @param tevglPkgCheck
	 * @throws CreatorblueException
	 */
	@SysLog(value="??????")
	@PostMapping("save")
	@SentinelResource("/pkg/tevglpkgcheck/save")
	public R save(@RequestBody(required = false) TevglPkgCheck tevglPkgCheck) throws CreatorblueException {
		tevglPkgCheck.setPcId(Identities.uuid());
		tevglPkgCheck.setCreateUserId(serviceLoginUtil.getLoginUserId());
		tevglPkgCheck.setCreateTime(DateUtils.getNowTimeStamp());
		//ValidatorUtils.check(tevglPkgCheck);
		tevglPkgCheckMapper.insert(tevglPkgCheck);
		return R.ok();
	}
	/**
	 * ??????
	 * @param tevglPkgCheck
	 * @throws CreatorblueException
	 */
	@SysLog(value="??????")
	@PostMapping("update")
	@SentinelResource("/pkg/tevglpkgcheck/update")
	public R update(@RequestBody(required = false) TevglPkgCheck tevglPkgCheck) throws CreatorblueException {
	    //ValidatorUtils.check(tevglPkgCheck);
		tevglPkgCheckMapper.update(tevglPkgCheck);
		return R.ok();
	}
	/**
	 * ????????????
	 * @param id
	 * @throws CreatorblueException
	 */
	@SysLog(value="????????????")
	@GetMapping("delete/{id}")
	@SentinelResource("/pkg/tevglpkgcheck/delete")
	public R delete(@PathVariable("id") String id) throws CreatorblueException {
		tevglPkgCheckMapper.delete(id);
		return R.ok();
	}
	/**
	 * ????????????
	 * @param ids
	 * @throws CreatorblueException
	 */
	@SysLog(value="????????????")
	@PostMapping("deleteBatch")
	@SentinelResource("/pkg/tevglpkgcheck/deleteBatch")
	public R deleteBatch(@RequestParam(required = true) String[] ids) throws CreatorblueException {
		tevglPkgCheckMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * ????????????
	 * @param id
	 * @throws CreatorblueException
	 */
	@SysLog(value="????????????")
	@GetMapping("view/{id}")
	@SentinelResource("/pkg/tevglpkgcheck/view")
	public R view(@PathVariable("id") String id) {
		return R.ok().put(Constant.R_DATA, tevglPkgCheckMapper.selectObjectById(id));
	}

	/**
	 * ?????????????????????????????????
	 * @param params
	 * @return
	 */
	@Override
	@SentinelResource("/pkg/tevglpkgcheck/queryPkgListForMgr")
	public R queryPkgListForMgr(Map<String, Object> params) {
		// ????????????????????????
		if (StrUtils.isNull(params.get("releaseStatus"))) {
			params.put("releaseStatus", "N");
		}
		// ????????????????????????Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<Map<String, Object>> tevglPkgCheckList = tevglPkgCheckMapper.queryWaitCheckPkgList(query);
		if (tevglPkgCheckList != null && tevglPkgCheckList.size() > 0) {
			convertUtil.convertDict(tevglPkgCheckList, "checkState", "isPass");
			tevglPkgCheckList.stream().forEach(a -> {
				a.put("pkgLogo", uploadPathUtils.stitchingPath(a.get("pkgLogo"), "12"));
			});
		}
		PageUtils pageUtil = new PageUtils(tevglPkgCheckList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}

	/**
	 * ?????????????????????????????????
	 * @param params
	 * @param loginUserId
	 * @return
	 */
	@Override
	@SentinelResource("/pkg/tevglpkgcheck/querMyWaitCheckPkgList")
	public R querMyWaitCheckPkgList(Map<String, Object> params, String loginUserId) {
		if (StrUtils.isNull(params.get("pkgId")) || StrUtils.isEmpty(loginUserId)) {
			return R.error("??????????????????");
		}
		params.put("refPkgId", params.get("pkgId"));
		params.remove("pkgId");
		params.put("loginUserId", loginUserId);
		List<Map<String,Object>> list = tevglPkgCheckMapper.querMyWaitCheckPkgList(params);
		if (list != null && list.size() > 0) {
			convertUtil.convertDict(list, "checkState", "isPass");
			list.stream().forEach(a -> {
				a.put("pkgLogo", uploadPathUtils.stitchingPath(a.get("pkgLogo"), "12"));
			});
			return R.ok().put(Constant.R_DATA, list.get(0));
		}
		return R.ok().put(Constant.R_DATA, null);
	}

	@Override
	public R updatePkgReleaseStatus(TevglPkgCheck tevglPkgCheck) {
		if (StrUtils.isEmpty(tevglPkgCheck.getPkgId()) || StrUtils.isEmpty(tevglPkgCheck.getCheckState())) {
			return R.error("??????????????????");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pkgId", tevglPkgCheck.getPkgId());
		List<TevglPkgCheck> list = tevglPkgCheckMapper.selectListByMap(map);
		if (list == null || list.size() == 0) {
			tevglPkgCheck.setPcId(Identities.uuid());
			tevglPkgCheck.setCreateTime(DateUtils.getNowTimeStamp());
			tevglPkgCheck.setCreateUserId(serviceLoginUtil.getLoginUserId());
			tevglPkgCheckMapper.insert(tevglPkgCheck);
			if ("Y".equals(tevglPkgCheck.getCheckState())) {
				// ?????????????????????????????????
				TevglPkgInfo t = new TevglPkgInfo();
				t.setPkgId(tevglPkgCheck.getPkgId());
				t.setReleaseStatus("Y");
				tevglPkgInfoMapper.update(t);
				// ???????????????
				TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(tevglPkgCheck.getPkgId());
				if (tevglPkgInfo != null) {				
					// TODO ??????????????????????????????
					map.clear();
					map.put("type", GlobalEmpiricalValueGetType.TEACHER_LIVING_TEXTBOOKS_17);
					map.put("params17", tevglPkgCheck.getPkgId());
				}
			}
		} else {
			// ??????????????????????????????????????????????????????????????????
			map.clear();
			map.put("refPkgId", tevglPkgCheck.getPkgId());
			List<TevglPkgInfo> tevglPkgInfoList = tevglPkgInfoMapper.selectListByMap(map);
			if (tevglPkgInfoList != null && tevglPkgInfoList.size() > 0) {
				return R.error("?????????????????????????????????????????????????????????");
			}
			TevglPkgCheck check = list.get(0);
			check.setReason(tevglPkgCheck.getReason());
			TevglPkgInfo t = new TevglPkgInfo();
			t.setPkgId(tevglPkgCheck.getPkgId());
			if ("Y".equals(tevglPkgCheck.getCheckState())) {
				t.setReleaseStatus("Y");
				check.setCheckState("Y");
			} else {
				t.setReleaseStatus("N");
				check.setCheckState("N");
			}
			tevglPkgInfoMapper.update(t);
			tevglPkgCheckMapper.update(check);
		}
		return R.ok("????????????");
	}
	
	/**
	 * ????????? teach_exp_type ????????????????????????????????????
	 * @return
	 */
	private Integer findEmpiricalValue() {
		List<TsysDict> list = dictService.getTsysDictListByType("teach_exp_type");
		if (list != null && list.size() > 0) {
			List<TsysDict> collect = list.stream().filter(a -> a.getDictCode().equals(GlobalEmpiricalValueGetType.TEACHER_LIVING_TEXTBOOKS_17)).collect(Collectors.toList());
			if (collect != null && collect.size() > 0) {
				return Integer.valueOf(collect.get(0).getDictValue());
			}
		}
		return 20;
	}

	/**
	 *  ????????????????????????????????????????????????????????????????????????
	 * @param pkgId
	 * @return
	 */
	@Override
	public R getBookTreeDataForMgrCheck(String pkgId) {
		if (StrUtils.isEmpty(pkgId)) {
			return R.error("??????????????????");
		}
		
		return null;
	}
}
