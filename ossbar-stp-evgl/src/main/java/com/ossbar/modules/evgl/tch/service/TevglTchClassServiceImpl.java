package com.ossbar.modules.evgl.tch.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.github.pagehelper.PageHelper;
import com.ossbar.common.cbsecurity.logs.annotation.SysLog;
import com.ossbar.common.exception.CreatorblueException;
import com.ossbar.common.utils.ConvertUtil;
import com.ossbar.common.utils.PageUtils;
import com.ossbar.common.utils.Query;
import com.ossbar.common.utils.ServiceLoginUtil;
import com.ossbar.common.validator.ValidatorUtils;
import com.ossbar.core.baseclass.domain.R;
import com.ossbar.modules.common.DictService;
import com.ossbar.modules.evgl.tch.api.TevglTchClassService;
import com.ossbar.modules.evgl.tch.domain.TevglTchClass;
import com.ossbar.modules.evgl.tch.persistence.TevglTchClassMapper;
import com.ossbar.modules.sys.api.TsysAttachService;
import com.ossbar.modules.sys.domain.TsysOrg;
import com.ossbar.modules.sys.persistence.TsysOrgMapper;
import com.ossbar.platform.core.common.utils.UploadFileUtils;
import com.ossbar.utils.constants.Constant;
import com.ossbar.utils.tool.DateUtils;
import com.ossbar.utils.tool.Identities;
import com.ossbar.utils.tool.StrUtils;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
public class TevglTchClassServiceImpl implements TevglTchClassService {

    @SuppressWarnings("unused")
    private Logger log = LoggerFactory.getLogger(TevglTchClassServiceImpl.class);
    @Autowired
    private TevglTchClassMapper tevglTchClassMapper;
    @Autowired
    private TsysOrgMapper tsysOrgMapper;

    @Autowired
    private ConvertUtil convertUtil;
    @Autowired
    private ServiceLoginUtil serviceLoginUtil;
    @Autowired
    private UploadFileUtils uploadPathUtils;

    @Value("${com.creatorblue.file-access-path}")
    public String creatorblueFieAccessPath;

    @Autowired
    private DictService dictService;
    @Autowired
    private TsysAttachService tsysAttachService;

    /**
     * ????????????????????????
     *
     * @param map
     * @return
     */
    @Override
    public R query(Map<String, Object> map) {
        // ????????????????????????Query
        Query query = new Query(map);
        PageHelper.startPage(query.getPage(),query.getLimit());
        List<TevglTchClass> tevglTchClassList = tevglTchClassMapper.selectListByMap(query);
        convertUtil.convertOrgId(tevglTchClassList, "orgId");
        convertUtil.convertUserId2RealName(tevglTchClassList, "createUserId", "updateUserId");
        convertUtil.convertUserId2RealName(tevglTchClassList, "createUserId", "updateUserId");
        convertUtil.convertDict(tevglTchClassList, "classState", "class_state");
        convertUtil.convertDict(tevglTchClassList, "type", "class_type");
        handleDatas(tevglTchClassList);
        PageUtils pageUtil = new PageUtils(tevglTchClassList,query.getPage(),query.getLimit());
        return R.ok().put(Constant.R_DATA, pageUtil);
    }


    private void handleDatas(List<TevglTchClass> tevglTchClassList) {
        if (tevglTchClassList == null || tevglTchClassList.size() == 0) {
            return;
        }
        tevglTchClassList.forEach(a -> {
            // ??????????????????
            handleClassPic(a);
            // ??????????????????
            if (StrUtils.isNotEmpty(a.getTeacherPic())) {
                a.setTeacherPic(uploadPathUtils.stitchingPath(a.getTeacherPic(), "7"));
            }
            // ??????????????????
            if (StrUtils.isNotEmpty(a.getTeachingAssistantPic())) {
                a.setTeachingAssistantPic(uploadPathUtils.stitchingPath(a.getTeachingAssistantPic(), "7"));
            }
            // ?????????????????????
            a.setRegistrationStartTime(getYearMonthDay(a.getRegistrationStartTime()));
            a.setExpectTime(getYearMonthDay(a.getExpectTime()));
        });
    }

