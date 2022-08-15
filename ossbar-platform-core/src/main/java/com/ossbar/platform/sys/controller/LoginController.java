package com.ossbar.platform.sys.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ossbar.core.baseclass.domain.R;
import com.ossbar.modules.sys.api.TsysLoginLogService;
import com.ossbar.modules.sys.api.TsysSettingsService;
import com.ossbar.modules.sys.api.TsysUserinfoService;
import com.ossbar.modules.sys.domain.TsysLoginLog;
import com.ossbar.modules.sys.domain.TsysUserinfo;
import com.ossbar.platform.core.common.cbsecurity.log.SysLog;
import com.ossbar.utils.tool.DateUtils;
import com.ossbar.utils.tool.IPUtils;
import com.ossbar.utils.tool.StrUtils;
import com.ossbar.utils.tool.TicketDesUtil;
import org.apache.commons.io.IOUtils;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;

/**
 * @author huj
 * @create 2019-05-13 10:19
 * @email hujun@creatorblue.com
 * @company 创蓝科技 www.creatorblue.com
 */
@RestController
@RefreshScope
@RequestMapping("/user")
public class LoginController {

	private Logger log = LoggerFactory.getLogger(getClass());

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
	public static void main(String[] args) {
		String val = TicketDesUtil.encryptWithMd5("admin", null);
		System.out.println(val);
	}
	@RequestMapping("login")
	public R doLogin(@RequestBody(required = false) JSONObject data, HttpSession session, HttpServletRequest request) {
		String username = data.getString("username");
		String password = data.getString("password");
		String captcha = data.getString("captcha");
		Object captchaSession = session.getAttribute(Constants.KAPTCHA_SESSION_KEY);

		log.debug("用户输入的验证码:" + captcha);
		log.debug("从session中拿验证码：" + session.getAttribute(Constants.KAPTCHA_SESSION_KEY));

		session.removeAttribute(Constants.KAPTCHA_SESSION_KEY);
		if (StrUtils.isNull(captchaSession) || StrUtils.isNull(captcha)
				|| !captcha.equalsIgnoreCase(captchaSession.toString())) {
			//String msg = "验证码错误！";
			//tsysLoginLogService.saveFailMessage(request, msg);
			//return R.error(msg);
		}
		TsysUserinfo userInfo = tsysUserinfoService.selectObjectByUserName(username);
		if (userInfo == null) {
			String msg = "账号或密码错误！";
			tsysLoginLogService.saveFailMessage(request, msg);
			return R.error(msg);
		}
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("username", username);
		map.add("password", password);
		map.add("grant_type", "password");
		map.add("scope", "all");
		HttpHeaders header = new HttpHeaders();
		// 需求需要传参为form-data格式
		header.setContentType(MediaType.MULTIPART_FORM_DATA);
		Encoder encoder = Base64.getEncoder();
		try {
			String basic = encoder.encodeToString((clientId + ":" + clientSecret).getBytes("utf-8"));
			header.add("Authorization", "Basic " + basic);
		} catch (UnsupportedEncodingException e) {
			log.error("出现异常", e.getMessage(), e);
		}
		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, header);
		try {
			System.out.println("请求地址： " + url);
			JSONObject response = restTemplate.postForObject(url, httpEntity, JSONObject.class);
			System.out.println("响应结果" + response);
			tsysLoginLogService.saveSuccessMessage(request, "用户正常登录", userInfo.getUserRealname());
			// 组装部分数据，满足前端需要
			Map<String, Object> m = new HashMap<>();
			m.put("userimg", userInfo.getUserimg()); // 用户头像
			m.put("userId", userInfo.getUserId()); // 用户ID
			m.put("userRealname", userInfo.getUserRealname()); // 用户真实姓名
			return R.ok().put("token", response.get("access_token")).put("data", m);
		} catch (Exception e) {
			log.error("登录失败！", e);
			String msg = "账号或密码错误！";
			tsysLoginLogService.saveFailMessage(request, msg);
			return R.error(msg);
		}
	}

	/**
	 * 生成图形验证码
	 * @param session
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("captcha.jpg")
	public void captcha(HttpSession session, HttpServletResponse response) throws IOException {
		response.setHeader("Cache-Control", "no-store, no-cache");
		response.setContentType("image/jpeg");
		// 生成文字验证码
		String text = producer.createText();
		// 生成图片验证码
		BufferedImage image = producer.createImage(text);
		// 保存到shiro session
		session.setAttribute(Constants.KAPTCHA_SESSION_KEY, text);
		log.debug("存入session的验证码: " + session.getAttribute(Constants.KAPTCHA_SESSION_KEY));
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
			// 开发阶段屏蔽注销token
			// restTemplate.getForObject(logoutUrl + "?accessToken=" + accessToken,
			// Object.class);
			session.invalidate();
		} catch (Exception e) {
			log.error("系统异常", e.getMessage());
		}
		return R.ok();
	}

	/**
	 * 查询设置 settingType 系统设置或用户设置（必填） settingUserId 用户设置则必填
	 * 
	 * @param map
	 * @return
	 */
	@GetMapping("/querySettings")
	@SysLog("查询")
	public R querySetting(@RequestParam(required = true) Map<String, Object> map) {
		return tsysSettingsService.querySetting(map);
	}
}
