package com.huamai.hdServer.util;

import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private static final DateTimeFormatter dateTimeFormatterForYYYYMMDDHHMM = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	private static final DateTimeFormatter dateTimeFormatterForHHMM = DateTimeFormatter.ofPattern("HH:mm");
	
	public static Date formatYYYYMMDD(String time) {
		try {
			if(time == null) {
				return null;
			}
			return simpleDateFormat.parse(time);
		}catch (Exception e) {
			return null;
		}
	}

	public static LocalDate stringToLocalDateForYYYYMMDD(String time) {
		try {
			if(StringUtils.isEmpty(time)) {
				return null;
			}
			return LocalDate.parse(time,dateTimeFormatter);
		}catch (Exception e) {
			return null;
		}
	}

	public static LocalDateTime stringToLocalDateTimeForHHMM(String time) {
		try {
			if(StringUtils.isEmpty(time)) {
				return null;
			}
			return LocalDateTime.parse(time,dateTimeFormatterForHHMM);
		}catch (Exception e) {
			return null;
		}
	}

	public static String localDateTimeToStringForYYYYMMDDHHMM(LocalDateTime localDateTime) {
		try {
			if(localDateTime == null) {
				return null;
			}
			return dateTimeFormatterForYYYYMMDDHHMM.format(localDateTime);
		}catch (Exception e) {
			return null;
		}
	}

	public static String localDateToStringForYYYYMMDD(LocalDate localDate) {
		try {
			if(localDate == null) {
				return null;
			}
			return dateTimeFormatter.format(localDate);
		}catch (Exception e) {
			return null;
		}
	}

	public static String localDateTimeToStringForHHMM(LocalDateTime localDateTime) {
		try {
			if(localDateTime == null) {
				return null;
			}
			return dateTimeFormatterForHHMM.format(localDateTime);
		}catch (Exception e) {
			return null;
		}
	}

	/**
	 * 得到两个日期之间的天数
	 */
	public static long getDistanceOfDates_edit(Calendar start, Calendar end) {
		long betweenDays=0;
		if(start==null||end==null) {
			return betweenDays;
		}
		start.add(Calendar.HOUR_OF_DAY,-start.get(Calendar.HOUR_OF_DAY));
		end.add(Calendar.HOUR_OF_DAY,-end.get(Calendar.HOUR_OF_DAY));

		betweenDays=(end.getTimeInMillis()+3600000-start.getTimeInMillis())/86400000;
		return betweenDays;
	}

}
