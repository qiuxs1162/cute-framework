package com.qiuxs.captcha.service;

import com.qiuxs.captcha.dao.CaptchaHistoryDao;
import com.qiuxs.captcha.entity.CaptchaHistory;
import com.qiuxs.cuteframework.core.persistent.database.service.ifc.IDataPropertyService;


/**
 * 验证码历史记录服务接口
 * 
 * 创建时间 ：2019-03-28 22:55:09
 * @author qiuxs
 */
public interface ICaptchaHistoryService extends IDataPropertyService<Long, CaptchaHistory, CaptchaHistoryDao> {

}
