package com.ossbar.modules.evgl.pkg.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.ossbar.common.cbsecurity.logs.annotation.SysLog;
import com.ossbar.common.exception.CreatorblueException;
import com.ossbar.common.utils.ConvertUtil;
import com.ossbar.common.utils.PageUtils;
import com.ossbar.common.utils.Query;
import com.ossbar.common.utils.ServiceLoginUtil;
import com.ossbar.core.baseclass.domain.R;
import com.ossbar.modules.common.PkgUtils;
import com.ossbar.modules.evgl.pkg.api.TevglPkgDefaultChapterService;
import com.ossbar.modules.evgl.pkg.domain.TevglPkgDefaultChapter;
import com.ossbar.modules.evgl.pkg.domain.TevglPkgInfo;
import com.ossbar.modules.evgl.pkg.persistence.TevglPkgDefaultChapterMapper;
import com.ossbar.modules.evgl.pkg.persistence.TevglPkgInfoMapper;
import com.ossbar.utils.constants.Constant;
import com.ossbar.utils.tool.DateUtils;
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
@RequestMapping("/pkg/tevglpkgdefaultchapter")
public class TevglPkgDefaultChapterServiceImpl implements TevglPkgDefaultChapterService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglPkgDefaultChapterServiceImpl.class);
	@Autowired
	private TevglPkgDefaultChapterMapper tevglPkgDefaultChapterMapper;
	@Autowired
	private ConvertUtil convertUtil;
	@Autowired
	private ServiceLoginUtil serviceLoginUtil;
	@Autowired
	private TevglPkgInfoMapper tevglPkgInfoMapper;
	@Autowired
	private PkgUtils pkgUtils;
	
	/**
	 * ????????????(??????List<Bean>)
	 * @param params
	 * @return R
	 */
	@SysLog(value="????????????(??????List<Bean>)")
	@GetMapping("/query")
	@SentinelResource("/pkg/tevglpkgdefaultchapter/query")
	public R query(@RequestParam Map<String, Object> params) {
		// ????????????????????????Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<TevglPkgDefaultChapter> tevglPkgDefaultChapterList = tevglPkgDefaultChapterMapper.selectListByMap(query);
		convertUtil.convertUserId2RealName(tevglPkgDefaultChapterList, "createUserId", "updateUserId");
		PageUtils pageUtil = new PageUtils(tevglPkgDefaultChapterList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	
	/**
	 * ????????????(??????List<Map<String, Object>)
	 * @param params
	 * @return R
	 */
	@SysLog(value="????????????(??????List<Map<String, Object>)")
	@GetMapping("/queryForMap")
	@SentinelResource("/pkg/tevglpkgdefaultchapter/queryForMap")
	public R queryForMap(@RequestParam Map<String, Object> params) {
		// ????????????????????????Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<Map<String, Object>> tevglPkgDefaultChapterList = tevglPkgDefaultChapterMapper.selectListMapByMap(query);
		convertUtil.convertUserId2RealName(tevglPkgDefaultChapterList, "create_user_id", "update_user_id");
		PageUtils pageUtil = new PageUtils(tevglPkgDefaultChapterList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * ??????
	 * @param tevglPkgDefaultChapter
	 * @throws CreatorblueException
	 */
	@SysLog(value="??????")
	@PostMapping("save")
	@SentinelResource("/pkg/tevglpkgdefaultchapter/save")
	public R save(@RequestBody(required = false) TevglPkgDefaultChapter tevglPkgDefaultChapter) throws CreatorblueException {
		tevglPkgDefaultChapter.setSeId(Identities.uuid());
		tevglPkgDefaultChapter.setCreateUserId(serviceLoginUtil.getLoginUserId());
		tevglPkgDefaultChapter.setCreateTime(DateUtils.getNowTimeStamp());
		//ValidatorUtils.check(tevglPkgDefaultChapter);
		tevglPkgDefaultChapterMapper.insert(tevglPkgDefaultChapter);
		return R.ok();
	}
	/**
	 * ??????
	 * @param tevglPkgDefaultChapter
	 * @throws CreatorblueException
	 */
	@SysLog(value="??????")
	@PostMapping("update")
	@SentinelResource("/pkg/tevglpkgdefaultchapter/update")
	public R update(@RequestBody(required = false) TevglPkgDefaultChapter tevglPkgDefaultChapter) throws CreatorblueException {
	    //ValidatorUtils.check(tevglPkgDefaultChapter);
		tevglPkgDefaultChapterMapper.update(tevglPkgDefaultChapter);
		return R.ok();
	}
	/**
	 * ????????????
	 * @param id
	 * @throws CreatorblueException
	 */
	@SysLog(value="????????????")
	@GetMapping("delete/{id}")
	@SentinelResource("/pkg/tevglpkgdefaultchapter/delete")
	public R delete(@PathVariable("id") String id) throws CreatorblueException {
		tevglPkgDefaultChapterMapper.delete(id);
		return R.ok();
	}
	/**
	 * ????????????
	 * @param ids
	 * @throws CreatorblueException
	 */
	@SysLog(value="????????????")
	@PostMapping("deleteBatch")
	@SentinelResource("/pkg/tevglpkgdefaultchapter/deleteBatch")
	public R deleteBatch(@RequestParam(required = true) String[] ids) throws CreatorblueException {
		tevglPkgDefaultChapterMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * ????????????
	 * @param id
	 * @throws CreatorblueException
	 */
	@SysLog(value="????????????")
	@GetMapping("view/{id}")
	@SentinelResource("/pkg/tevglpkgdefaultchapter/view")
	public R view(@PathVariable("id") String id) {
		return R.ok().put(Constant.R_DATA, tevglPkgDefaultChapterMapper.selectObjectById(id));
	}

	/**
	 * ????????????
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R saveBatch(JSONObject jsonObject, String loginUserId) {
		String pkgId = jsonObject.getString("pkgId");
		if (StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("??????????????????");
		}
		JSONArray jsonArray = jsonObject.getJSONArray("list");
		/*if (jsonArray == null || jsonArray.size() == 0) {
			return R.error("??????????????????????????????????????????");
		}*/
		// ?????????????????????
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pkgId", pkgId);
		List<TevglPkgDefaultChapter> existedList = tevglPkgDefaultChapterMapper.selectListByMap(map);
		// ?????????
		if (existedList.size() > 0) {
			List<String> seIdList = existedList.stream().map(a -> a.getSeId()).collect(Collectors.toList());
			tevglPkgDefaultChapterMapper.deleteBatch(seIdList.stream().toArray(String[]::new));
		}
		// ???????????????
		List<String> nameList = new ArrayList<String>();
		List<TevglPkgDefaultChapter> insertList = new ArrayList<TevglPkgDefaultChapter>();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject obj = jsonArray.getJSONObject(i);
			String name = obj.getString("name");
			name = name.trim();
			if (StrUtils.isEmpty(name)) {
				continue;
			}
			if (!nameList.contains(name)) {
				nameList.add(name);
			}
		}
		String msg = "????????????";
		if (nameList.size() > 0) {
			for (int i = 0; i < nameList.size(); i++) {
				TevglPkgDefaultChapter t = new TevglPkgDefaultChapter();
				t.setSeId(Identities.uuid());
				t.setPkgId(pkgId);
				t.setName(nameList.get(i));
				t.setCreateTime(DateUtils.getNowTimeStamp());
				t.setCreateUserId(loginUserId);
				t.setSortNum(i);
				insertList.add(t);
			}
			if (insertList.size() > 0) {
				tevglPkgDefaultChapterMapper.insertBatch(insertList);
			}
			if (jsonArray.size() > 0 && jsonArray.size() != nameList.size()) {
				msg = "????????????????????????????????????????????????";
			}
		}
		return R.ok(msg);
	}

	/**
	 * ?????????????????????????????????
	 * @param pkgId
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R queryDefaultNameList(String pkgId, String loginUserId) {
		if (StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("??????????????????");
		}
		TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (tevglPkgInfo == null) {
			return R.error("????????????");
		}
		// ????????????????????????????????????
		boolean hasOperatingAuthorization = pkgUtils.hasOperatingAuthorization(tevglPkgInfo, loginUserId);
		if (!hasOperatingAuthorization) {
			return R.error("????????????????????????????????????");
		}
		Map<String, Object> map = new HashMap<>();
		map.put("pkgId", pkgId);
		map.put("sidx", "sort_num");
		map.put("order", "asc");
		List<Map<String,Object>> list = tevglPkgDefaultChapterMapper.selectListMapByMap(map);
		return R.ok().put(Constant.R_DATA, list);
	}
}
