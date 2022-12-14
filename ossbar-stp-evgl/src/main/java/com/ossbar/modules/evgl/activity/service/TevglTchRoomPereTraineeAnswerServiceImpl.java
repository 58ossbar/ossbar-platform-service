package com.ossbar.modules.evgl.activity.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.github.pagehelper.PageHelper;
import com.ossbar.common.cbsecurity.logs.annotation.SysLog;
import com.ossbar.common.exception.CreatorblueException;
import com.ossbar.common.utils.ConvertUtil;
import com.ossbar.common.utils.PageUtils;
import com.ossbar.common.utils.Query;
import com.ossbar.common.utils.ServiceLoginUtil;
import com.ossbar.core.baseclass.domain.R;
import com.ossbar.modules.evgl.activity.api.TevglTchRoomPereTraineeAnswerService;
import com.ossbar.modules.evgl.tch.domain.TevglTchClassroom;
import com.ossbar.modules.evgl.tch.domain.TevglTchRoomPereAnswer;
import com.ossbar.modules.evgl.tch.domain.TevglTchRoomPereTraineeAnswer;
import com.ossbar.modules.evgl.tch.persistence.TevglTchClassroomMapper;
import com.ossbar.modules.evgl.tch.persistence.TevglTchClassroomTraineeMapper;
import com.ossbar.modules.evgl.tch.persistence.TevglTchRoomPereAnswerMapper;
import com.ossbar.modules.evgl.tch.persistence.TevglTchRoomPereTraineeAnswerMapper;
import com.ossbar.platform.core.common.utils.UploadFileUtils;
import com.ossbar.utils.constants.Constant;
import com.ossbar.utils.tool.DateUtils;
import com.ossbar.utils.tool.Identities;
import com.ossbar.utils.tool.StrUtils;
import org.apache.dubbo.config.annotation.Service;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
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
@RequestMapping("/trainee/tevgltchroomperetraineeanswer")
public class TevglTchRoomPereTraineeAnswerServiceImpl implements TevglTchRoomPereTraineeAnswerService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglTchRoomPereTraineeAnswerServiceImpl.class);
	@Autowired
	private TevglTchRoomPereTraineeAnswerMapper tevglTchRoomPereTraineeAnswerMapper;
	@Autowired
	private TevglTchRoomPereAnswerMapper tevglTchRoomPereAnswerMapper;
	@Autowired
	private TevglTchClassroomMapper tevglTchClassroomMapper;
	@Autowired
	private TevglTchClassroomTraineeMapper tevglTchClassroomTraineeMapper;
	@Autowired
	private ConvertUtil convertUtil;
	@Autowired
	private ServiceLoginUtil serviceLoginUtil;
	@Autowired
	private UploadFileUtils uploadPathUtils;
	@Autowired
	private RedissonClient redisson;
	@Autowired
	private StringRedisTemplate redisTemplate;
	
	/**
	 * ????????????(??????List<Bean>)
	 * @param params
	 * @return R
	 */
	@SysLog(value="????????????(??????List<Bean>)")
	@GetMapping("/query")
	@SentinelResource("/trainee/tevgltchroomperetraineeanswer/query")
	public R query(@RequestParam Map<String, Object> params) {
		// ????????????????????????Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<TevglTchRoomPereTraineeAnswer> tevglTchRoomPereTraineeAnswerList = tevglTchRoomPereTraineeAnswerMapper.selectListByMap(query);
		convertUtil.convertUserId2RealName(tevglTchRoomPereTraineeAnswerList, "createUserId", "updateUserId");
		convertUtil.convertUserId2RealName(tevglTchRoomPereTraineeAnswerList, "createUserId", "updateUserId");
		PageUtils pageUtil = new PageUtils(tevglTchRoomPereTraineeAnswerList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	
	/**
	 * ????????????(??????List<Map<String, Object>)
	 * @param params
	 * @return R
	 */
	@SysLog(value="????????????(??????List<Map<String, Object>)")
	@GetMapping("/queryForMap")
	@SentinelResource("/trainee/tevgltchroomperetraineeanswer/queryForMap")
	public R queryForMap(@RequestParam Map<String, Object> params) {
		// ????????????????????????Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<Map<String, Object>> tevglTchRoomPereTraineeAnswerList = tevglTchRoomPereTraineeAnswerMapper.selectListMapByMap(query);
		convertUtil.convertUserId2RealName(tevglTchRoomPereTraineeAnswerList, "create_user_id", "update_user_id");
		PageUtils pageUtil = new PageUtils(tevglTchRoomPereTraineeAnswerList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * ??????
	 * @param tevglTchRoomPereTraineeAnswer
	 * @throws CreatorblueException
	 */
	@SysLog(value="??????")
	@PostMapping("save")
	@SentinelResource("/trainee/tevgltchroomperetraineeanswer/save")
	public R save(@RequestBody(required = false) TevglTchRoomPereTraineeAnswer tevglTchRoomPereTraineeAnswer) throws CreatorblueException {
		tevglTchRoomPereTraineeAnswer.setTraineeAnswerId(Identities.uuid());
		tevglTchRoomPereTraineeAnswer.setCreateUserId(serviceLoginUtil.getLoginUserId());
		tevglTchRoomPereTraineeAnswer.setCreateTime(DateUtils.getNowTimeStamp());
		//ValidatorUtils.check(tevglTchRoomPereTraineeAnswer);
		tevglTchRoomPereTraineeAnswerMapper.insert(tevglTchRoomPereTraineeAnswer);
		return R.ok();
	}
	/**
	 * ??????
	 * @param tevglTchRoomPereTraineeAnswer
	 * @throws CreatorblueException
	 */
	@SysLog(value="??????")
	@PostMapping("update")
	@SentinelResource("/trainee/tevgltchroomperetraineeanswer/update")
	public R update(@RequestBody(required = false) TevglTchRoomPereTraineeAnswer tevglTchRoomPereTraineeAnswer) throws CreatorblueException {
	    tevglTchRoomPereTraineeAnswer.setUpdateUserId(serviceLoginUtil.getLoginUserId());
	    tevglTchRoomPereTraineeAnswer.setUpdateTime(DateUtils.getNowTimeStamp());
	    //ValidatorUtils.check(tevglTchRoomPereTraineeAnswer);
		tevglTchRoomPereTraineeAnswerMapper.update(tevglTchRoomPereTraineeAnswer);
		return R.ok();
	}
	/**
	 * ????????????
	 * @param id
	 * @throws CreatorblueException
	 */
	@SysLog(value="????????????")
	@GetMapping("delete/{id}")
	@SentinelResource("/trainee/tevgltchroomperetraineeanswer/delete")
	public R delete(@PathVariable("id") String id) throws CreatorblueException {
		tevglTchRoomPereTraineeAnswerMapper.delete(id);
		return R.ok();
	}
	/**
	 * ????????????
	 * @param ids
	 * @throws CreatorblueException
	 */
	@SysLog(value="????????????")
	@PostMapping("deleteBatch")
	@SentinelResource("/trainee/tevgltchroomperetraineeanswer/deleteBatch")
	public R deleteBatch(@RequestBody(required = true) String[] ids) throws CreatorblueException {
		tevglTchRoomPereTraineeAnswerMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * ????????????
	 * @param id
	 * @throws CreatorblueException
	 */
	@SysLog(value="????????????")
	@GetMapping("view/{id}")
	@SentinelResource("/trainee/tevgltchroomperetraineeanswer/view")
	public R view(@PathVariable("id") String id) {
		return R.ok().put(Constant.R_DATA, tevglTchRoomPereTraineeAnswerMapper.selectObjectById(id));
	}

	/**
	 * ???????????????????????????
	 * @author zhouyl???
	 * @date 2020???12???24???
	 * @param ctId ??????id
	 * @param activityId ??????id
	 * @param loginUserId ????????????id
	 * @return
	 */
	@Override
	@PostMapping("traineeAnswer")
	@Transactional
	public R traineeAnswer(String ctId, String activityId, String loginUserId) {
		if (StrUtils.isEmpty(ctId) || StrUtils.isEmpty(activityId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("??????????????????");
		}
		TevglTchClassroom classroom = tevglTchClassroomMapper.selectObjectById(ctId);
		if (classroom == null) {
			return R.error("????????????????????????????????????");
		}
		if ("1".equals(classroom.getClassroomState())) {
			return R.error("?????????????????????????????????");
		}
		if ("3".equals(classroom.getClassroomState())) {
			return R.error("????????????????????????????????????");
		}
		// ??????????????????id??????????????????????????????????????????????????????????????????????????????
		TevglTchRoomPereAnswer pereAnswer = tevglTchRoomPereAnswerMapper.selectObjectById(activityId);
		if (pereAnswer == null) {
			return R.error("?????????????????????????????????~");
		}
		if (pereAnswer.getType() == null || !pereAnswer.getType().equals(1)) {
			return R.error("??????????????????????????????");
		}
		// ??????????????????
		List<String> traineeIdList = tevglTchClassroomTraineeMapper.findTraineeIdByCtId(ctId);
		if (!traineeIdList.contains(loginUserId)) {
			return R.error("?????????????????????????????????????????????");
		}
		boolean flag1 = false;
		boolean ifSuccess = false; // ????????????????????????
		// ?????????
		String key = "??????????????????" + ctId + "_" + activityId;
		RLock lock = redisson.getLock(key);
		lock.lock();
		try {
			// ?????????????????????????????????????????????
			Integer num = 0;
			String s = redisTemplate.opsForValue().get(getMyRedisKey(ctId, activityId));
			if (StrUtils.isEmpty(s)) {
				List<String> selectAnswerNums = tevglTchRoomPereTraineeAnswerMapper.selectTraineeIdList(ctId, activityId);
				Integer currentSuccessNum = (selectAnswerNums == null || selectAnswerNums.size() == 0) ? 0 : selectAnswerNums.size();
				redisTemplate.opsForValue().set(getMyRedisKey(ctId, activityId), String.valueOf(pereAnswer.getAnswerNum() - currentSuccessNum));
			} else {
				num = Integer.valueOf(s);
			}
			if (num > 0) {
				Integer val = tevglTchRoomPereTraineeAnswerMapper.countTraineeAnswer(ctId, activityId, loginUserId);
				if (val == null || val <= 0) {
					// ????????????
					TevglTchRoomPereTraineeAnswer traineeAnswer = traineeAnswerInfo(ctId, activityId, loginUserId);
					tevglTchRoomPereTraineeAnswerMapper.insert(traineeAnswer);
					// ??????????????????????????????
					redisTemplate.opsForValue().set(getMyRedisKey(ctId, activityId), String.valueOf(num - 1));
					ifSuccess = true;
					log.debug("{}??????????????????", loginUserId);
				} else {
					flag1 = true;
				}
			}
		} finally {
			lock.unlock();
		}
		if (flag1) {
			return R.ok("?????????????????????");
		}
		// ???????????????????????????????????????
		List<String> traineeIds = tevglTchRoomPereTraineeAnswerMapper.selectTraineeIdList(ctId, activityId);
		// ?????????????????????????????????
		List<Map<String, Object>> traineeList = getTraineeList(ctId, activityId, traineeIds);
		// ???????????????????????????????????????
		if (ifSuccess) {
			//sendIm(classroom, activityId, traineeList);
		}
		return R.ok("????????????????????????").put(Constant.R_DATA, traineeList);
	}
	
	/**
	 * ??????????????????key
	 * @param ctId
	 * @param activityId
	 * @return ??????????????????????????????????????????_c1e0397b5737483c8915b958f81b46c9_263148468eda4619ba4763be58ccf2e5
	 */
	private String getMyRedisKey(String ctId, String activityId) {
		ctId = StrUtils.isEmpty(ctId) ? "??????id" : ctId;
		activityId = StrUtils.isEmpty(activityId) ? "??????id" : activityId;
		return "???????????????????????????_" + ctId + "_" + activityId;
	}

	/**
	 * ????????????????????????????????????
	 * @param ctId
	 * @param activityId
	 * @param traineeIds
	 * @return
	 */
	private List<Map<String, Object>> getTraineeList(String ctId, String activityId, List<String> traineeIds) {
		if (StrUtils.isEmpty(ctId) || StrUtils.isEmpty(activityId) || traineeIds == null || traineeIds.size() == 0) {
			return new ArrayList<>();
		}
		Map<String, Object> params = new HashMap<>();
		params.put("ctId", ctId);
		params.put("traineeIds", traineeIds);
		List<Map<String, Object>> traineeList = tevglTchClassroomTraineeMapper.selectListMapForWeb(params);
		traineeList.stream().forEach(a -> {
			// ????????????
			String traineePic = (String) a.get("traineePic");
			if (StrUtils.isNotEmpty(traineePic) && !"null".equals(traineePic)) {
				a.put("traineePic", uploadPathUtils.stitchingPath(traineePic, "16"));
			}
			a.put("ct_id", ctId);
			a.put("ctId", ctId);
			a.put("activity_id", activityId);
			a.put("activityId", activityId);
		});
		return traineeList;
	}
	
	/**
	 * ??????????????????
	 * @param ctId
	 * @param activityId
	 * @param loginUserId
	 * @return
	 */
	public TevglTchRoomPereTraineeAnswer traineeAnswerInfo(String ctId, String activityId, String loginUserId) {
		TevglTchRoomPereTraineeAnswer traineeAnswer = new TevglTchRoomPereTraineeAnswer();
		traineeAnswer.setTraineeAnswerId(Identities.uuid());
		traineeAnswer.setCtId(ctId);
		traineeAnswer.setTraineeId(loginUserId);
		traineeAnswer.setAnserId(activityId);
		traineeAnswer.setTraineeEmpiricalValue(0);
		traineeAnswer.setCreateUserId(loginUserId);
		traineeAnswer.setCreateTime(DateUtils.getNowTimeStamp());
		return traineeAnswer;
	}

	/**
	 * ??????????????????????????????
	 * @param ctId
	 * @param activityId
	 * @param loginUserId
	 * @return
	 */
	@Override
	public R querySuccessfulTraineeList(String ctId, String activityId, String loginUserId) {
		if (StrUtils.isEmpty(ctId) || StrUtils.isEmpty(activityId) || StrUtils.isEmpty(loginUserId)) {
			return R.error("??????????????????");
		}
		TevglTchRoomPereAnswer tevglTchRoomPereAnswer = tevglTchRoomPereAnswerMapper.selectObjectById(activityId);
		if (tevglTchRoomPereAnswer == null) {
			return R.error("???????????????");
		}
		Map<String, Object> params = new HashMap<>();
		params.put("ctId", ctId);
		params.put("activityId", activityId);
		List<TevglTchRoomPereTraineeAnswer> list = tevglTchRoomPereTraineeAnswerMapper.selectListByMap(params);
		if (list == null || list.size() == 0) {
			return R.ok().put(Constant.R_DATA, list).put("num", tevglTchRoomPereAnswer.getAnswerNum());
		}
		List<String> traineeIds = list.stream().map(a -> a.getTraineeId()).collect(Collectors.toList());
		params.clear();
		params.put("traineeIds", traineeIds);
		List<Map<String, Object>> traineeInfoList = tevglTchClassroomTraineeMapper.selectListMapForWeb(params);
		traineeInfoList.stream().forEach(traineeInfo -> {
			traineeInfo.put("traineePic", uploadPathUtils.stitchingPath(traineeInfo.get("traineePic"), "16"));
		});
		return R.ok("??????????????????????????????").put(Constant.R_DATA, traineeInfoList).put("num", tevglTchRoomPereAnswer.getAnswerNum());
	}
}
