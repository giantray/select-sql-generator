package com.github.giantray.compositesSelectSql;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

	public static final String TYPE_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

	static String longTypeToStr(long time, String formatType) {

		String strTime = "";
		if (time > 0) {
			SimpleDateFormat sDateFormat = new SimpleDateFormat(formatType);

			strTime = sDateFormat.format(new Date(time));

		}

		return strTime;

	}

	static String dateToStr(Date date, String formatType) {

		return longTypeToStr(date.getTime(), formatType);

	}

}
