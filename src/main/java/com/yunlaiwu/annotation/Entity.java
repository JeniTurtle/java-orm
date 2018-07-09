package com.yunlaiwu.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 
 * 
 *  标识该对象需要持久化
 */
@Target(ElementType.TYPE) // 只能用来描述类、接口(包括注解类型) 或enum声明
@Retention(RetentionPolicy.RUNTIME) // 程序运行时执行
public @interface Entity {
	
	/** 不为空则说明表名字跟entity类名字不一致 */
	String table() default "";
}