    private String getYearMonthDay(String sourceStr) {
        if (StrUtils.isEmpty(sourceStr)) {
            return sourceStr;
        }
        String yearMonthDay = "";
        String[] split = sourceStr.split("-");
        if (split != null) {
            for (int i = 0; i < split.length; i++) {
                yearMonthDay += split[i];
            }
        }
        return yearMonthDay;
    }

    private void handleClassPic(TevglTchClass a) {
        if (a == null) {
            return;
        }
        // ????????????????????????
        if (StrUtils.isNotEmpty(a.getClassImg())) {
            a.setClassPic(uploadPathUtils.stitchingPath(a.getClassImg(), "13"));
            a.setClassImg(uploadPathUtils.stitchingPath(a.getClassImg(), "13"));
        } else {
            // ??????????????????????????????
            if (StrUtils.isNotEmpty(a.getClassPic())) {
                // ????????????????????????,???????????????
                if (a.getClassPic().indexOf("https") == -1 && a.getClassPic().indexOf("http") == -1) {
                    // ????????????uploads?????????
                    if (a.getClassPic().indexOf("uploads/dict") == -1) {
                        String val = creatorblueFieAccessPath + "/dict/" + a.getClassPic();
                        a.setClassPic(val);
                    }
                }
            }
        }
    }


    /**
     * ????????????????????????
     *
     * @param map
     * @return
     */
    @Override
    public R queryForMap(Map<String, Object> map) {
        return null;
    }

    /**
     * ??????
     *
     * @param tevglTchClass
     * @return
     */
    @Override
    public R save(TevglTchClass tevglTchClass) {
        String attachId = tevglTchClass.getAttachId();
        String id = Identities.uuid();
        tevglTchClass.setClassId(id);
        tevglTchClass.setCreateUserId(serviceLoginUtil.getLoginUserId());
        tevglTchClass.setCreateTime(DateUtils.getNowTimeStamp());
        ValidatorUtils.check(tevglTchClass);
        tevglTchClassMapper.insert(tevglTchClass);
        // ???????????????????????????
        tsysAttachService.updateAttachForAdd(attachId, id, "13");
        return R.ok();
    }

    /**
     * ??????
     *
     * @param tevglTchClass
     * @return
     */
    @Override
    public R update(TevglTchClass tevglTchClass) {
        return null;
    }

    /**
     * ??????
     *
     * @param id
     * @return
     */
    @Override
    @SysLog(value="????????????")
    @GetMapping("delete/{id}")
    @SentinelResource("/tch/tevgltchclass/delete")
    public R delete(@PathVariable("id") String id) throws CreatorblueException {
        tevglTchClassMapper.delete(id);
        return R.ok();
    }

    /**
     * ????????????
     *
     * @param ids
     * @return
     */
    @Override
    @SysLog(value="????????????")
    @PostMapping("deleteBatch")
    @SentinelResource("/tch/tevgltchclass/deleteBatch")
    public R deleteBatch(@RequestBody(required = true) String[] ids) throws CreatorblueException {
        tevglTchClassMapper.deleteBatch(ids);
        return R.ok();
    }

