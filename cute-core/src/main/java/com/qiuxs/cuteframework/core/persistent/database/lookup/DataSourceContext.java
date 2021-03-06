package com.qiuxs.cuteframework.core.persistent.database.lookup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qiuxs.cuteframework.core.basic.Constants.DsType;
import com.qiuxs.cuteframework.core.basic.bean.UserLite;
import com.qiuxs.cuteframework.core.basic.utils.NumberUtils;
import com.qiuxs.cuteframework.core.basic.utils.StringUtils;
import com.qiuxs.cuteframework.core.context.ApplicationContextHolder;
import com.qiuxs.cuteframework.core.context.TLVariableHolder;
import com.qiuxs.cuteframework.core.context.UserContext;
import com.qiuxs.cuteframework.core.persistent.database.service.ifc.IDataSourceService;
import com.qiuxs.cuteframework.core.persistent.unit.DsUnitUtil;
import com.qiuxs.cuteframework.core.persistent.unit.entity.DsUnit;

/**
 * 数据源上下文
 * 
 * @author qiuxs
 * 
 * 创建时间 ： 2018年7月27日 下午11:17:24
 *
 */
public class DataSourceContext {

	private static Logger log = LogManager.getLogger(DataSourceContext.class);

	private static final String TL_DS_ID = "_tl_current_ds_id";

	/**数据源切换的开关：用于手动切换*/
	private static final String TLK_DS_SWITCH = "tlDsSwitch";
	/** sql监控开关 */
	private static final String TL_SQL_MONITOR_SWITCH = "_tl_sql_monitor_switch";
	
	/** 动态数据源 */
	private static DynamicDataSource dynamicDataSource;

	/**<namespace, dsType>*/
	private static Map<String, String> dsTypeMap = new HashMap<String, String>();
	/**<namespace, dsId>*/
	private static Map<String, String> dsIdMap = new HashMap<String, String>();
	/**dao对应的pojo（可以不是entity，而是dto）<namespace, pojo>*/
	private static Map<String, Class<?>> pojoClassMap = new HashMap<>();
	/** 单元ID和业务库的对应关系 */
	private static Map<Long, String> unitDsMap = new HashMap<>();
	
	/** 序列库id */
	private static String seqDsId;
	/** 日志库id */
	private static String logDsId;
	/** 入口库id */
	private static String entryDsId;
	/** 业务库列表 */
	private static List<String> bizDsList = new ArrayList<String>();

	public static DataSource getDynamicDataSource() {
		if (dynamicDataSource == null) {
			dynamicDataSource = ApplicationContextHolder.getBean(DynamicDataSource.class);
		}
		return dynamicDataSource;
	}
	
	/***
	 * 变更当前数据库为日志库
	 *  
	 * @author qiuxs  
	 * @return
	 */
	public static String setLogDb() {
		return setUpDs(logDsId);
	}
	
	/**
	 * 获取日志库id
	 *  
	 * @author qiuxs  
	 * @return
	 */
	public static String getLogDb() {
		return logDsId;
	}
	
	/**
	 * 变更当前数据库为序列库
	 *  
	 * @author qiuxs  
	 * @return
	 */
	public static String setSeqDb() {
		return setUpDs(seqDsId);
	}
	
	/**
	 * 获取序列库id
	 *  
	 * @author qiuxs  
	 * @return
	 */
	public static String getSeqDb() {
		return seqDsId;
	}
	
	/**
	 * 变更当前数据库为入口库
	 *  
	 * @author qiuxs  
	 * @return
	 */
	public static String setEntryDb() {
		return setUpDs(entryDsId);
	}
	
	/**
	 * 获取入口库id
	 *  
	 * @author qiuxs  
	 * @return
	 */
	public static String getEntryDb() {
		return entryDsId;
	}
	
	/**
	 * 初始化序列库id
	 *  
	 * @author qiuxs  
	 * @param seqDsId
	 */
	public static void initSeqDb(String seqDsId) {
		DataSourceContext.seqDsId = seqDsId;
	}
	
