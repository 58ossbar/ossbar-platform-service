package com.ossbar.modules.evgl.tch.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.ossbar.common.cbsecurity.logs.annotation.SysLog;
import com.ossbar.common.exception.CreatorblueException;
import com.ossbar.common.utils.PageUtils;
import com.ossbar.common.utils.Query;
import com.ossbar.core.baseclass.domain.R;
import com.ossbar.modules.common.CbRoomUtils;
import com.ossbar.modules.evgl.tch.api.TevglTchClassroomRoleprivilegeService;
import com.ossbar.modules.evgl.tch.domain.TevglTchClassroom;
import com.ossbar.modules.evgl.tch.domain.TevglTchClassroomRoleprivilege;
import com.ossbar.modules.evgl.tch.persistence.TevglTchClassroomMapper;
import com.ossbar.modules.evgl.tch.persistence.TevglTchClassroomRoleprivilegeMapper;
import com.ossbar.utils.constants.Constant;
import com.ossbar.utils.tool.Identities;
import com.ossbar.utils.tool.StrUtils;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
@RequestMapping("/tch/tevgltchclassroomroleprivilege")
public class TevglTchClassroomRoleprivilegeServiceImpl implements TevglTchClassroomRoleprivilegeService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglTchClassroomRoleprivilegeServiceImpl.class);
	@Autowired
	private TevglTchClassroomRoleprivilegeMapper tevglTchClassroomRoleprivilegeMapper;
	@Autowired
	private TevglTchClassroomMapper tevglTchClassroomMapper;
	@Autowired
	private CbRoomUtils roomUtils;
	
	/**
	 * ????????????(??????List<Bean>)
	 * @param params
	 * @return R
	 */
	@SysLog(value="????????????(??????List<Bean>)")
	@GetMapping("/query")
	@SentinelResource("/tch/tevgltchclassroomroleprivilege/query")
	public R query(@RequestParam Map<String, Object> params) {
		// ????????????????????????Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<TevglTchClassroomRoleprivilege> tevglTchClassroomRoleprivilegeList = tevglTchClassroomRoleprivilegeMapper.selectListByMap(query);
		PageUtils pageUtil = new PageUtils(tevglTchClassroomRoleprivilegeList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	
	/**
	 * ????????????(??????List<Map<String, Object>)
	 * @param params
	 * @return R
	 */
	@SysLog(value="????????????(??????List<Map<String, Object>)")
	@GetMapping("/queryForMap")
	@SentinelResource("/tch/tevgltchclassroomroleprivilege/queryForMap")
	public R queryForMap(@RequestParam Map<String, Object> params) {
		// ????????????????????????Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<Map<String, Object>> tevglTchClassroomRoleprivilegeList = tevglTchClassroomRoleprivilegeMapper.selectListMapByMap(query);
		PageUtils pageUtil = new PageUtils(tevglTchClassroomRoleprivilegeList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * ??????
	 * @param tevglTchClassroomRoleprivilege
	 * @throws CreatorblueException
	 */
	@SysLog(value="??????")
	@PostMapping("save")
	@SentinelResource("/tch/tevgltchclassroomroleprivilege/save")
	public R save(@RequestBody(required = false) TevglTchClassroomRoleprivilege tevglTchClassroomRoleprivilege) throws CreatorblueException {
		tevglTchClassroomRoleprivilege.setId(Identities.uuid());
		//ValidatorUtils.check(tevglTchClassroomRoleprivilege);
		tevglTchClassroomRoleprivilegeMapper.insert(tevglTchClassroomRoleprivilege);
		return R.ok();
	}
	/**
	 * ??????
	 * @param tevglTchClassroomRoleprivilege
	 * @throws CreatorblueException
	 */
	@SysLog(value="??????")
	@PostMapping("update")
	@SentinelResource("/tch/tevgltchclassroomroleprivilege/update")
	public R update(@RequestBody(required = false) TevglTchClassroomRoleprivilege tevglTchClassroomRoleprivilege) throws CreatorblueException {
	    //ValidatorUtils.check(tevglTchClassroomRoleprivilege);
		tevglTchClassroomRoleprivilegeMapper.update(tevglTchClassroomRoleprivilege);
		return R.ok();
	}
	/**
	 * ????????????
	 * @param id
	 * @throws CreatorblueException
	 */
	@SysLog(value="????????????")
	@GetMapping("delete/{id}")
	@SentinelResource("/tch/tevgltchclassroomroleprivilege/delete")
	public R delete(@PathVariable("id") String id) throws CreatorblueException {
		tevglTchClassroomRoleprivilegeMapper.delete(id);
		return R.ok();
	}
	/**
	 * ????????????
	 * @param ids
	 * @throws CreatorblueException
	 */
	@SysLog(value="????????????")
	@PostMapping("deleteBatch")
	@SentinelResource("/tch/tevgltchclassroomroleprivilege/deleteBatch")
	public R deleteBatch(@RequestParam(required = true) String[] ids) throws CreatorblueException {
		tevglTchClassroomRoleprivilegeMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * ????????????
	 * @param id
	 * @throws CreatorblueException
	 */
	@SysLog(value="????????????")
	@GetMapping("view/{id}")
	@SentinelResource("/tch/tevgltchclassroomroleprivilege/view")
	public R view(@PathVariable("id") String id) {
		return R.ok().put(Constant.R_DATA, tevglTchClassroomRoleprivilegeMapper.selectObjectById(id));
	}

	/**
	 * ??????
	 * @param jsonObject
	 * @return
	 */
	@Override
	public R savePermissionSet(JSONObject jsonObject, String loginUserId) {
		String ctId = jsonObject.getString("ctId");
		if (StrUtils.isEmpty(loginUserId) || StrUtils.isEmpty(ctId)) {
			return R.error("??????????????????");
		}
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(ctId);
		if (tevglTchClassroom == null) {
			return R.error("???????????????????????????");
		}
		boolean hasAuth = roomUtils.hasOperatingAuthorization(tevglTchClassroom, loginUserId);
		if (!hasAuth) {
			return R.error("???????????????????????????");
		}
		JSONArray jsonArray = jsonObject.getJSONArray("menuIdList");
		// ???????????????
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ctId", ctId);
		List<TevglTchClassroomRoleprivilege> list = tevglTchClassroomRoleprivilegeMapper.selectListByMap(map);
		if (list != null && list.size() > 0) {
			List<String> collect = list.stream().map(a -> a.getId()).collect(Collectors.toList());
			tevglTchClassroomRoleprivilegeMapper.deleteBatch(collect.stream().toArray(String[]::new));
		}
		// ???????????????
		if (jsonArray != null && jsonArray.size() > 0) {
			List<String> menuIdList = new ArrayList<String>();
			for (int i = 0; i < jsonArray.size(); i++) {
				if (!menuIdList.contains(jsonArray.getString(i))) {
					menuIdList.add(jsonArray.getString(i));
				}
			}
			List<TevglTchClassroomRoleprivilege> insertList = new ArrayList<TevglTchClassroomRoleprivilege>();
			for (int i = 0; i < menuIdList.size(); i++) {
				TevglTchClassroomRoleprivilege t = new TevglTchClassroomRoleprivilege();
				t.setId(Identities.uuid());
				t.setCtId(ctId);
				t.setRoleId("1"); // ???????????????????????????
				t.setMenuId(menuIdList.get(i));
				insertList.add(t);
			}
			tevglTchClassroomRoleprivilegeMapper.insertBatch(insertList);
		}
		return R.ok("????????????");
	}

	/**
	 * ????????????
	 * @param ctId
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R queryPermissionList(String ctId, String loginUserId) {
		if (StrUtils.isEmpty(ctId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("??????????????????");
		}
		TevglTchClassroom tevglTchClassroom = tevglTchClassroomMapper.selectObjectById(ctId);
		if (tevglTchClassroom == null) {
			return R.ok().put(Constant.R_DATA, new ArrayList<>());
		}
		boolean isRoomCreator = loginUserId.equals(tevglTchClassroom.getCreateUserId());
		boolean isTeachingAssistant = StrUtils.isNotEmpty(tevglTchClassroom.getTraineeId()) && loginUserId.equals(tevglTchClassroom.getTraineeId());
		// ????????????????????????????????????????????????
		if (!(isRoomCreator || isTeachingAssistant)) {
			return R.ok().put(Constant.R_DATA, new ArrayList<>());
		}
		Map<String, Object> map = new HashMap<>();
		map.put("roleId", "1");
		map.put("ctId", ctId);
		return R.ok().put(Constant.R_DATA, tevglTchClassroomRoleprivilegeMapper.queryPermissionList(map));
	}
	
	/**
	 * ?????????????????????????????????
	 *
	 * @param ctId
	 * @return
	 */
	@Override
	public List<String> queryPermissionListByCtId(String ctId) {
		return tevglTchClassroomRoleprivilegeMapper.queryPermissionListByCtId(ctId);
	}
}