    /**
     * ????????????
     *
     * @param id
     * @return
     */
    @Override
    @SysLog(value="????????????")
    @GetMapping("view/{id}")
    @SentinelResource("/tch/tevgltchclass/view")
    public R view(@PathVariable("id") String id) {
        TevglTchClass tevglTchClass = tevglTchClassMapper.selectObjectById(id);
        if (tevglTchClass == null) {
            return R.ok().put(Constant.R_DATA, new TevglTchClass());
        }
        tevglTchClass.setTeacherPic(uploadPathUtils.stitchingPath(tevglTchClass.getTeacherPic(), "7"));
        tevglTchClass.setTeachingAssistantPic(uploadPathUtils.stitchingPath(tevglTchClass.getTeachingAssistantPic(), "7"));
        if (StrUtils.isNotEmpty(tevglTchClass.getClassImg())) {
            tevglTchClass.setClassImg(uploadPathUtils.stitchingPath(tevglTchClass.getClassImg(), "13"));
        }
        if (StrUtils.isNotEmpty(tevglTchClass.getClassPic())) {
            String name = tevglTchClass.getClassPic();
            // ????????????????????????,???????????????
            if (name.indexOf("https") == -1 && name.indexOf("http") == -1) {
                // ????????????uploads?????????
                if (name.indexOf("uploads/dict") == -1) {
                    String val = creatorblueFieAccessPath + "/dict/" + name;
                    tevglTchClass.setClassPic(val);
                }
            }
        }
        return R.ok().put(Constant.R_DATA, tevglTchClass);
    }


    /**
     * ????????????????????????
     *
     * @param params
     * @return
     */
    @Override
    public List<TevglTchClass> selectListByMap(Map<String, Object> params) {
        params.put("sidx", "create_time");
        params.put("order", "desc");
        Query query = new Query(params);
        return tevglTchClassMapper.selectListByMap(query);
    }

    /**
     * ??????????????????
     *
     * @param params
     * @param loginUserId
     * @return
     */
    @Override
    public R queryClassListData(Map<String, Object> params, String loginUserId) {
        if (StrUtils.isEmpty(loginUserId)) {
            return R.error("??????????????????");
        }
        if (StrUtils.isNull(params.get("majorId"))) {
            return R.ok().put(Constant.R_DATA, new ArrayList<>());
        }
        params.put("classState", "3"); // ??????3?????????????????????
        List<Map<String,Object>> list = tevglTchClassMapper.selectSimpleListMap(params);
        list.stream().forEach(item -> {
            String title = "";
            if (!StrUtils.isNull(item.get("orgParentName"))) {
                title += item.get("orgParentName");
            }
            if (!StrUtils.isNull(item.get("orgName"))) {
                title += " " + item.get("orgName");
            }
            title += " " + item.get("className");
            item.put("title", title);
        });
        return R.ok().put(Constant.R_DATA, list);
    }

    /**
     * ????????????????????????
     * @param params
     * @return
     */
    @Override
    public List<Map<String, Object>> selectSimpleListMap(Map<String, Object> params) {
        return tevglTchClassMapper.selectSimpleListMap(params);
    }

