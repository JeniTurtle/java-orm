package com.yunlaiwu.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.yunlaiwu.entity.Player;
import com.yunlaiwu.orm.OrmProcessor;
import com.yunlaiwu.utils.DbUtils;


public class TestEntity {

	/** junit中自定义的注解, 表示优先执行 **/
	@Before
	public void init() {
		OrmProcessor.INSTANCE.initOrmBridges();
	}

	@Test
	public void testQuery() {
		Player player = DbUtils.queryOne("select * from player where id=1" , Player.class);
		assertTrue(player.getName().equals("yunlaiwu"));
	}

	@Test
	public void testUpdate() {
		Player player = DbUtils.queryOne("select * from player where id=1" , Player.class);
		player.setName("Hello");
		player.setUpdate();
		player.save();

		//check
		Player tmpPlayer = DbUtils.queryOne("select * from player where id=1" , Player.class);
		assertTrue(tmpPlayer.getName().equals("Hello"));
		
		//rollback
		player.setName("yunlaiwu");
		player.setUpdate();
		player.save();
	}

	@Test
	public void testInsert() {
		Player player = new Player();
		player.setNo(666);
		player.setName("younger");
		player.setInsert();
		
		player.save();
		
		//check
		Player tmpPlayer = DbUtils.queryOne("select * from player where id=" + player.getNo() , Player.class);
		assertTrue(tmpPlayer.getName().equals("younger"));
		
		//rollback
		player.setDelete();
		player.save();
	}

	@Test
	public void testDelete() {
		Player player = DbUtils.queryOne("select * from player where id=1" , Player.class);
		player.setDelete();
		player.save();
		
		//check
		Player tmpPlayer = DbUtils.queryOne("select * from player where id=" + player.getNo() , Player.class);
		assertTrue(tmpPlayer == null);
		
		//rollback
		player.setName("yunlaiwu");
		player.setInsert();
		player.save();
	}
	
}
