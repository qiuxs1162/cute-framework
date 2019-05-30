package com.qiuxs.cuteframework.web;

public class WebConstants {
	
	/** apiKey请求餐宿 */
	public static final String REQ_P_API_KEY = "apiKey";
	public static final String REQ_H_COMPRESS_TYPE = "compress-type";
	
	/** 自动生成apiKey */
	public static final String AUTO_API_KEY = "auto";
	
	/** 系统控制器前缀 */
	public static final String SYS_CONTROLLER_PREFIX = "/sys";
	/** 默认RequestMapper.produces */
	public static final String DEFAULT_REQUEST_PRODUCES = "application/json; charset=UTF-8";
	/** XML */
	public static final String XML_PRODUCES = "text/xml; charset=UTF-8";
	
	public static class ErrorCode {
		/** 服务端系统错误代码基础 */
		public static final int WEB_ERROR_CODE = -1000;
		/** ApiKey不存在 */
		public static final int API_KEY_NOT_EXISTS = WEB_ERROR_CODE - 1;
	}
}
