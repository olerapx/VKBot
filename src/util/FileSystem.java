package util;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import bot.Bot;
import crypto.Decryptor;
import crypto.Encryptor;

/**
 * Provides working with all used application files.
 */
public class FileSystem
{
	private static String tempCaptchaPath = "/cache/temp_captcha/";
	private static String botsPath = "/cache/bots/";
	
	private static String botDataExt = ".dat";
	private static String botKeyExt = ".cr";
	
	private static String botPath, botDataFile, botKeyFile;
	
	public static String getTempCaptchaPath () {return tempCaptchaPath;}
	public static String getBotsPath() {return botsPath;}
	
	public static void writeBotToFile (Bot bot)
	{
		bot.stop();
		
		String uuid = UUID.nameUUIDFromBytes(BigInteger.valueOf(bot.getID()).toByteArray()).toString();	
		String botID = String.valueOf(bot.getID());
		
		constructBotPaths(uuid, botID);
		
		File botFile = new File (botPath);			
				
		if (checkFileIntegrity(botFile, uuid))
		{
			try 
			{
				Encryptor en = new Encryptor(new File (botDataFile), new File(botKeyFile));				
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
				refreshDirectory(botFile);

				Encryptor en = new Encryptor(new File (botDataFile), botKeyFile);				
				en.write(bot);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}	
		}
	}
	
	private static void constructBotPaths(String uuid, String botID)
	{
		botPath = System.getProperty("user.dir") + FilenameUtils.separatorsToSystem(botsPath) + botID;
		botDataFile = botPath + FilenameUtils.separatorsToSystem("/") + uuid + botDataExt;
		botKeyFile = botPath + FilenameUtils.separatorsToSystem("/") + uuid + botKeyExt;	
	}
	
	private static boolean checkFileIntegrity (File botFile, String uuid)
	{
		File botDataFile = new File (botFile.getAbsolutePath() + "/" + uuid + botDataExt);
		if (!botDataFile.exists() || botDataFile.isDirectory())
			return false;
		
		File botKeyFile = new File (botFile.getAbsolutePath() + "/" + uuid + botKeyExt);
		if (!botKeyFile.exists() || botKeyFile.isDirectory())
			return false;
		
		return true;
	}
	
	private static void refreshDirectory (File botFile) throws IOException
	{
		if (botFile.isFile())
			botFile.delete();
		else 
		{
			try 
			{
				FileUtils.deleteDirectory(botFile);
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		
		botFile.mkdirs();
		
		new File (botDataFile).createNewFile();
		new File (botKeyFile).createNewFile();
	}
	
	public static Bot readBotFromFile (int ID) throws IOException
	{
		String uuid = UUID.nameUUIDFromBytes(BigInteger.valueOf(ID).toByteArray()).toString();	
		String botID = String.valueOf(ID);
		
		constructBotPaths (uuid, botID);
		
		File botFile = new File (botPath);			

		if (checkFileIntegrity(botFile, uuid))
		{
			try 
			{
				Decryptor de = new Decryptor(new File (botDataFile), new File(botKeyFile));				
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
	
	//TODO: settings
}
