package com.ossbar.modules.evgl.site.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.github.pagehelper.PageHelper;
import com.ossbar.common.cbsecurity.logs.annotation.SysLog;
import com.ossbar.common.exception.CreatorblueException;
import com.ossbar.common.utils.*;
import com.ossbar.core.baseclass.domain.R;
import com.ossbar.modules.evgl.site.api.TevglSiteResourceextService;
import com.ossbar.modules.evgl.site.domain.TevglSiteAvd;
import com.ossbar.modules.evgl.site.domain.TevglSiteResourceext;
import com.ossbar.modules.evgl.site.persistence.*;
import com.ossbar.modules.evgl.site.vo.TsysResourceVo;
import com.ossbar.modules.sys.domain.TsysDict;
import com.ossbar.modules.sys.domain.TsysResource;
import com.ossbar.modules.sys.persistence.TsysResourceMapper;
import com.ossbar.utils.constants.Constant;
import com.ossbar.utils.tool.DateUtils;
import com.ossbar.utils.tool.Identities;
import com.ossbar.utils.tool.StrUtils;
import org.apache.commons.lang3.StringUtils;
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
@RequestMapping("/site/tevglsiteresourceext")
public class TevglSiteResourceextServiceImpl implements TevglSiteResourceextService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglSiteResourceextServiceImpl.class);
	@Autowired
	private TevglSiteResourceextMapper tevglSiteResourceextMapper;
	@Autowired
	private TevglsiteResourceMapper tevglsiteResourceMapper;
	@Autowired
	private ConvertUtil convertUtil;
	@Autowired
	private TsysResourceMapper tsysResourceMapper;
	@Autowired
	private TevglSiteSeoMapper tevglSiteSeoMapper;
	@Autowired
	private ServiceLoginUtil serviceLoginUtil;
	@Autowired
	private DictUtil dictUtil;
	@Autowired
	private TevglSiteAvdMapper tevglSiteAvdMapper;
	@Autowired
	private TevglSiteNewsMapper tevglSiteNewsMapper;
	
	/**
	 * ????????????(??????List<Bean>)
	 * @param params
	 * @return R
	 */
	@SysLog(value="????????????(??????List<Bean>)")
	@GetMapping("/query")
	@SentinelResource("/site/tevglsiteresourceext/query")
	public R query(@RequestParam Map<String, Object> params) {
		// ????????????????????????Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<TevglSiteResourceext> tevglSiteResourceextList = tevglSiteResourceextMapper.selectListByMap(query);
		PageUtils pageUtil = new PageUtils(tevglSiteResourceextList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	
	/**
	 * ????????????(??????List<Map<String, Object>)
	 * @param params
	 * @return R
	 */
	@SysLog(value="????????????(??????List<Map<String, Object>)")
	@GetMapping("/queryForMap")
	@SentinelResource("/site/tevglsiteresourceext/queryForMap")
	public R queryForMap(@RequestParam Map<String, Object> params) {
		// ????????????????????????Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<Map<String, Object>> tevglSiteResourceextList = tevglSiteResourceextMapper.selectListMapByMap(query);
		PageUtils pageUtil = new PageUtils(tevglSiteResourceextList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * ??????
	 * @param tevglSiteResourceext
	 * @throws CreatorblueException
	 */
	@SysLog(value="??????")
	@PostMapping("save")
	@SentinelResource("/site/tevglsiteresourceext/save")
	public R save(@RequestBody(required = false) TevglSiteResourceext tevglSiteResourceext) throws CreatorblueException {
		tevglSiteResourceext.setSiteId(Identities.uuid());
		//ValidatorUtils.check(tevglSiteResourceext);
		tevglSiteResourceextMapper.insert(tevglSiteResourceext);
		return R.ok();
	}
	/**
	 * ??????
	 * @param tevglSiteResourceext
	 * @throws CreatorblueException
	 */
	@SysLog(value="??????")
	@PostMapping("update")
	@SentinelResource("/site/tevglsiteresourceext/update")
	public R update(@RequestBody(required = false) TevglSiteResourceext tevglSiteResourceext) throws CreatorblueException {
	    //ValidatorUtils.check(tevglSiteResourceext);
		tevglSiteResourceextMapper.update(tevglSiteResourceext);
		return R.ok();
	}
	/**
	 * ????????????
	 * @param id
	 * @throws CreatorblueException
	 */
	@SysLog(value="????????????")
	@GetMapping("delete/{id}")
	@SentinelResource("/site/tevglsiteresourceext/delete")
	public R delete(@PathVariable("id") String id) throws CreatorblueException {
		tevglSiteResourceextMapper.delete(id);
		return R.ok();
	}
	/**
	 * ????????????
	 * @param ids
	 * @throws CreatorblueException
	 */
	@SysLog(value="????????????")
	@PostMapping("deleteBatch")
	@SentinelResource("/site/tevglsiteresourceext/deleteBatch")
	public R deleteBatch(@RequestBody(required = true) String[] ids) throws CreatorblueException {
		tevglSiteResourceextMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * ????????????
	 * @param id
	 * @throws CreatorblueException
	 */
	@SysLog(value="????????????")
	@GetMapping("view/{id}")
	@SentinelResource("/site/tevglsiteresourceext/view")
	public R view(@PathVariable("id") String id) {
		return R.ok().put(Constant.R_DATA, tevglSiteResourceextMapper.selectObjectById(id));
	}
	
	/**
	 * <p>?????????????????????????????????</p>
	 * @author huj
	 * @data 2019???7???12???
	 * @param menuId
	 * @return
	 */
	@Override
	@SysLog(value="?????????????????????????????????")
	@GetMapping("/querySite")
	@SentinelResource("/site/tevglsiteresourceext/querySite")
	public List<TsysResourceVo> querySite(String menuId){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sidx", "order_num");
		map.put("order", "asc");
		List<TsysResourceVo> menuList = (menuId == null)? 
				tevglsiteResourceMapper.selectSiteListVoByMap(map)
				:tevglsiteResourceMapper.selectSiteListVoParentId(menuId);
		menuList.stream().forEach(item -> {
			Integer num = tevglSiteNewsMapper.countNewsNumByMenuId(item.getMenuId());
			item.setNum(num);
		});	
		// ?????????????????? ????????????:b43e1950c19d40deb8b9b701ffec5f65
		//List<TsysResource> list = build("b43e1950c19d40deb8b9b701ffec5f65", menuList, 0);
		return menuList;
	}

	/**
	 * <p>??????????????????</p>
	 * @author huj
	 * @data 2019???7???15???
	 * @param parentId ???ID
	 * @param allList ??????????????????
	 * @param level ??????
	 * @return
	 */
	public List<TsysResource> build(String parentId, List<TsysResource> allList, int level){
		if (allList == null || allList.size() == 0) {
			return new ArrayList<>();
		}
		List<TsysResource> parentNode = allList.stream().filter(a -> a.getParentId().equals(parentId)).collect(Collectors.toList());
		if (parentNode.size() > 0) {
			level ++; // level???????????????????????????
			final int level2 = level;
			parentNode.forEach(a -> {
				a.setLevel(level2);
				a.setChildren(build(a.getMenuId(), allList, level2));
			});
		}
		return parentNode;
	}
	
	/**
	 * <p>??????????????????</p>
	 * @author huj
	 * @data 2019???7???15???
	 * @param id
	 * @return
	 */
	@Override
	@GetMapping("/viewSite/{id}")
	@SysLog(value="??????????????????")
	public TsysResource viewSite(@PathVariable("id") String id) {
		TsysResource menu = tsysResourceMapper.selectObjectById(id);
		convertUtil.convertOrgId(menu, "orgId");
		List<TsysResource> menuList = tevglsiteResourceMapper.selectSiteListByMap(new HashMap<String, Object>());
		Map<String, TsysResource> map = new HashMap<String, TsysResource>();
		for (TsysResource res : menuList) {
			map.put(res.getMenuId(), res);
		}
		convertUtil.convertBean(menu, map, "name", "parentId");
		return menu;
	}

	/**
	 * <p>??????????????????</p>
	 * @author huj
	 * @data 2019???7???16???
	 * @param menu
	 * @return
	 */
	@Override
	@SysLog(value="??????????????????")
	@PostMapping("/saveOrUpdateSite")
	public R saveOrUpdateSite(@RequestBody(required = false) TsysResource menu) {
		verifyForm(menu);
		if(menu.getMenuId()==null || menu.getMenuId().length()==0){
			menu.setMenuId(Identities.uuid());
			menu.setCreateTime(DateUtils.getNowTimeStamp());
			menu.setCreateUserId(serviceLoginUtil.getLoginUserId());
			tsysResourceMapper.insert(menu);
			TevglSiteResourceext tevglSiteResourceext = new TevglSiteResourceext();
			tevglSiteResourceext.setMenuId(menu.getMenuId());
			tevglSiteResourceext.setSiteId(Identities.uuid());
			tevglSiteResourceextMapper.insert(tevglSiteResourceext);
		} else{
			menu.setUpdateUserId(serviceLoginUtil.getLoginUserId());
			menu.setUpdateTime(DateUtils.getNowTimeStamp());
			tsysResourceMapper.update(menu);
		}
		return R.ok().put("menuId", menu.getMenuId()).put("row", tsysResourceMapper.selectObjectById(menu.getParentId()));
	}

	/**
	 * <p>????????????</p>
	 * @author huj
	 * @data 2019???7???16???
	 * @param id
	 * @return
	 */
	@Override
	public R deleteSite(String id) {
		// ?????????????????????????????????
		List<TsysResource> menuList = tevglsiteResourceMapper.selectSiteListParentId(id);
		if(menuList.size() > 0){
			return R.error("??????????????????????????????");
		}
		// ???????????????????????????
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("menuId", id);
		List<TevglSiteAvd> avdList = tevglSiteAvdMapper.selectListByMap(map);
		if (avdList.size() > 0 && avdList != null) {
			return R.error("????????????????????????????????????????????????????????????????????????");
		}
		doDeleteSite(id);
		return R.ok();
	}
	
	/**
	 * <p>???????????????????????????</p>
	 * @author huj
	 * @data 2019???7???16???
	 * @param id
	 * @throws CreatorblueException
	 */
	public void doDeleteSite(String id) throws CreatorblueException {
		TevglSiteResourceext tevglSiteResourceext = tevglSiteResourceextMapper.selectObjectByMenuId(id);
		if (tevglSiteResourceext != null){
			// ????????????????????????
			tevglSiteResourceextMapper.delete(tevglSiteResourceext.getSiteId());
			// ????????????SEO
			tevglSiteSeoMapper.deleteByRelationId(tevglSiteResourceext.getSiteId());
		} else {
			// ????????????SEO
			tevglSiteSeoMapper.deleteByRelationId(id);
		}
		//??????????????????
		tsysResourceMapper.deleteBatchRolPri(new String[]{id});
		tsysResourceMapper.deleteBatchRes(new String[]{id});
	}

	/**
	 * <p>????????????,???????????????????????????</p>
	 * @author huj
	 * @data 2019???7???16???
	 * @return
	 */
	@Override
	public R getSiteType() {
		List<TsysDict> list = dictUtil.getByType("siteType");
		return R.ok().put(Constant.R_DATA, list);
	}
	
	/**
	 * ??????????????????????????????
	 */
	private void verifyForm(TsysResource menu){
		
		if(StringUtils.isBlank(menu.getName())){
			throw new CreatorblueException("??????????????????");
		}

		// ????????????????????????????????????????????????????????????????????????????????????
		Map<String, Object> map = new HashMap<>();
		map.put("parentId", menu.getParentId());
		if (StrUtils.isEmpty(menu.getMenuId())) { // ??????
			List<TsysResource> list = tsysResourceMapper.selectListParentId2(menu.getParentId());
			if (list.size() > 0) {
				list.forEach(a -> {
					if (a.getName().equals(menu.getName())) {
						throw new CreatorblueException("???????????????,???????????????????????????????????????");
					}
				});
				return ;
			}
		} else {
			TsysResource resource = tsysResourceMapper.selectObjectById(menu.getMenuId());
			if (resource != null) {
				if (!menu.getName().equals(resource.getName())) {
					List<TsysResource> list = tsysResourceMapper.selectListParentId2(menu.getParentId());
					if (list.size() > 0) {
						list.forEach(a -> {
							if (a.getName().equals(menu.getName()) && !a.getMenuId().equals(menu.getMenuId())) {
								throw new CreatorblueException("???????????????,???????????????????????????????????????");
							}
						});
						return ;
					}
				}
			}
		}
		
		//??????????????????(3??????4??????)
		int parentType = -1;
		if(menu.getParentId().length()>0 && !"0".equals(menu.getParentId())){
			TsysResource parentMenu = tsysResourceMapper.selectObjectById(menu.getParentId());
			parentType = parentMenu.getType();
		}
		//??????
		if(menu.getType() == 3){
			if(parentType == 4){
				throw new CreatorblueException("?????????????????????????????????????????????");
			}
			return ;
		}
		//??????
		if(menu.getType() == 4){
			if(parentType != 3 && parentType != 4 || parentType==-1){
				throw new CreatorblueException("?????????????????????????????????????????????");
			}
			return ;
		}
		
	}

	/**
	 * <p>????????????</p>
	 * @author huj
	 * @data 2019???7???17???
	 * @param id
	 * @return
	 */
	@Override
	public TsysResource editSite(String id) {
		return tsysResourceMapper.selectObjectById(id);
	}

	/**
	 * <p>????????????????????????,???IT?????????????????????</p>
	 * @author huj
	 * @data 2019???7???20???
	 * @return
	 */
	@Override
	public List<TsysDict> getTradeType() {
		List<TsysDict> list = dictUtil.getByType("tradeType");
		return list;
	}
	
}
