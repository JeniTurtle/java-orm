package com.yunlaiwu.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunlaiwu.orm.BeanProcessor;
import com.yunlaiwu.orm.OrmBridge;
import com.yunlaiwu.orm.OrmProcessor;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DbUtils {

	private static Logger logger = LoggerFactory.getLogger(DbUtils.class); 

	// 生成c3p0数据源对象,会自动读取配置文件
	private static ComboPooledDataSource cpds = new ComboPooledDataSource();

	static{
		try {
			//加载JDBC驱动
			cpds.setDriverClass( "com.mysql.cj.jdbc.Driver" );
			cpds.setJdbcUrl( "jdbc:mysql://localhost/world?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC" );
			cpds.setUser("root");                                  
			cpds.setPassword("123456");  

		}catch(Exception e) {
			logger.error("DbUtils init failed", e);
		}
	}


	/**
	 * 查询返回一个bean实体
	 * @param sql
	 * @param entity
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T queryOne(String sql, Class<?> entity) {
		OrmBridge bridge = OrmProcessor.INSTANCE.getOrmBridge(entity);
		if (bridge == null || entity == null || StringUtils.isEmpty(sql)) {
			return null;
		}
		Connection connection = null;
		Statement statement = null;
		try{
			connection = cpds.getConnection();
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);	
			while (resultSet.next()) {
				// 往beanProcessor的构造函数中,传入ormbridge对象中存的column和entity类的property的映射集合,并且调用toBean方法返回一个entity对象
				return  (T) new BeanProcessor(bridge.getColumnToPropertyOverride()).toBean(resultSet, entity);
			}
		}catch(Exception e) {
			logger.error("DbUtils queryOne failed", e);
		}finally {
			if (connection != null) {
				try{
					connection.close();
				}catch(Exception e2) {
					logger.error("DbUtils queryOne failed", e2);
				}
			}
		}
		return null;
	}

	/**
	 * 查询返回bean实体列表
	 * @param sql
	 * @param entity
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> queryMany(String sql, Class<?> entity) {
		List<T> result = new ArrayList<>();
		Connection connection = null;
		Statement statement = null;
		try{
			connection = cpds.getConnection();
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);	
			Object bean = entity.newInstance();
			while (resultSet.next()) {
				bean = new BeanProcessor().toBean(resultSet, entity);
				result.add((T) bean);
			}
		}catch(Exception e) {
			logger.error("DbUtils queryMany failed", e);
		}finally {
			if (connection != null) {
				try{
					connection.close();
				}catch(Exception e2) {
					logger.error("DbUtils queryMany failed", e2);
				}
			}
		}
		return result;
	}

	/**
	 * 查询返回一个map
	 * @param sql
	 * @return
	 */
	public static Map<String, Object> queryMap(String sql) {
		Connection connection = null;
		Statement statement = null;
		Map<String, Object> result = new HashMap<>();
		try{
			connection = cpds.getConnection();
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);	
			ResultSetMetaData rsmd = rs.getMetaData();

			while (rs.next()) {
				int cols = rsmd.getColumnCount();
				for (int i = 1; i <= cols; i++)
				{
					String columnName = rsmd.getColumnLabel(i);
					if ((null == columnName) || (0 == columnName.length())) {
						columnName = rsmd.getColumnName(i);
					}
					result.put(columnName, rs.getObject(i));
				}
				break;
			}
		}catch(Exception e) {
			logger.error("DbUtils queryMap failed", e);
		}finally {
			if (connection != null) {
				try{
					connection.close();
				}catch(Exception e2) {
					logger.error("DbUtils queryMap failed", e2);
				}
			}
		}
		return result;
	}

	/**
	 * 查询返回一个map
	 * @param sql
	 * @param entity
	 * @return
	 */
	public static List<Map<String, Object>> queryMapList(String sql) {
		Connection connection = null;
		Statement statement = null;
		List<Map<String, Object>> result = new ArrayList<>();
		try{
			connection = cpds.getConnection();
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);	
			ResultSetMetaData rsmd = rs.getMetaData();

			while (rs.next()) {
				int cols = rsmd.getColumnCount();
				Map<String, Object> map = new HashMap<>();
				for (int i = 1; i <= cols; i++)
				{
					String columnName = rsmd.getColumnLabel(i);
					if ((null == columnName) || (0 == columnName.length())) {
						columnName = rsmd.getColumnName(i);
					}
					map.put(columnName, rs.getObject(i));
				}
				result.add(map);
			}
		}catch(Exception e) {
			logger.error("DbUtils queryMapList failed", e);
		}finally {
			if (connection != null) {
				try{
					connection.close();
				}catch(Exception e2) {
					logger.error("DbUtils queryMapList failed", e2);
				}
			}
		}
		return result;
	}

	/**
	 * 执行特定的sql语句
	 * @param sql
	 * @return
	 */
	public static boolean executeSql(String sql) {
		if (StringUtils.isEmpty(sql)) {
			return true;
		}
		Connection connection = null;
		Statement statement = null;
		try{
			connection = cpds.getConnection();
			statement = connection.createStatement();
			statement.execute(sql);	
			return true;
		}catch (Exception e) {
			logger.error("DbUtils executeSql failed", e);
		}finally {
			if (connection != null) {
				try{
					connection.close();
				}catch(Exception e2) {
					logger.error("DbUtils executeSql failed", e2);
				}
			}
		}
		return false;
	}

}
