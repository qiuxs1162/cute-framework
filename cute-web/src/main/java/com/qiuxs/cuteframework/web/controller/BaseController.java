package com.qiuxs.cuteframework.web.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.alibaba.fastjson.JSONObject;
import com.qiuxs.cuteframework.core.basic.ex.LoginException;
import com.qiuxs.cuteframework.core.basic.utils.ExceptionUtils;
import com.qiuxs.cuteframework.core.basic.utils.StringUtils;
import com.qiuxs.cuteframework.core.context.EnvironmentContext;
import com.qiuxs.cuteframework.core.utils.notice.NoticeLogger;
import com.qiuxs.cuteframework.tech.log.LogConstant;
import com.qiuxs.cuteframework.tech.log.LogUtils;
import com.qiuxs.cuteframework.web.WebConstants.HttpHeader;
import com.qiuxs.cuteframework.web.action.ActionConstants;
import com.qiuxs.cuteframework.web.context.RequestContextHolder;
import com.qiuxs.cuteframework.web.log.entity.RequestLog;

/**
 * 控制器基类
 * 
 * @author qiuxs
 *
 */
public abstract class BaseController {

	protected static Logger log = LogManager.getLogger(BaseController.class);
	
	public static final String LOGIN_PATH_KEY = "login-path";
	public static final String DEFAULT_LOGIN_PATH = "/login";
	
	@ExceptionHandler
	public String handlerException(Throwable e, HttpServletResponse response) {
		response.addIntHeader(HttpHeader.STATUS.value(), RequestLog.FAILED);
		e = ExceptionUtils.getRtootThrowable(e);
		boolean loginError = false;
		if (e instanceof LoginException) {
			// 登陆异常，返回标准状态码
			response.addHeader("needLogin", "true");
			response.addHeader("loginPath", getLoginPath());
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			loginError = true;
		}
		JSONObject error = ExceptionUtils.logError(log, e);
		error.put("globalId", LogUtils.getContextMap().get(LogConstant.COLUMN_GLOBALID));
		if (!EnvironmentContext.isDebug() && !loginError) {
			NoticeLogger.error(error.getString("msg"), ExceptionUtils.getStackTrace(e));
		}
		return error.toJSONString();
	}
	
	/**
	 * 获取登陆页面地址
	 *  
	 * @author qiuxs  
	 * @return
	 */
	public static String getLoginPath() {
		String loginPath = EnvironmentContext.getString(LOGIN_PATH_KEY, DEFAULT_LOGIN_PATH);
		return StringUtils.handlePath(loginPath);
	}

	/**
	 * 填充客户端信息
	 * 
	 * @param params
	 * @param request
	 */
	protected void fillClientInfo(Map<String, String> params, HttpServletRequest request) {
		params.put(ActionConstants.CLIENT_IP, RequestContextHolder.getCliIp());
	}

	/**
	 * 填充服务端信息
	 * 
	 * @param params
	 * @param request
	 */
	protected void fillServerInfo(Map<String, String> params, HttpServletRequest request) {
		params.put(ActionConstants.REQUEST_URL, RequestContextHolder.getRequestUrl());
		params.put(ActionConstants.REQUEST_SCHEME, RequestContextHolder.getScheme());
		params.put(ActionConstants.SERVER_HOST, RequestContextHolder.getServerHost());
		params.put(ActionConstants.REQUEST_PORT, String.valueOf(RequestContextHolder.getServerPort()));
	}
}
