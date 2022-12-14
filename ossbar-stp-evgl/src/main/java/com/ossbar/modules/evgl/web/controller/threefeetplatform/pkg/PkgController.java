package com.ossbar.modules.evgl.web.controller.threefeetplatform.pkg;

import com.alibaba.fastjson.JSONObject;
import com.ossbar.core.baseclass.domain.R;
import com.ossbar.modules.common.CbUploadUtils;
import com.ossbar.modules.evgl.common.CheckSession;
import com.ossbar.modules.evgl.common.EvglGlobal;
import com.ossbar.modules.evgl.common.LoginUtils;
import com.ossbar.modules.evgl.pkg.api.ReleaseTeachingPackageService;
import com.ossbar.modules.evgl.pkg.api.TevglPkgCheckService;
import com.ossbar.modules.evgl.pkg.api.TevglPkgDefaultChapterService;
import com.ossbar.modules.evgl.pkg.api.TevglPkgInfoService;
import com.ossbar.modules.evgl.pkg.domain.TevglPkgInfo;
import com.ossbar.modules.evgl.trainee.domain.TevglTraineeInfo;
import com.ossbar.utils.constants.Constant;
import com.ossbar.utils.tool.StrUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的教学包
 * @author huj
 * @create 2022-09-16 9:20
 * @email hujun@creatorblue.com
 * @company 创蓝科技 www.creatorblue.com
 */
@RestController
@RequestMapping("/pkginfo-api")
public class PkgController {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Reference(version = "1.0.0")
    private TevglPkgInfoService tevglPkgInfoService;
    @Reference(version = "1.0.0")
    private TevglPkgDefaultChapterService tevglPkgDefaultChapterService;
    @Reference(version = "1.0.0")
    private ReleaseTeachingPackageService releaseTeachingPackageService;
    @Reference(version = "1.0.0")
    private TevglPkgCheckService tevglPkgCheckService;

    @Autowired
    private CbUploadUtils uploadUtils;

    /**
     * 获取筛选条件
     * @return
     */
    @RequestMapping("/getFilterTypeList")
    public R getFilterTypeList () {
        List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
        Map<String, Object> v0 = new HashMap<String, Object>();
        v0.put("label", "全部状态");
        v0.put("value", "");
        Map<String, Object> v1 = new HashMap<String, Object>();
        v1.put("label", "自建");
        v1.put("value", "ower");
        Map<String, Object> v2 = new HashMap<String, Object>();
        v2.put("label", "共建");
        v2.put("value", "together");
        Map<String, Object> v3 = new HashMap<String, Object>();
        v3.put("label", "授权");
        v3.put("value", "auth");
        Map<String, Object> v4 = new HashMap<String, Object>();
        v4.put("label", "其它");
        v4.put("value", "other");
        list.add(v0);
        list.add(v1);
        list.add(v2);
        list.add(v3);
        //list.add(v4);
        return R.ok().put(Constant.R_DATA, list);
    }

    /**
     * 我的教学包列表
     * @param request
     * @param params
     * @return
     */
    @GetMapping("/listMyPkgInfo")
    @CheckSession
    public R listMyPkgInfo(HttpServletRequest request, @RequestParam Map<String, Object> params) {
        TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
        if (traineeInfo == null) {
            return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
        }
        // 不展示该值为3的，可见性(来源字典:1私有or2公有3都不可见)
        params.put("displayNo", "3");
        String together = null;
        if (StrUtils.isNull(params.get("releaseStatus")) || "F".equals(params.get("releaseStatus"))) {
            together = "Y";
        }
        if (StrUtils.isNull(params.get("releaseStatus"))) {
            params.put("releaseStatus", "N");
        }
        params.put("createUserId", traineeInfo.getTraineeId());
        return tevglPkgInfoService.listMyPkgInfo(params, traineeInfo.getTraineeId(), together);
    }

