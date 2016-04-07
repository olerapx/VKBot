package util;

import bot.Bot;

/**
 * Provides working with all used application files.
 */
public class FileSystem
{
	private static String tempCaptchaPath = "cache/temp_captcha";
	private static String botsPath = "cache/bots";
	
	public static String getTempCaptchaPath () {return tempCaptchaPath;}
	public static String getBotsPath() {return botsPath;}
	
	public static void writeBotToFile (Bot bot)
	{
		//TODO
	}
	
	public static Bot readFromFile (int botID)
	{
		//TODO
		return null;
	}
	
	//TODO: settings
}
