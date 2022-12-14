package com.ossbar.modules.evgl.trainee.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.github.pagehelper.PageHelper;
import com.ossbar.common.cbsecurity.logs.annotation.SysLog;
import com.ossbar.common.exception.CreatorblueException;
import com.ossbar.common.utils.*;
import com.ossbar.core.baseclass.domain.R;
import com.ossbar.modules.evgl.book.domain.TevglBookMajor;
import com.ossbar.modules.evgl.book.persistence.TevglBookMajorMapper;
import com.ossbar.modules.evgl.pkg.persistence.TevglPkgInfoMapper;
import com.ossbar.modules.evgl.tch.domain.TevglTchClassroom;
import com.ossbar.modules.evgl.tch.domain.TevglTchClassroomTrainee;
import com.ossbar.modules.evgl.tch.domain.TevglTchTeacher;
import com.ossbar.modules.evgl.tch.persistence.TevglTchClassroomMapper;
import com.ossbar.modules.evgl.tch.persistence.TevglTchClassroomTraineeMapper;
import com.ossbar.modules.evgl.tch.persistence.TevglTchClasstraineeMapper;
import com.ossbar.modules.evgl.tch.persistence.TevglTchTeacherMapper;
import com.ossbar.modules.evgl.trainee.api.TevglTraineeInfoService;
import com.ossbar.modules.evgl.trainee.domain.TevglTraineeInfo;
import com.ossbar.modules.evgl.trainee.dto.SaveTraineeDTO;
import com.ossbar.modules.evgl.trainee.persistence.TevglTraineeInfoMapper;
import com.ossbar.modules.evgl.trainee.vo.TraineeCharmInfoVO;
import com.ossbar.modules.evgl.trainee.vo.TraineeInfoVO;
import com.ossbar.modules.sys.api.TsysAttachService;
import com.ossbar.utils.constants.Constant;
import com.ossbar.utils.tool.DateUtils;
import com.ossbar.utils.tool.Identities;
import com.ossbar.utils.tool.StrUtils;
import com.ossbar.utils.tool.TicketDesUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
@RequestMapping("/trainee/tevgltraineeinfo")
public class TevglTraineeInfoServiceImpl implements TevglTraineeInfoService {

    @SuppressWarnings("unused")
    private Logger log = LoggerFactory.getLogger(TevglTraineeInfoServiceImpl.class);

    @Autowired
    private TevglTraineeInfoMapper tevglTraineeInfoMapper;
    @Autowired
    private TevglTchClasstraineeMapper tevglTchClasstraineeMapper;
    @Autowired
    private TevglTchClassroomMapper tevglTchClassroomMapper;
    @Autowired
    private TevglTchClassroomTraineeMapper tevglTchClassroomTraineeMapper;
    @Autowired
    private TevglPkgInfoMapper tevglPkgInfoMapper;
    @Autowired
    private TevglTchTeacherMapper tevglTchTeacherMapper;
    @Autowired
    private TevglBookMajorMapper tevglBookMajorMapper;

    @Autowired
    private TsysAttachService tsysAttachService;

    @Autowired
    private ConvertUtil convertUtil;
    @Autowired
    private ServiceLoginUtil serviceLoginUtil;
    @Autowired
    private UploadUtils uploadPathUtils;

    /**
     * ??????????????????(??????List<Bean>)
     * @param map
     * @return
     */
    @Override
    @GetMapping("/query")
    @SentinelResource("/trainee/tevgltraineeinfo/query")
    @SysLog(value="??????????????????(??????List<Bean>)")
    public R query(Map<String, Object> map) {
        // ????????????????????????Query
        Query query = new Query(map);
        PageHelper.startPage(query.getPage(),query.getLimit());
        List<TevglTraineeInfo> tevglTraineeInfoList = tevglTraineeInfoMapper.selectListByMap(query);
        convertUtil.convertUserId2RealName(tevglTraineeInfoList, "createUserId", "updateUserId");
        PageUtils pageUtil = new PageUtils(tevglTraineeInfoList,query.getPage(),query.getLimit());
        return R.ok().put(Constant.R_DATA, pageUtil);
    }

