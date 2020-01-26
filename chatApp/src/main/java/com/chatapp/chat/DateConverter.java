package com.chatapp.chat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateConverter {
	public static LocalDateTime convert(String d) throws ParseException {
		SimpleDateFormat format= new SimpleDateFormat("YYYY-MM-DD HH:MM:SS");
		Date date1= format.parse(d);
		return convert(date1);
	}
	public static LocalDateTime convert(Date dateToConvert) {
	    return dateToConvert.toInstant()
	      .atZone(ZoneId.systemDefault())
	      .toLocalDateTime();
	}
}
