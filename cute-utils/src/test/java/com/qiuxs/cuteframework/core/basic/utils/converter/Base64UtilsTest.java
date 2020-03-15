package com.qiuxs.cuteframework.core.basic.utils.converter;

import org.junit.Assert;
import org.junit.Test;

public class Base64UtilsTest {

	@Test
	public void testEnDecode() {
		String str = "我是字符串";
		System.out.println("原始：" + str);
		String base64Str = Base64Utils.encodeString(str);
		System.out.println("加密后：" + base64Str);
		String decodedStr = Base64Utils.decodeString(base64Str);
		System.out.println("解密后：" + decodedStr);
		Assert.assertEquals("Assert str :", str, decodedStr);
	}
	
}