package com.ossbar.platform.core.common.cbsecurity.log;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * Title:系统日志注解 Copyright: Copyright (c) 2017
 * Company:creatorblue.co.,ltd
 * 
 * @author creatorblue.co.,ltd
 * @version 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {

	String value() default "";
}
