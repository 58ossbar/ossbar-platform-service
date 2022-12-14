package com.ossbar.platform.core.config;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import com.ossbar.platform.core.common.cbsecurity.repeatsubmit.TokenInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
@AutoConfigureBefore(SecurityConfig.class)
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${com.creatorblue.file-upload-path:/mnt/creatorblue/uploads}")
	private String filePath;

	@Value("${com.creatorblue.is-cors:false}")
	private boolean isCors;

	@Value("${com.creatorblue.cors-url:*}")
	private String corsUrl;

	@Bean
	public RequestContextListener requestContextListener() {
		return new RequestContextListener();
	}
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		if(isCors) {
			registry.addMapping("/**")
					.allowedOrigins(corsUrl)
					.allowedMethods("*")
					.allowedHeaders("*")
					.allowCredentials(true)
					.maxAge(3600);
		}
	}

	@Override
	public void configureDefaultServletHandling(final DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	@Override
	public void addViewControllers(final ViewControllerRegistry registry) {
		//registry.addViewController("/").setViewName("forward:/index");
	}

	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/uploads/**")
				.addResourceLocations("file:" + filePath);
		registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
	}
	/*@Bean
	public LicenseInterceptor initializingLicenseInterceptor() {
		logger.info("????????????license??????");
		LicenseInterceptor lci = new LicenseInterceptor();
		return lci;
	}*/
	/**
	 * ?????????????????????
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// ???????????????
		registry.addInterceptor(initializingLocaleChangeInterceptor());
		// ????????????????????????
		registry.addInterceptor(initializingTokenInterceptor()).addPathPatterns("/**");
		// ???????????????????????????
		//registry.addInterceptor(initializingLicenseInterceptor()).addPathPatterns("/user/login");
	}
	@Bean
	public TokenInterceptor initializingTokenInterceptor() {
		logger.info("????????????????????????????????????????????????");
		return new TokenInterceptor();
	}
	/**
	 * ????????????????????????
	 */
	@Bean
	public StringHttpMessageConverter stringHttpMessageConverter() {
		StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
		stringHttpMessageConverter.setDefaultCharset(Charset.forName("UTF-8"));
		return stringHttpMessageConverter;
	}

	/**
	 * JSON ????????????
	 */
	@Bean
	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
		MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
		mappingJackson2HttpMessageConverter.setDefaultCharset(Charset.forName("UTF-8"));
		mappingJackson2HttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
		return mappingJackson2HttpMessageConverter;
	}

	// ????????????????????????
	@Bean
	public HttpMessageConverter<String> responseBodyConverter() {
		StringHttpMessageConverter converter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
		return converter;
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(stringHttpMessageConverter());
		converters.add(mappingJackson2HttpMessageConverter());
	}
	/**
	 * ??????????????? @Title: localeResolver @Description: @param @return ?????? @return
	 * LocaleResolver ???????????? @throws
	 */
	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver slr = new SessionLocaleResolver();
		// ????????????
		slr.setDefaultLocale(Locale.CHINESE);
		return slr;
	}

	@Bean
	public LocaleChangeInterceptor initializingLocaleChangeInterceptor() {
		logger.info("??????????????????");
		LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
		// ?????????
		lci.setParamName("lang");
		return lci;
	}
	@Bean
	public RestTemplate restTemplate(){
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new CbMappingJackson2HttpMessageConverter());
		return restTemplate;
	}
}