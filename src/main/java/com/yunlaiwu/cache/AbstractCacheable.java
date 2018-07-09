package com.yunlaiwu.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunlaiwu.utils.DbUtils;
import com.yunlaiwu.utils.SqlUtils;

public abstract class AbstractCacheable extends Cacheable {
	
	private static Logger logger = LoggerFactory.getLogger(AbstractCacheable.class); 

	@Override
	public DbStatus getStatus() {
		return this.status;
	}

	@Override
	public final boolean isInsert() {
		return this.status == DbStatus.INSERT;
	}

	@Override
	public final boolean isUpdate() {
		return this.status == DbStatus.UPDATE;
	}

	@Override
	public final boolean isDelete() {
		return this.status == DbStatus.DELETE;
	}

	public void setInsert() {
		this.status = DbStatus.INSERT;
	}

	public final void setUpdate(){
		this.status = DbStatus.UPDATE;
	}

	public final void setDelete(){
		this.status = DbStatus.DELETE;
	}
	
	public final void save() {
		String saveSql = SqlUtils.getSaveSql(this);
		if (DbUtils.executeSql(saveSql)) {
			this.status = DbStatus.NORMAL;
		}
		if (logger.isDebugEnabled()) {
			System.err.println(saveSql);
		}
	}
}
