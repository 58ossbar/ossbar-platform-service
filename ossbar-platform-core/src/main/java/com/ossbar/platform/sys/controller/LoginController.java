package com.ossbar.platform.sys.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.ossbar.common.constants.ExecStatus;
import com.ossbar.core.baseclass.domain.R;
import com.ossbar.modules.sys.api.TsysLoginLogService;
import com.ossbar.modules.sys.api.TsysSettingsService;
import com.ossbar.modules.sys.api.TsysUserinfoService;
import com.ossbar.modules.sys.domain.TsysUserinfo;
import com.ossbar.modules.sys.vo.Oauth2ResponseVO;
import com.ossbar.modules.sys.vo.SysUserVO;
import com.ossbar.platform.core.common.cbsecurity.log.SysLog;
import com.ossbar.platform.core.common.handler.CustomResponseErrorHandler;
import com.ossbar.platform.core.common.utils.UploadFileUtils;
import com.ossbar.utils.constants.Constant;
import com.ossbar.utils.tool.BeanUtils;
import com.ossbar.utils.tool.StrUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Map;

/**
 * @author huj
 * @create 2019-05-13 10:19
 * @email hujun@creatorblue.com
 * @company ???????????? www.creatorblue.com
 */
@RestController
@RefreshScope
@RequestMapping("/user")
public class LoginController {

	private Logger log = LoggerFactory.getLogger(getClass());

	private final static String INDEX_USER = "2";

	@Reference(version = "1.0.0")
	private TsysSettingsService tsysSettingsService;
	@Autowired
	private Producer producer;
	@Autowired
	private RestTemplate restTemplate;
	@Reference(version = "1.0.0")
	private TsysUserinfoService tsysUserinfoService;
	@Reference(version = "1.0.0")
	private TsysLoginLogService tsysLoginLogService;

	@Value("${security.oauth2.client.access-token-uri:}")
	private String url;
	@Value("${security.oauth2.client.logout:}")
	private String logoutUrl;
	@Value("${security.oauth2.client.client-id:}")
	private String clientId;
	@Value("${security.oauth2.client.client-secret:}")
	private String clientSecret;

	@Autowired
	private UploadFileUtils uploadFileUtils;

	@RequestMapping("login")
	public R doLogin(@RequestBody JSONObject data, HttpSession session, HttpServletRequest request) throws Exception {
		String username = data.getString("username");
		String password = data.getString("password");
		String captcha = data.getString("captcha");
		if (StrUtils.isEmpty(username) || StrUtils.isEmpty(password)) {
			return R.error("???????????????????????????");
		}
		// TODO ??????????????????????????????????????????

		Object captchaSession = session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
		session.removeAttribute(Constants.KAPTCHA_SESSION_KEY);

		if (StrUtils.isNull(captchaSession) || StrUtils.isNull(captcha) || !captcha.equalsIgnoreCase(captchaSession.toString())) {
			String msg = "??????????????????";
			tsysLoginLogService.saveFailMessage(request, msg);
			return R.error(msg);
		}
		TsysUserinfo userInfo = tsysUserinfoService.selectObjectByUserName(username);
		if (userInfo == null) {
			tsysLoginLogService.saveFailMessage(request, ExecStatus.INCORRECT_ACCOUNT_PASSWORD.getMsg());
			return R.error(ExecStatus.INCORRECT_ACCOUNT_PASSWORD.getMsg());
		}
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("username", username);
		map.add("password", password);
		map.add("grant_type", "password");
		map.add("scope", "all");
		HttpHeaders header = new HttpHeaders();
		// ?????????????????????form-data??????
		header.setContentType(MediaType.MULTIPART_FORM_DATA);
		Encoder encoder = Base64.getEncoder();
		try {
			String basic = encoder.encodeToString((clientId + ":" + clientSecret).getBytes("utf-8"));
			header.add("Authorization", "Basic " + basic);
		} catch (UnsupportedEncodingException e) {
			log.error("????????????", e.getMessage(), e);
		}
		try {
			HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, header);
			// ?????????????????????
			restTemplate.setErrorHandler(new CustomResponseErrorHandler());
			// oauth??????
			log.info("???????????? {}", url);
			Oauth2ResponseVO response = restTemplate.postForObject(url, httpEntity, Oauth2ResponseVO.class);
			if (response != null && !StringUtils.isEmpty(response.getError())) {
				log.error("??????{}???????????? {}", username, response.getError_description());
				tsysLoginLogService.saveFailMessage(request, ExecStatus.INCORRECT_ACCOUNT_PASSWORD.getMsg());
				return R.error(response.getError_description());
			}
			// ??????????????????
			tsysLoginLogService.saveSuccessMessage(request, "??????????????????", userInfo.getUserRealname());
			// ???????????????????????????????????????
			SysUserVO vo = new SysUserVO();
			BeanUtils.copyProperties(vo, userInfo);
			vo.setToken(response.getAccess_token());
			vo.setUserimg(uploadFileUtils.stitchingPath(userInfo.getUserimg(), INDEX_USER));
			return R.ok().put(Constant.R_DATA, vo);
		} catch (Exception e) {
			log.error("????????????????????????", e);
			return R.error("??????????????????????????????????????????");
		}
	}

	/**
	 * ?????????????????????
	 * @param session
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("captcha.jpg")
	public void captcha(HttpSession session, HttpServletResponse response) throws IOException {
		response.setHeader("Cache-Control", "no-store, no-cache");
		response.setContentType("image/jpeg");
		// ?????????????????????
		String text = producer.createText();
		// ?????????????????????
		BufferedImage image = producer.createImage(text);
		// ?????????shiro session
		session.setAttribute(Constants.KAPTCHA_SESSION_KEY, text);
		ServletOutputStream out = response.getOutputStream();
		ImageIO.write(image, "jpg", out);
		IOUtils.closeQuietly(out);
	}

	@RequestMapping("logout")
	public R doLogout(HttpSession session, @RequestHeader("authorization") String authorization, String accessToken) {
		try {
			if (authorization != null && authorization.length() > 6) {
				accessToken = authorization.substring(6).trim();
			}
			// ????????????????????????token
			// restTemplate.getForObject(logoutUrl + "?accessToken=" + accessToken,
			// Object.class);
			session.invalidate();
		} catch (Exception e) {
			log.error("????????????", e.getMessage());
		}
		return R.ok();
	}

	/**
	 * ???????????? settingType ??????????????????????????????????????? settingUserId ?????????????????????
	 * 
	 * @param map
	 * @return
	 */
	@GetMapping("/querySettings")
	@SysLog("??????")
	public R querySetting(@RequestParam(required = true) Map<String, Object> map) {
		return tsysSettingsService.querySetting(map);
	}
}