	/**
	 * 初始化日志库id
	 *  
	 * @author qiuxs  
	 * @param logDsId
	 */
	public static void initLogDb(String logDsId) {
		DataSourceContext.logDsId = logDsId;
	}
	
	/**
	 * 初始化入口库id
	 *  
	 * @author qiuxs  
	 * @param entryDsId
	 */
	public static void initEntryDb(String entryDsId) {
		DataSourceContext.entryDsId = entryDsId;
	}
	
	/**
	 * 添加业务库
	 *  
	 * @author qiuxs  
	 * @param bizDsId
	 */
	public static void addBizDb(String bizDsId) {
		DataSourceContext.bizDsList.add(bizDsId);
	}
	
	/**
	 * 获取业务库列表
	 *  
	 * @author qiuxs  
	 * @return
	 */
	public static List<String> getBizDsList(){
		return new ArrayList<String>(bizDsList);
	}

	public static String getDsId() {
		String currentDsId = TLVariableHolder.getVariable(TL_DS_ID);
		return currentDsId;
	}

	public static String setUpDs(String dsId) {
		String oldDsId = TLVariableHolder.getVariable(TL_DS_ID);
		TLVariableHolder.setVariable(TL_DS_ID, dsId);
		return oldDsId;
	}

	public static String putDsType(String nameSpace, String dsType) {
		if (StringUtils.isNotBlank(nameSpace)) {
			return dsTypeMap.put(nameSpace, dsType);
		}
		return null;
	}

	public static String putDsId(String nameSpace, String dsId) {
		if (StringUtils.isNotBlank(nameSpace)) {
			return dsIdMap.put(nameSpace, dsId);
		}
		return null;
	}

	public static Class<?> putPojoClass(String nameSpace, Class<?> pojoClass) {
		if (StringUtils.isNotBlank(nameSpace)) {
			return pojoClassMap.put(nameSpace, pojoClass);
		}
		return null;
	}
	
	public static Class<?> getPojoClass(String nameSpace) {
		return pojoClassMap.get(nameSpace);
	}

	public static String getDsType(String nameSpace) {
		return dsTypeMap.get(nameSpace);
	}

	/**
	 * 设置数据源切换为手动，默认是自动
	 *  
	 * @author qiuxs  
	 * @param onOff
	 * 线程变量统一清理
	 */
	public static void setDsSwitchManual() {
		TLVariableHolder.setVariable(TLK_DS_SWITCH, false);
	}

	/**
	 * 设置数据源切换为自动
	 *  
	 * @author qiuxs  
	 * @param onOff
	 * 线程变量统一清理
	 */
	public static void setDsSwitchAuto() {
		TLVariableHolder.setVariable(TLK_DS_SWITCH, true);
	}

	/**
	 * 是否自动切换数据源
	 * @author qiuxs
	 * @return
	 * 		true：数据源自动切换
	 * 		false：手动切换数据源
	 */
	public static boolean isDsSwitchAuto() {
		Boolean switchFlag = (Boolean) TLVariableHolder.getVariable(TLK_DS_SWITCH);
		return switchFlag == null ? true : switchFlag.booleanValue();
	}

	/**
	 * 获取数据源ID
	 * @param dsType
	 * @return
	 */
	public static String getDsIdByDsType(String dsType) {
		if (dsType == null || DsType.UNKNOWN.value().equals(dsType)) {
			return null;
		} else if (DsType.BIZ.value().equals(dsType)) {
			return getBizDsId();
		} else {
			return DynamicDataSource.getDynamicDataSource().getNonBizDsId(dsType);
		}
	}

	/**
	 * 获取业务库id
	 * @return
	 */
	private static String getBizDsId() {
		UserLite userLite = UserContext.getUserLiteOpt();
		if (userLite != null) {
			Long unitId = userLite.getUnitId();
			if (unitId != null) {
				String dsId = getDsId(unitId);
				if (dsId != null) {
					return dsId;
				}
			}
		}
		return DynamicDataSource.getDynamicDataSource().getEntryDb();
	}

