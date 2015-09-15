package com.github.giantray.compositesSelectSql;

/**
 * @author lizeyang
 * @date 2015年9月15日
 */
public class StringUtil {
	static boolean isNotBlank(String str) {
		return !StringUtil.isBlank(str);
	}

	static boolean isBlank(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(str.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}

}