    /**
     * ????????????(??????List<Map<String, Object>)
     * @param map
     * @return R
     */
    @Override
    @GetMapping("/queryForMap")
    @SentinelResource("/trainee/tevgltraineeinfo/queryForMap")
    @SysLog(value="????????????(??????List<Map<String, Object>)")
    public R queryForMap(Map<String, Object> map) {
        // ????????????????????????Query
        Query query = new Query(map);
        PageHelper.startPage(query.getPage(),query.getLimit());
        List<Map<String, Object>> tevglTraineeInfoList = tevglTraineeInfoMapper.selectListMapByMap(query);
        convertUtil.convertUserId2RealName(tevglTraineeInfoList, "create_user_id", "update_user_id");
        PageUtils pageUtil = new PageUtils(tevglTraineeInfoList,query.getPage(),query.getLimit());
        return R.ok().put(Constant.R_DATA, pageUtil);
    }

    @Override
    @SysLog(value="??????")
    @PostMapping("save")
    @SentinelResource("/trainee/tevgltraineeinfo/save")
    public R save(TevglTraineeInfo tevglTraineeInfo) throws CreatorblueException {
        tevglTraineeInfo.setTraineeId(Identities.uuid());
        tevglTraineeInfo.setCreateUserId(serviceLoginUtil.getLoginUserId());
        tevglTraineeInfo.setCreateTime(DateUtils.getNowTimeStamp());
        //ValidatorUtils.check(tevglTraineeInfo);
        tevglTraineeInfoMapper.insert(tevglTraineeInfo);
        return R.ok();
    }

    /**
     * ??????
     * @param tevglTraineeInfo
     * @return
     * @throws CreatorblueException
     */
    @Override
    @SysLog(value="??????")
    public R update(TevglTraineeInfo tevglTraineeInfo) throws CreatorblueException {
        tevglTraineeInfo.setUpdateUserId(tevglTraineeInfo.getTraineeId());
        tevglTraineeInfo.setUpdateTime(DateUtils.getNowTimeStamp());
        tevglTraineeInfoMapper.update(tevglTraineeInfo);
        String attachId = tevglTraineeInfo.getAttachId();
        // ?????????????????????
        if (StrUtils.isNotEmpty(attachId)) {
            tsysAttachService.updateAttachForAdd(attachId, tevglTraineeInfo.getTraineeId(), "16");
        }
        return R.ok();
    }

    /**
     * ????????????
     * @param id
     * @throws CreatorblueException
     */
    @Override
    @SysLog(value="????????????")
    @GetMapping("delete/{id}")
    @SentinelResource("/trainee/tevgltraineeinfo/delete")
    public R delete(@PathVariable("id") String id) throws CreatorblueException {
        tevglTraineeInfoMapper.delete(id);
        return R.ok();
    }

    /**
     * ????????????
     * @param ids
     * @throws CreatorblueException
     */
    @SysLog(value="????????????")
    @PostMapping("deleteBatch")
    @SentinelResource("/trainee/tevgltraineeinfo/deleteBatch")
    @Override
    public R deleteBatch(@RequestBody String[] ids) throws CreatorblueException {
        tevglTraineeInfoMapper.deleteBatch(ids);
        return R.ok();
    }

    @Override
    @SysLog(value="????????????")
    @GetMapping("view/{id}")
    @SentinelResource("/trainee/tevgltraineeinfo/view")
    public R view(@PathVariable("id") String id) {
        TraineeInfoVO traineeInfoVO = this.selectTraineeVoById(id);
        return R.ok().put(Constant.R_DATA, traineeInfoVO);
    }


    /**
     * ??????????????????????????????????????????
     *
     * @param mobile
     * @return
     */
    @Override
    public TevglTraineeInfo selectByMobile(String mobile) {
        return tevglTraineeInfoMapper.selectByMobile(mobile);
    }

    /**
     * ????????????????????????
     *
     * @param params
     * @return
     */
    @Override
    public List<TevglTraineeInfo> selectListByMap(Map<String, Object> params) {
        return tevglTraineeInfoMapper.selectListByMap(params);
    }

    /**
     * ????????????id??????
     *
     * @param id
     * @return
     */
    @Override
    public TevglTraineeInfo selectObjectById(String id) {
        return tevglTraineeInfoMapper.selectObjectById(id);
    }

    /**
     * ????????????openid????????????
     *
     * @param openid
     * @return
     */
    @Override
    public TevglTraineeInfo selectObjectByOpenId(String openid) {
        return tevglTraineeInfoMapper.selectObjectByOpenId(openid);
    }