    /**
     * ???????????????????????????
     *
     * @param params
     * @return
     */
    @Override
    public R getClassTree(Map<String, Object> params) {
        // ????????????
        Object classState = params.get("classState");
        // ??????????????????
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        // element??????????????????????????????
        List<String> defaultExpandedKeys = new ArrayList<>();
        // ????????????
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sidx", "create_time");
        map.put("order", "desc");
        map.put("nonOrgType", "2");
        // ??????????????????
        List<TsysOrg> orglist = tsysOrgMapper.selectListByMap(map);
        for(TsysOrg org : orglist){
            Map<String,Object> m = new HashMap<String,Object>();
            m.put("id",org.getOrgId());
            m.put("name", org.getOrgName());
            m.put("parentId", org.getParentId());
            m.put("type", "01");
            list.add(m);
        }
        // ???????????????????????????????????????
        map.clear();
        if(classState != null && !"".equals(classState)){
            map.put("classState", classState);
        }
        List<TevglTchClass> classList = tevglTchClassMapper.selectListByMap(map);
        convertUtil.convertDict(classList, "classState", "class_state");
        Map<String,List<TevglTchClass>> yearList = classList.stream()
                .filter(a -> StrUtils.isNotEmpty(a.getAcceptTime()))
                .collect(Collectors.groupingBy(a -> ((TevglTchClass)a).getOrgId()+"#"+((TevglTchClass)a).getAcceptTime().substring(0, 4)));
        List<Map<String,Object>> tempList = new ArrayList<>();
        yearList.forEach((k,v) -> {
            Map<String,Object> m = new HashMap<String,Object>();
            m.put("id", k);
            m.put("name", k.split("#")[1]);
            m.put("parentId", k.split("#")[0]);
            m.put("type", "04");
            //list.add(m);
            tempList.add(m);
            defaultExpandedKeys.add(k);
            v.forEach(a -> {
                Map<String,Object> mm = new HashMap<String,Object>();
                mm.put("id", a.getClassId());
                mm.put("name", a.getClassName() + "("+a.getClassState()+")");
                mm.put("parentId", k);
                mm.put("type", "02");
                list.add(mm);
            });
        });
        // ?????????????????????
        List<Map<String, Object>> res = tempList.stream().sorted((h1, h2) -> h2.get("name").toString().compareTo(h1.get("name").toString())).collect(Collectors.toList());
        list.addAll(res);
        List<TevglTchClass> emptyList = classList.stream().filter(a -> StrUtils.isEmpty(a.getAcceptTime())).collect(Collectors.toList());
        for(TevglTchClass cc : emptyList){
            Map<String,Object> m = new HashMap<String,Object>();
            m.put("id", cc.getClassId());
            m.put("name", cc.getClassName() + "("+cc.getClassState()+")");
            m.put("parentId", cc.getOrgId());
            m.put("type", "02");
            list.add(m);
        }
        List<Map<String, Object>> resultList = buildTree("-1", list, 0);
        return R.ok().put(Constant.R_DATA, resultList)
                .put("defaultExpandedKeys", defaultExpandedKeys);
    }

    /**
     * ????????????????????????
     * @param parentId
     * @param allList
     * @param level
     * @return
     */
    private List<Map<String, Object>> buildTree(String parentId, List<Map<String, Object>> allList, int level) {
        if (allList == null || allList.size() == 0) {
            return null;
        }
        // ????????????????????????
        List<Map<String, Object>> nodeList = allList.stream().filter(a -> a.get("parentId").equals(parentId)).collect(Collectors.toList());
        if (nodeList != null && nodeList.size() > 0) {
            // level???????????????????????????
            level ++;
            for (int i = 0; i < nodeList.size(); i++) {
                Map<String, Object> node = nodeList.get(i);
                // ????????????
                node.put("level", level);
                // ??????
                List<Map<String, Object>> list = buildTree(node.get("id").toString(), allList, level);
                if (list != null && list.size() > 0) {
                    node.put("children", list);
                } else {
                    node.put("children", null);
                }
            }
        }
        return nodeList;
    }

    @Override
    public R getClassDictTypeList(Map<String, Object> params) {
        List<Map<String,Object>> dictList = dictService.getDictList("class_type");
        dictList.stream().forEach(item -> {
            Object type = item.get("dictCode");
            if (StrUtils.notNull(type)) {
                item.put("totalCount", tevglTchClassMapper.countClassNumByType(type));
            } else {
                item.put("totalCount", 0);
            }
        });
        return R.ok().put(Constant.R_DATA, dictList);
    }

    @Override
    public R queryClassListForWeb(Map<String, Object> params) {
        // ????????????????????????Query
        Query query = new Query(params);
        PageHelper.startPage(query.getPage(),query.getLimit());
        List<TevglTchClass> tevglTchClassList = tevglTchClassMapper.findClassListByMap(query);
        convertUtil.convertDict(tevglTchClassList, "classState", "class_state");
        handleDatas(tevglTchClassList);
        PageUtils pageUtil = new PageUtils(tevglTchClassList,query.getPage(),query.getLimit());
        return R.ok().put(Constant.R_DATA, pageUtil);
    }
}
