package com.yunlaiwu.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 
 * 标识该属性需要映射到表字段
 */
@Target(ElementType.FIELD) // 只能用来描述类中的Field
@Retention(RetentionPolicy.RUNTIME) // 运行时执行
public @interface Column {
	
	/** 不为空则说明表字段名字跟entity属性不一致 */
	String name() default "";
}