    @Override
    public TraineeInfoVO selectTraineeVoById(Object id) {
        TraineeInfoVO traineeInfo = tevglTraineeInfoMapper.selectTraineeVoById(id);
        if (traineeInfo != null) {
            // ????????????
            traineeInfo.setTraineePic(uploadPathUtils.stitchingPath(traineeInfo.getTraineePic(), "16"));
            // ??????????????????
            List<String> classIdList = tevglTchClasstraineeMapper.selectClassIdListByTraineeId(traineeInfo.getTraineeId());
            if (classIdList != null && classIdList.size() > 0) {
                traineeInfo.setClassId(classIdList.stream().collect(Collectors.joining(",")));
            }
        }
        return traineeInfo;
    }

    /**
     * ??????????????????????????????
     *
     * @param traineeId
     * @return
     */
    @Override
    public R viewTraineeInfo(String traineeId) {
        if (StrUtils.isEmpty(traineeId)) {
            return R.error("??????traineeId??????");
        }
        TevglTraineeInfo traineeInfo = tevglTraineeInfoMapper.selectObjectById(traineeId);
        if (traineeInfo == null) {
            return R.error("????????????????????????");
        }
        // ??????????????????
        Map<String, Object> result = new HashMap<String, Object>();
        // ????????????
        Map<String, Object> basicInfo = toGetBasicTraineeInfo(traineeInfo);
        // ????????????
        Map<String, Object> charmInfo = toGetCharmTraineeInfo(traineeInfo);
        // ????????????
        result.put("basicInfo", basicInfo);
        result.put("charmInfo", charmInfo);
        return R.ok().put(Constant.R_DATA, result);
    }

    /**
     * ????????????
     *
     * @param params
     * @return
     */
    @Override
    public R listTrainee(Map<String, Object> params) {
        Query query = new Query(params);
        List<Map<String,Object>> list = tevglTraineeInfoMapper.selectSimpleListByMap(query);
        list.stream().forEach(trainee -> {
            trainee.put("traineePic", uploadPathUtils.stitchingPath(trainee.get("traineePic"), "16"));
        });
        return R.ok().put(Constant.R_DATA, list);
    }

    /**
     * ??????????????????????????????
     *
     * @param traineeId
     * @return
     */
    @Override
    public R viewTraineeInfoForManagementPanel(String traineeId) {
        if (StrUtils.isEmpty(traineeId)) {
            return R.error("??????traineeId??????");
        }
        TevglTraineeInfo traineeInfo = tevglTraineeInfoMapper.selectObjectById(traineeId);
        if (traineeInfo == null) {
            return R.error("????????????????????????");
        }
        // ??????????????????
        Map<String, Object> result = new HashMap<String, Object>();
        // ????????????
        result.put("basicInfo", toGetBasicInfo(traineeInfo));
        // ????????????
        result.put("charmInfo", toGetCharmInfo(traineeInfo));
        // ????????????
        return R.ok().put(Constant.R_DATA, result);
    }

