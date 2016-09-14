package util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.UUID;

import org.apache.commons.io.FileUtils;

import bot.Bot;
import bot.BotPath;
import crypto.Decryptor;
import crypto.Encryptor;

/**
 * Provides working with all used application files.
 */
public class FileSystem
{
	static String tempCaptchaPath = "/cache/temp_captcha/";
	static String botsPath = "/cache/bots/";
	
	static String botDataExt = ".dat";
	static String botKeyExt = ".cr";
	
	public static String getTempCaptchaPath () {return tempCaptchaPath;}
	public static String getBotsPath() {return botsPath;}
	
	public static String getBotDataExt() {return botDataExt;}
	public static String getBotKeyExt() {return botKeyExt;}
	
	public static void writeBotToFile (Bot bot)
	{
		bot.stop();
		
		BotPath path = new BotPath (bot);
				
		if (checkBotFileIntegrity(path))
		{
			try 
			{
				Encryptor en = new Encryptor(new File (path.getBotDataFilePath()), new File(path.getBotKeyFilePath()));				
				en.write(bot);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}	
		}
		else
		{
			try 
			{
				refreshBotDirectory(path);

				Encryptor en = new Encryptor(new File (path.getBotDataFilePath()), path.getBotKeyFilePath());		
				en.write(bot);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}	
		}
	}
		
	public static boolean isBotExist(Bot bot)
	{
		BotPath path = new BotPath(bot);
		
		return checkBotFileIntegrity(path);
	}
	
	private static boolean checkBotFileIntegrity (BotPath path)
	{
		File botDataFile = new File (path.getBotDataFilePath());
		if (!botDataFile.exists() || botDataFile.isDirectory())
			return false;
		
		File botKeyFile = new File (path.getBotKeyFilePath());
		if (!botKeyFile.exists() || botKeyFile.isDirectory())
			return false;
		
		return true;
	}
	
	private static void refreshBotDirectory (BotPath path) throws IOException
	{
		File botDirectory = new File (path.getBotDirectoryPath());
		if (botDirectory.isFile())
			botDirectory.delete();
		else 
		{
			try 
			{
				FileUtils.deleteDirectory(botDirectory);
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		
		botDirectory.mkdirs();
		
		new File (path.getBotDataFilePath()).createNewFile();
		new File (path.getBotKeyFilePath()).createNewFile();
	}
	
	public static Bot readBotFromFile (int ID) throws IOException
	{	
		BotPath path = new BotPath(ID);

		if (checkBotFileIntegrity(path))
		{
			try 
			{
				Decryptor de = new Decryptor(new File (path.getBotDataFilePath()), new File(path.getBotKeyFilePath()));			
				Bot bot = (Bot)de.read();
				
				return bot;
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}	
		}
		else
		{
			throw new IOException("The bot does not exist or corrupted");
		}
		
		return null;
	}
	
	public static File downloadCaptchaToFile(String captchaURL) throws IOException
	{
		File captchaImageFile = new File (tempCaptchaPath+"/"+UUID.nameUUIDFromBytes(captchaURL.getBytes()).toString());
		
		captchaImageFile.getParentFile().mkdirs();
		captchaImageFile.createNewFile();
		
		FileUtils.copyURLToFile(new URL(captchaURL), captchaImageFile);
		
		return captchaImageFile;
	}
	
	//TODO: settings
}