	/**
	 * 根据单元ID获取数据源
	 *  
	 * @author qiuxs  
	 * @param unitId
	 * @return
	 */
	public static String getDsId(Long unitId) {
		String dsId = unitDsMap.get(unitId);
		if (dsId == null && DsUnitUtil.hasDsUnit && NumberUtils.greaterThanZero(unitId)) {
			DsUnitUtil.refreshUnitDs(unitId);
			dsId = unitDsMap.get(unitId);
		}
		if (dsId == null) {
			if (log.isDebugEnabled()) {
				log.debug("getDsId dsId = null, unitId = " + unitId + ", hasDsUnit = " + DsUnitUtil.hasDsUnit + ", unitDsMap = " + unitDsMap);
			}
		}
		return dsId;
	}

	/**
	 * 通过dao层的nameSpace获取数据源
	 * @param nameSpace
	 * @return
	 */
	public static String getDsIdByNameSpace(String nameSpace) {
		String dsType = dsTypeMap.get(nameSpace);
		if (DsType.DIRECT.value().equals(dsType)) {
			return dsIdMap.get(nameSpace);
		} else if (DsType.ENTRY.value().equals(dsType)) {
			return entryDsId;
		} else {
			return getDsIdByDsType(dsType);
		}
	}

	/**
	 * 根据服务类获取数据源
	 * @param svc
	 * @return
	 */
	public static String getDsIdBySvc(Object svc) {
		if (svc instanceof IDataSourceService) {
			IDataSourceService dsSvc = (IDataSourceService) svc;
			return getDsIdByDsType(dsSvc.getDsType());
		} else {
			return getDsIdByDsType(DsType.BIZ.value());
		}
	}

	/**
	 * 刷新全部数据源对应关系
	 *  
	 * @author qiuxs  
	 * @param dsUnits
	 */
	public static void refreshAllDsUnit(List<DsUnit> dsUnits) {
		Map<Long, String> tempUnitDs = new HashMap<Long, String>();
		dsUnits.forEach(item -> {
			tempUnitDs.put(item.getUnitId(), item.getDsId());
		});
		unitDsMap = tempUnitDs;
	}

	/**
	 * 刷新单个数据源对应关系
	 *  
	 * @author qiuxs  
	 * @param dsUnit
	 */
	public static void refreshDsUnit(DsUnit dsUnit) {
		if (dsUnit == null) {
			return;
		}
		Map<Long, String> tempUnitDs = new HashMap<Long, String>();
		tempUnitDs.putAll(unitDsMap);
		tempUnitDs.put(dsUnit.getUnitId(), dsUnit.getDsId());
		unitDsMap = tempUnitDs;
	}

	/**
	 * 判断是否需要监控sql
	 *  
	 * @author qiuxs  
	 * @return
	 */
	public static boolean monitorSql() {
		Boolean monitorFlag = TLVariableHolder.getVariable(TL_SQL_MONITOR_SWITCH);
		return monitorFlag != null && monitorFlag;
	}
	
	/**
	 * 开启sql监控
	 *  
	 * @author qiuxs  
	 * @return
	 */
	public static Boolean switchSqlMonitorOn() {
		return setMonitorSql(true);
	}
	
	/**
	 * 关闭sql监控
	 *  
	 * @author qiuxs  
	 * @return
	 */
	public static Boolean switchSqlMonitorOff() {
		return setMonitorSql(false);
	}
	
	/**
	 * 设置sql监控开关
	 *  
	 * @author qiuxs  
	 * @param flag
	 * @return
	 */
	private static Boolean setMonitorSql(boolean flag) {
		return (Boolean) TLVariableHolder.setVariable(TL_SQL_MONITOR_SWITCH, flag);
	}

}
