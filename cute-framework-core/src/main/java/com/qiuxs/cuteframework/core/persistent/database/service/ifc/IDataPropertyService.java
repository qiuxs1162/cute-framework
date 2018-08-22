package com.qiuxs.cuteframework.core.persistent.database.service.ifc;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.qiuxs.cuteframework.core.persistent.database.dao.IBaseDao;
import com.qiuxs.cuteframework.core.persistent.database.dao.page.PageInfo;
import com.qiuxs.cuteframework.core.persistent.database.entity.IEntity;

/**
 * 提供
 * 
 * @author qiuxs
 *
 * @param <PK>
 * @param <T>
 * @param <D>
 */
public interface IDataPropertyService<PK extends Serializable, T extends IEntity<PK>, D extends IBaseDao<PK, T>> extends IPropertyService<PK, T> {

	public void setId(T bean);
	
	public void create(T bean);

	public void createInBatch(List<T> beans);
	
	public void save(T bean);

	public void update(T newBean);

	public T getById(PK id);

	public List<T> getByIds(Collection<PK> ids);

	public List<T> findByMap(final Map<String, Object> params, PageInfo pageInfo);

	public void deleteById(PK id);

}