    /**
     * ????????????
     * @param traineeInfo
     * @return
     */
    private Map<String, Object> toGetBasicInfo(TevglTraineeInfo traineeInfo){
        if (traineeInfo == null) {
            return null;
        }
        Map<String, Object> info = new HashMap<>();
        String traineeId = traineeInfo.getTraineeId();
        // ????????????
        TevglTchTeacher teacherInfo = tevglTchTeacherMapper.selectObjectByTraineeId(traineeId);
        if (teacherInfo == null) {
            teacherInfo = new TevglTchTeacher();
            log.debug("?????? traineeId [" + traineeId +"], ???????????????");
        }
        // ??????????????????
        String traineeType = traineeInfo.getTraineeType();
        String traineeSex = traineeInfo.getTraineeSex();
        convertUtil.convertDict(traineeInfo, "traineeSex", "sex");
        convertUtil.convertDict(traineeInfo, "traineeType", "trainee_type");
        // ??????????????????
        convertUtil.convertOrgId(teacherInfo, "orgId");
        // ??????????????????
        convertUtil.convertOrgId(teacherInfo, "orgIdDepartment");
        // ????????????(????????????)
        String mainSubjects = "";
        if (StrUtils.isNotEmpty(teacherInfo.getMajorId())) {
            String[] split = teacherInfo.getMajorId().split(",");
            List<String> majorIds = Stream.of(split).collect(Collectors.toList());
            if (majorIds != null && majorIds.size() > 0) {
                Map<String, Object> map = new HashMap<>();
                map.put("majorIds", majorIds);
                List<TevglBookMajor> majorList = tevglBookMajorMapper.selectListByMap(map);
                mainSubjects = majorList.stream().map(a -> a.getMajorName()).collect(Collectors.joining(","));
            }
        }
        // ????????????,?????????????????????????????????
        String teacherPic = teacherInfo.getTeacherPic();
        if (StrUtils.isNotEmpty(teacherPic)) {
            teacherPic = uploadPathUtils.stitchingPath(teacherPic, "7");
        } else {
            // ????????????
            teacherPic = traineeInfo.getTraineeHead();
            // ??????????????????????????????, (???????????????????????????????????????)
            if (StrUtils.isNotEmpty(traineeInfo.getTraineePic())) {
                teacherPic = uploadPathUtils.stitchingPath(traineeInfo.getTraineePic(), "16");
            } else { // ?????????????????????????????????????????????
                info.put("teacherPic", "/uploads/defaulthead.png");
            }
        }
        // ????????????
        info.put("traineeId", traineeId); // ??????(????????????????????????????????????)
        info.put("traineePic", teacherPic); // ????????????????????????
        // ??????,????????????????????????????????????????????????????????????
        //info.put("traineeName", StrUtils.isEmpty(traineeInfo.getTraineeName()) ? traineeInfo.getNickName() : traineeInfo.getTraineeName());
        info.put("traineeName", traineeInfo.getTraineeName());
        info.put("nickName", traineeInfo.getNickName());
        info.put("teacherErtificateNumber", teacherInfo.getTeacherErtificateNumber()); // ?????????
        info.put("teacherPostName", teacherInfo.getPostName()); // ??????
        info.put("mainSubjects", mainSubjects); // ????????????
        info.put("jobNumber", teacherInfo.getJobNumber()); // ??????/??????
        info.put("school", teacherInfo.getOrgId()); // ????????????
        info.put("college", teacherInfo.getOrgIdDepartment()); // ????????????
        info.put("mobile", traineeInfo.getMobile()); // ??????
        info.put("email", traineeInfo.getEmail()); // ??????
        info.put("empiricalValue", handleEmpiricalValue(traineeInfo)); // ?????????
        info.put("traineeType", traineeType); // ????????????
        info.put("traineeTypeName", traineeInfo.getTraineeType()); // ??????????????????
        info.put("traineeSex", traineeSex); // ??????
        info.put("traineeSexName", traineeInfo.getTraineeSex());
        info.put("traineeQq", traineeInfo.getTraineeQq());
        info.put("traineeEducation", traineeInfo.getTraineeEducation());
        return info;
    }

    private TraineeCharmInfoVO toGetCharmInfo(TevglTraineeInfo traineeInfo) {
        if (traineeInfo == null) {
            return null;
        }
        String traineeId = traineeInfo.getTraineeId();
        TraineeCharmInfoVO vo = new TraineeCharmInfoVO();
        Map<String, Object> params = new HashMap<>();
        params.put("createUserId", traineeId);
        List<TevglTchClassroom> classroomList = tevglTchClassroomMapper.selectListByMap(params);
        // ???????????????
        Integer studentNum = tevglTchClassroomTraineeMapper.countStudentNumByTraineeId(traineeId);
        // ??????????????????
        Integer resourceNum = tevglPkgInfoMapper.countPkgResCount(traineeId);
        // ??????????????????
        Integer activityNum = tevglPkgInfoMapper.countPkgActCount(traineeId);
        // ?????????????????????
        vo.setEmpiricalValue(handleEmpiricalValue(traineeInfo));
        vo.setCloudClassroomNum(classroomList == null ? 0 : classroomList.size());
        vo.setStudentNum(studentNum == null ? 0 : studentNum);
        vo.setResourceNum(resourceNum == null ? 0 : resourceNum);
        vo.setActivityNum(activityNum == null ? 0 : activityNum);
        vo.setBlogsNum(StrUtils.isNull(traineeInfo.getBlogsNum()) ? 0 : traineeInfo.getBlogsNum());
        vo.setCharmValue(0);
        return vo;
    }

    /**
     * ?????????
     * @param traineeInfo
     * @return
     */
    private Integer handleEmpiricalValue(TevglTraineeInfo traineeInfo) {
        Integer empiricalValue = tevglTraineeInfoMapper.countEmpiricalValue(traineeInfo.getTraineeId());
        // ????????????????????????????????????????????????
        if (empiricalValue != null && traineeInfo.getEmpiricalValue() != null && !empiricalValue.equals(traineeInfo.getEmpiricalValue())) {
            TevglTraineeInfo t = new TevglTraineeInfo();
            t.setTraineeId(traineeInfo.getTraineeId());
            t.setEmpiricalValue(empiricalValue);
            tevglTraineeInfoMapper.update(t);
        }
        return empiricalValue;
    }

