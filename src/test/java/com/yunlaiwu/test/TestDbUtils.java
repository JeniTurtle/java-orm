package com.yunlaiwu.test;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.yunlaiwu.orm.OrmProcessor;
import com.yunlaiwu.utils.DbUtils;

public class TestDbUtils {

	@Before
	public void init() {
		OrmProcessor.INSTANCE.initOrmBridges();
	}
	
	public void testQueryOne() {
		
	}
	
	@Test
	public void testQuerMap() {
		Map<String, Object> result = DbUtils.queryMap("select p.id as pid, p.`name` as pname, h.level as hlevel from player p, house h where p.id=1");
		System.err.println(result);
	}
	
	@Test
	public void testQuerMapList() {
		List<Map<String, Object>> result = DbUtils.queryMapList("select * from player ");
		System.err.println(result);
	}
	
}