    /**
     * 根据条件搜索课程
     * @param request
     * @param majorId
     * @return
     */
    @PostMapping("/selectSubjectRefList")
    @CheckSession
    public R selectSubjectRefList(HttpServletRequest request, String majorId) {
        TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
        if (traineeInfo == null) {
            return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("majorId", majorId);
        map.put("together", "Y");
        map.put("createUserId", traineeInfo.getTraineeId());
        map.put("loginUserId", traineeInfo.getTraineeId());
        map.put("state", "Y");
        return R.ok().put(Constant.R_DATA, tevglPkgInfoService.selectSubjectRefList(map));
    }

    /**
     * 保存教学包基础信息
     * @param request
     * @param tevglPkgInfo
     * @param file
     * @return
     */
    @RequestMapping("/saveInfo")
    @CheckSession
    public R saveInfo(HttpServletRequest request, TevglPkgInfo tevglPkgInfo,
                      @RequestPart(name = "file", required = false) MultipartFile file) {
        TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
        if (traineeInfo == null) {
            return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
        }
        try {
            String attachId = "";
            String fileName = "";
            if (file != null && !file.isEmpty()) {
                // 头像上传
                R r = uploadUtils.uploads(file, "12", null, "images");
                Integer code = (Integer) r.get("code");
                if (code != 0) {
                    return r;
                }
                @SuppressWarnings("unchecked")
                Map<String, Object> obj = (Map<String, Object>) r.get("data");
                attachId = (String) obj.get("attachId");
                fileName = (String) obj.get("fileName");
            }
            if (StrUtils.isNotEmpty(fileName)) {
                tevglPkgInfo.setPkgLogo(fileName);
            }
            if (StrUtils.isNotEmpty(attachId)) {
                tevglPkgInfo.setAttachId(attachId);
            }
            if (StrUtils.isEmpty(tevglPkgInfo.getPkgId())) {
                return tevglPkgInfoService.saveInfo(tevglPkgInfo, traineeInfo.getTraineeId());
            } else {
                return tevglPkgInfoService.updateInfo(tevglPkgInfo, traineeInfo.getTraineeId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("保存失败");
        }
    }

    /**
     * 查看教学包基础信息
     * @param request
     * @param pkgId
     * @return
     */
    @GetMapping("/view/{id}")
    @CheckSession
    public R viewPkgInfoForUpdate(HttpServletRequest request, @PathVariable("id") String pkgId) {
        TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
        if (traineeInfo == null) {
            return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
        }
        return tevglPkgInfoService.viewPkgInfoForUpdate(pkgId, traineeInfo.getTraineeId());
    }


    /**
     * 查看教学包基础信息
     * @param request
     * @param pkgId
     * @return
     */
    @GetMapping("/viewPkgInfo")
    @CheckSession
    public R viewPkgInfo(HttpServletRequest request, String pkgId) {
        TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
        if (traineeInfo == null) {
            return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
        }
        return tevglPkgInfoService.viewPkgBaseInfo(pkgId, traineeInfo.getTraineeId());
    }

    /**
     * 删除教学包
     * @param request
     * @param pkgId
     * @return
     */
    @PostMapping("/deletePkg")
    @CheckSession
    public R deletePkg(HttpServletRequest request, String pkgId, String temp) {
        TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
        if (traineeInfo == null) {
            return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
        }
        return R.error("暂不支持！");
    }

    /**
     * 发布教学包
     * @param request
     * @param jsonObject
     * @return
     */
    @PostMapping("/releaseTeachingPackage")
    @CheckSession
    public R releaseTeachingPackage(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
        TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
        if (traineeInfo == null) {
            return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
        }
        return releaseTeachingPackageService.releaseTeachingPackage(jsonObject, traineeInfo.getTraineeId());
    }

    @RequestMapping("/queryDataList")
    @CheckSession
    public R queryDataList(HttpServletRequest request, String pkgId) {
        TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
        if (traineeInfo == null) {
            return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
        }
        return releaseTeachingPackageService.queryDataList(pkgId, traineeInfo.getTraineeId());
    }

    /**
     * 检验当前源教学包是否有正在等待审核通过的衍生教学包
     * @param request
     * @param pkgId
     * @return
     */
    @PostMapping("/check")
    @CheckSession
    public R check(HttpServletRequest request, String pkgId) {
        TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
        if (traineeInfo == null) {
            return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
        }
        return releaseTeachingPackageService.check(pkgId, traineeInfo.getTraineeId());
    }

    @PostMapping("/querMyWaitCheckPkgList")
    @CheckSession
    public R querMyWaitCheckPkgList(HttpServletRequest request, String pkgId) {
        TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
        if (traineeInfo == null) {
            return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("pkgId", pkgId);
        return tevglPkgCheckService.querMyWaitCheckPkgList(params, traineeInfo.getTraineeId());
    }

    @PostMapping("/queryDefaultNameList")
    @CheckSession
    public R queryDefaultNameList(HttpServletRequest request, String pkgId) {
        TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
        if (traineeInfo == null) {
            return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
        }
        return tevglPkgDefaultChapterService.queryDefaultNameList(pkgId, traineeInfo.getTraineeId());
    }

    /**
     * 保存
     * @param request
     * @param jsonObject
     * @return
     */
    @PostMapping("/saveBatch")
    @CheckSession
    public R saveBatch(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
        TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
        if (traineeInfo == null) {
            return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
        }
        return tevglPkgDefaultChapterService.saveBatch(jsonObject, traineeInfo.getTraineeId());
    }

    /**
     * 获取当前教学包的直系父教学包下的直系子教学包们（即历史版本）
     * @param request
     * @param pkgId
     * @return
     */
    @RequestMapping("getHistoryPkgList")
    public R getHistoryPkgList(HttpServletRequest request, String pkgId) {
        TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
        if (traineeInfo == null) {
            return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
        }
        return tevglPkgInfoService.getHistoryPkgList(pkgId);
    }

    /**
     * 获取当前使用的教学包，所能升级的版本
     * @param pkgId
     * @return
     */
    @PostMapping("queryUpgradeList")
    @CheckSession
    public R queryUpgradeList(HttpServletRequest request, String ctId, String pkgId) {
        TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
        if (traineeInfo == null) {
            return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
        }
        return R.ok().put(Constant.R_DATA, new ArrayList<>());
    }

    /**
     * 教学包下拉列表（包含自己创建的、被授权的、以及免费的）（新增课堂页面中用到）
     * @param request
     * @param params {'subjectRef':''}
     * @return
     */
    @GetMapping("/listPkgInfoSelect")
    @CheckSession
    public R listPkgInfoSelect(HttpServletRequest request, @RequestParam Map<String, Object> params) {
        TevglTraineeInfo traineeInfo = LoginUtils.getLoginUser(request);
        if (traineeInfo == null) {
            return R.error(EvglGlobal.UN_LOGIN_MESSAGE);
        }
        return tevglPkgInfoService.queryPkgListByUnionAllForSelect(params, traineeInfo.getTraineeId());
    }
}
