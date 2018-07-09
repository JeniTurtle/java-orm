package com.yunlaiwu.orm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.yunlaiwu.annotation.Column;
import com.yunlaiwu.annotation.Entity;
import com.yunlaiwu.annotation.Id;
import com.yunlaiwu.exception.OrmConfigExcpetion;
import com.yunlaiwu.utils.ClassFilter;
import com.yunlaiwu.utils.ClassScanner;
import com.yunlaiwu.utils.StringUtils;


/** 用枚举项当做一个持久化对象,进行缓存数据对象模型(entity)和ormBridges对象之间的映射 **/
public enum OrmProcessor {

	INSTANCE;
	
	/** entity与对应的ormbridge的映射关系 */
	private Map<Class<?>, OrmBridge>   = new HashMap<>();



	/** 拿到所有entity类并创建对应的bridge对象,存到Mapper中 **/
	public void initOrmBridges() {
		Set<Class<?>> entityClazzs = listEntityClazzs();
		
		for (Class<?> clazz:entityClazzs) {
			OrmBridge bridge = createBridge(clazz);
			this.classOrmMapperr.put(clazz, bridge);
		}
	}

	/** 使用反射的方式,动态操作OrmBridges对象 **/
	private OrmBridge createBridge(Class<?> clazz) {
		OrmBridge bridge = new OrmBridge();
		//拿到class的Entity注解对象
		Entity entity = (Entity) clazz.getAnnotation(Entity.class);
		//没有设置tablename,则用class名,首字母小写
		if (entity.table().length() <= 0) {
			bridge.setTableName(StringUtils.firstLetterToLowerCase(clazz.getSimpleName()));
		}else {
			bridge.setTableName(entity.table());
		}
		//获取该类所有声明的字段,包括public、private和proteced，但是不包括父类的字段
		Field[] fields = clazz.getDeclaredFields();
		for (Field field:fields) {
			//拿到field的Column注解对象
			Column column = field.getAnnotation(Column.class);

			//拿到field声明时的名字
			String fieldName = field.getName();
			try{
				//如果声明field有Column注解,那么拿到对应getter,setter方法,并存放到bridge对象的getterMethor(HashMap)中
				if (column != null) {
					Method m = clazz.getMethod("get" + StringUtils.firstLetterToUpperCase(field.getName()));
					bridge.addGetterMethod(fieldName, m);
					Method m2 = clazz.getMethod("set" + StringUtils.firstLetterToUpperCase(field.getName()), field.getType());
					bridge.addSetterMethod(fieldName, m2);
				}
				//如果field上有主键注解的话,那么放到bridge对象中的主键集合uniqueProperties(Set)里
				if (field.getAnnotation(Id.class) != null) {
					bridge.addUniqueKey(fieldName);
				}
				//如果column注解的name属性非空,则加入到bridge对象的property与表column的映射集合(Map)中.
				if (!StringUtils.isEmpty(column.name())) {
					bridge.addPropertyColumnOverride(fieldName, column.name());
				}
				//把entity的声明的字段名添加到bridge的持久化字段的集合(Set)里
				bridge.addProperty(fieldName);
			}catch(Exception e) {
				throw new OrmConfigExcpetion(e);
			}
			// 如果没有唯一索引,那么抛出异常
			if (bridge.getQueryProperties().size() <= 0) {
				throw new OrmConfigExcpetion(clazz.getSimpleName() + " entity 没有查询索引主键字段");
			}
		}
		
		return bridge;
	}

	/** 读取包下面所有带有Entity注解的class **/
	private Set<Class<?>> listEntityClazzs() {
		return ClassScanner.getClasses("com.yunlaiwu.entity",
				/**使用内部类实现ClassFilter接口,并重写accept方法,**/
				new ClassFilter() {
					@Override
					public boolean accept(Class<?> clazz) {return clazz.getAnnotation(Entity.class) != null;}
				});
	}
	
	public OrmBridge getOrmBridge(Class<?> clazz) {
		return this.classOrmMapperr.get(clazz);
	}
}