    /**
     * ????????????????????????
     * @param traineeInfo
     * @return
     * @apiNote
     * {
     * traineeId???????????????traineeName???????????????traineeSex?????????mobile???????????????className????????????
     * traineeSchool???????????????traineePic?????????studentNumber??????
     * }
     */
    private Map<String, Object> toGetBasicTraineeInfo(TevglTraineeInfo traineeInfo) {
        if (traineeInfo == null) {
            traineeInfo = new TevglTraineeInfo();
        }
        String traineeId = traineeInfo.getTraineeId();
        String traineeSex = traineeInfo.getTraineeSex();
        convertUtil.convertDict(traineeInfo, "traineeSex", "sex");
        Map<String, Object> info = new HashMap<>();
        Map<String, Object> params = new HashMap<>();
        // ????????????
        params.put("traineeId", traineeId);
        List<Map<String, Object>> list = tevglTchClasstraineeMapper.selectListMapForWeb(params);
        if (list != null && list.size() > 0) {
            info.put("classId", list.get(0).get("classId"));
            info.put("className", list.get(0).get("className"));
        }
        // ????????????
        String traineePic = traineeInfo.getTraineePic();
        if (StrUtils.isNotEmpty(traineePic)) {
            //traineePic = creatorblueFieAccessPath + uploadPathUtils.getPathByParaNo("16") + "/" + traineePic;
            traineePic = uploadPathUtils.stitchingPath(traineePic, "16");
        } else {
            traineePic = traineeInfo.getTraineeHead();
        }
        // ?????????????????????
        info.put("traineeId", traineeInfo.getTraineeId());
        //info.put("traineeName", StrUtils.isEmpty(traineeInfo.getTraineeName()) ? traineeInfo.getNickName() : traineeInfo.getTraineeName());
        info.put("traineeName", traineeInfo.getTraineeName());
        info.put("nickName", traineeInfo.getNickName());
        info.put("traineeSex", traineeSex);
        info.put("traineeSexName", traineeInfo.getTraineeSex());
        info.put("mobile", traineeInfo.getMobile());
        info.put("traineePic", traineePic);
        info.put("traineeSchool", traineeInfo.getTraineeSchool());
        info.put("traineePic", traineePic);
        info.put("jobNumber", traineeInfo.getJobNumber());
        // TODO ???????????????????????????????????????????????????
        info.put("empiricalValue", handleEmpiricalValue(traineeInfo));
        info.put("email", traineeInfo.getEmail());
        info.put("traineeQq", traineeInfo.getTraineeQq());
        info.put("traineeEducation", traineeInfo.getTraineeEducation());
        return info;
    }

    /**
     * ????????????????????????
     * @param traineeInfo
     * @return
     */
    private Map<String, Object> toGetCharmTraineeInfo(TevglTraineeInfo traineeInfo) {
        Map<String, Object> info = new HashMap<>();
        // ??????????????????
        //String cloudClassroomNum = "0";
        info.clear();
        info.put("traineeId", traineeInfo.getTraineeId());
        info.put("state", "Y");
        List<TevglTchClassroomTrainee> list = tevglTchClassroomTraineeMapper.selectListByMap(info);
        long count = list.stream().map(a -> a.getCtId()).distinct().count();
        // ??????????????????
        info.put("cloudClassroomNum", count);
        info.put("studentNum", 0);
        info.put("resourceNum", 0);
        info.put("activityNum", 0);
        info.put("blogsNum", StrUtils.isNull(traineeInfo.getBlogsNum()) ? 0 : traineeInfo.getBlogsNum());
        info.put("charmValue", 0);
        info.put("empiricalValue", handleEmpiricalValue(traineeInfo));
        return info;
    }


