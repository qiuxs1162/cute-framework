package com.qiuxs.cuteframework.core.persistent.database.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.qiuxs.cuteframework.core.persistent.database.dao.page.PageInfo;
import com.qiuxs.cuteframework.core.persistent.database.entity.IEntity;

public interface IBaseDao<PK extends Serializable, T extends IEntity<PK>> {

	public void deleteById(PK id);

	public T get(PK id);

	public List<T> getByIds(Collection<PK> ids);

	public List<T> list(Map<String, Object> params, PageInfo pageInfo);
	
	public List<T> list(Map<String, Object> params);
	
	public Long getCount(Map<String, Object> params);

	public void insert(T bean);
	
	public void insertInBatch(List<T> beans);

	public void update(T bean);
}
