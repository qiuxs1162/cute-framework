package com.hzecool.core.cache.mc.redis;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hzecool.tech.redis.RedisDAO;

/**
 * 
 * 功能描述: Redis存储的Map，不使用序列化和反序列；
 * --谨慎使用：因为每个对象一个mapKey，导致mapKey变多，但是遍历mapKey只能使用"keys xx*"，导致效率低下
 * --推荐使用RedisStringMapSet
 * 
 * 格式： 
 * --objMap部分存储对象：<String("map:"+":"+name),[<String(objProp), String(objPropValue)>,...]>
 * 
 * 推荐用法：[<String, String>,...]代表一个对象
 * 
 * <p>新增原因: 
 *  
 * @author fengdg   
 * @version 1.0.0
 * @since 2017年3月14日 上午11:33:27
 */
public class RedisStringMap implements IRedisMap<String, String> {

	private RedisDAO redisDAO;
	String nameKey;

	/**
	 * redis键统一加map:
	 * 
	 * @param name
	 */
	public RedisStringMap(String name) {
		this.nameKey = "map:" + name;
	}

	/**
	 * 
	 * 指定RedisDAO
	 * 
	 * @param name
	 * @param redisDao
	 */
	public RedisStringMap(String name, RedisDAO redisDao) {
		this.nameKey = "map:" + name;
		this.redisDAO = redisDao;
	}

	/**
	 * 功能描述: 用于延迟初始化RedisDAO. lazy initialization holder class模式
	 */
	private static class DefaultRedisDAOHolder {
		/** map用的比较多，用自己的连接池 */
		static final RedisDAO REDIS_DAO = new RedisDAO("map");
	}
	
	/**
	 * 
	 * 初始化时未指定dao，则使用默认的dao
	 * 
	 * @return
	 */
	public RedisDAO getRedisDAO() {
		if (redisDAO == null) {
			redisDAO = DefaultRedisDAOHolder.REDIS_DAO;
		}
		return redisDAO;
	}

	@Override
	public int size() {
		Long l = getRedisDAO().hlen(nameKey);
		return l.intValue();
	}

	@Override
	public boolean isEmpty() {
		boolean b = false;
		if (size() == 0) {
			b = true;
		}
		return b;
	}

	@Override
	public boolean containsKey(Object key) {
		Boolean b = getRedisDAO().hexists(nameKey, key.toString());
		return b.booleanValue();
	}

	/** 此方法开销很大，因为要先把所有值取回，慎用 */
	@Override
	public boolean containsValue(Object value) {
		Collection<String> c = values();
		boolean b = false;
		if (c != null) {
			b = c.contains(value);
		}
		return b;
	}

	@Override
	public String get(Object key) {
		return getRedisDAO().hget(nameKey, key.toString());
	}

	/**
	 * 将指定的值与此映射中的指定键关联（可选操作）。如果此映射以前包含一个该键的映射关系，则用指定值替换旧值
	 * 
	 * @return 返回null,因为redis返回的是long，不是以前的值
	 */
	@Override
	public String put(String key, String val) {
		getRedisDAO().hset(nameKey, key, val);
		return null;
	}

	/**
	 * 返回null,因为redis返回的是long，不是以前的值
	 */
	@Override
	public String remove(Object key) {
		getRedisDAO().hdel(nameKey, key.toString());
		return null;
	}

	@Override
	public void putAll(Map<? extends String, ? extends String> m) {
		if (m != null) {
			Map<String, String> map = new HashMap<String, String>();
			for (Map.Entry<? extends String, ? extends String> entry : m.entrySet()) {
				map.put(entry.getKey(), entry.getValue());
			}
			getRedisDAO().hmset(nameKey, map);
		}
	}

	@Override
	public void clear() {
		getRedisDAO().del(nameKey);
	}

	@Override
	public Set<String> keySet() {
		return getRedisDAO().hkeys(nameKey);
	}

	@Override
	public Collection<String> values() {
		return getRedisDAO().hvals(nameKey);
	}

	@Override
	public Set<Map.Entry<String, String>> entrySet() {
		Map<String, String> map = getMap();
		if (map != null) {
			return map.entrySet();
		}
		return Collections.emptySet();
	}

	private Map<String, String> getMap() {
		return getRedisDAO().hgetAll(this.nameKey);
	}

	/**
	 * 一次读取多个keys的value值，以列表方式
	 * 
	 * @author fengdg
	 * @param keys
	 * @return
	 */
	public List<String> getListByKeys(List<String> listKey) {
		return hmget(listKey);
	}

	/**
	 * 从redis中一次性读取多个key值的字节数组列表
	 * 
	 * @author fengdg
	 * @param keys
	 * @return
	 */
	private List<String> hmget(List<String> listKey) {
		return getRedisDAO().hmget(nameKey, listKey.toArray(new String[0]));
	}

	@Override
	public String toString() {
		Map<String, String> map = getMap();
		if (map != null) {
			return map.toString();
		}
		return super.toString();
	}

	public String getNameKey() {
		return nameKey;
	}

}
