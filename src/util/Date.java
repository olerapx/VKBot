package util;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import bot.Bot;

public class Date 
{
	public static String formatTimestamp(long unixtime)
	{
		Instant i = Instant.ofEpochSecond(unixtime);			
		DateTimeFormatter f = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm:ss");
		
		return i.atZone(Calendar.getInstance().getTimeZone().toZoneId()).format(f);
	}
	
	public static String formatYears(long userAge)
	{
		String age;
		
		if (userAge==-1L)
			return "";	
		else age = String.valueOf(userAge)+" ";
		
		if (userAge%10 == 1 && userAge != 11)
			age += Bot.resources.getString("Bot.age.oneYear");
		else if (userAge%10 >=2 && userAge%10 <=4 && !(userAge >= 12 && userAge<=14))
			age+=Bot.resources.getString("Bot.age.coupleYears");
		else
			age += Bot.resources.getString("Bot.age.years");
		
		return age;
	}
}