    /**
     * ????????????
     *
     * @param oldPwd    ?????????
     * @param newPwd    ?????????
     * @param confimPwd ?????????????????????????????????
     * @param logUserId ???????????????????????????
     * @return
     */
    @Override
    public R updatePassword(String oldPwd, String newPwd, String confimPwd, String logUserId) {
        // ??????
        oldPwd = oldPwd.trim();
        newPwd = newPwd.trim();
        confimPwd = newPwd.trim();
        if (StrUtils.isEmpty(oldPwd.trim())) {
            return R.error("????????????????????????");
        }
        if (oldPwd.length() > 16) {
            return R.error("???????????????????????????16???");
        }
        if (StrUtils.isEmpty(newPwd.trim()) || StrUtils.isEmpty(confimPwd.trim())) {
            return R.error("??????????????????????????????????????????");
        }
        if (newPwd.length() > 16 || confimPwd.length() > 16) {
            return R.error("?????????????????????????????????????????????16???");
        }
        if (!newPwd.equals(confimPwd)) {
            return R.error("?????????????????????????????????");
        }
        TevglTraineeInfo traineeInfo = tevglTraineeInfoMapper.selectObjectById(logUserId);
        // ????????????????????????????????????????????????????????????????????????????????????????????????????????????
        if (traineeInfo == null) {
            return R.error("??????????????????????????????");
        }else {
            // ???????????????????????????????????????
            String originalPassword = TicketDesUtil.encryptWithMd5(oldPwd, traineeInfo.getUserYan());
            if (!originalPassword.equals(traineeInfo.getUserPasswd())) {
                return R.error("????????????????????????????????????");
            }
        }
        // ?????????????????????????????????
        if (traineeInfo != null) {
            TevglTraineeInfo userInfo = new TevglTraineeInfo();
            if (StrUtils.isNotEmpty(newPwd)) {
                String salt = RandomStringUtils.randomAlphanumeric(24);
                userInfo.setUserPasswd(TicketDesUtil.encryptWithMd5(newPwd, salt));  // ??????????????????
                userInfo.setUserYan(salt);  										 // ?????????
                userInfo.setTraineeId(logUserId);
                tevglTraineeInfoMapper.update(userInfo);
            }
        }
        return R.ok("??????????????????");
    }

    /**
     * ??????????????????
     *
     * @param dto
     * @return
     */
    @Override
    public R updatePersonInfo(SaveTraineeDTO dto) {
        if (StrUtils.isEmpty(dto.getTraineeName())) {
            dto.setTraineeName(dto.getTeacherName());
        }
        //ValidatorUtils.check(dto);
        TevglTraineeInfo t = new TevglTraineeInfo();
        t.setTraineeId(dto.getTraineeId());
        t.setTraineeName(dto.getTraineeName());
        t.setNickName(dto.getNickName());
        t.setTraineeSex(dto.getTraineeSex());
        t.setJobNumber(dto.getJobNumber());
        t.setEmail(dto.getEmail());
        tevglTraineeInfoMapper.update(t);
        // ????????????????????????
        TevglTchTeacher existedTevglTchTeacher = tevglTchTeacherMapper.selectObjectByTraineeId(dto.getTraineeId());
        if (existedTevglTchTeacher != null) {
            TevglTchTeacher o = new TevglTchTeacher();
            o.setTeacherId(existedTevglTchTeacher.getTeacherId());
            o.setTeacherName(dto.getTeacherName());
            o.setSex(dto.getTraineeSex());
            o.setJobNumber(dto.getJobNumber());
            o.setTeacherErtificateNumber(dto.getTeacherErtificateNumber());
            tevglTchTeacherMapper.update(o);
        }
        return R.ok("????????????");
    }

    /**
     * ????????????????????????????????????????????????????????????????????????????????????????????????????????????
     *
     * @param map
     * @return
     */
    @Override
    public List<TraineeInfoVO> findTraineeListNotInClass(Map<String, Object> map) {
        List<TraineeInfoVO> traineeList = tevglTraineeInfoMapper.findTraineeListNotInClass(map);
        if (traineeList != null && traineeList.size() > 0) {
            traineeList.stream().forEach(trainee -> {
                trainee.setTraineePic(uploadPathUtils.stitchingPath(trainee.getTraineePic(), "16"));
            });
        }
        return traineeList;
    }

    /**
     * ???????????????????????????????????????????????????????????????
     *
     * @param map
     * @return
     */
    @Override
    public R findTraineeListNotInClassWithPage(Map<String, Object> map) {
        Query query = new Query(map);
        PageHelper.startPage(query.getPage(), query.getLimit());
        List<TraineeInfoVO> list = this.findTraineeListNotInClass(query);
        PageUtils pageUtil = new PageUtils(list, query.getLimit(), query.getLimit());
        return R.ok().put(Constant.R_DATA, pageUtil);
    }

}
