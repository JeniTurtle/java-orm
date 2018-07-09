package com.yunlaiwu.entity;

import com.yunlaiwu.annotation.Column;
import com.yunlaiwu.annotation.Entity;
import com.yunlaiwu.annotation.Id;
import com.yunlaiwu.cache.AbstractCacheable;

/** 对应SQL表结构的类, 实例化后可以作为一条数据实体,对sql进行操作 **/

// 这个注解的意思是要告诉运行中的程序,这个类对应的表名称是player
@Entity(table="player")
public class Player extends AbstractCacheable{

	@Column(name="id")
	@Id
	private long no;
	@Column
	private String name;

	public long getNo() {
		return no;
	}

	public void setNo(long id) {
		this.no = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Player [id=" + no + ", name=" + name + "]";
	}
	
	
}
