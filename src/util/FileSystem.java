package util;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
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
	
	private static String botDirectoryPath, botDataFilePath, botKeyFilePath;
	
	public static String getTempCaptchaPath () {return tempCaptchaPath;}
	public static String getBotsPath() {return botsPath;}
	
	public static void writeBotToFile (Bot bot)
	{
		bot.stop();
		
		String uuid = UUID.nameUUIDFromBytes(BigInteger.valueOf(bot.getID()).toByteArray()).toString();	
		String botID = String.valueOf(bot.getID());
		
		constructBotPaths(uuid, botID);
		
		File botDirectory = new File (botDirectoryPath);			
				
		if (checkFileIntegrity(botDirectory, uuid))
		{
			try 
			{
				Encryptor en = new Encryptor(new File (botDataFilePath), new File(botKeyFilePath));				
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
				refreshDirectory(botDirectory);

				Encryptor en = new Encryptor(new File (botDataFilePath), botKeyFilePath);				
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
		botDirectoryPath = System.getProperty("user.dir") + FilenameUtils.separatorsToSystem(botsPath) + botID;
		botDataFilePath = botDirectoryPath + FilenameUtils.separatorsToSystem("/") + uuid + botDataExt;
		botKeyFilePath = botDirectoryPath + FilenameUtils.separatorsToSystem("/") + uuid + botKeyExt;	
	}
	
	private static boolean checkFileIntegrity (File botDirectory, String uuid)
	{
		File botDataFile = new File (botDirectory.getAbsolutePath() + "/" + uuid + botDataExt);
		if (!botDataFile.exists() || botDataFile.isDirectory())
			return false;
		
		File botKeyFile = new File (botDirectory.getAbsolutePath() + "/" + uuid + botKeyExt);
		if (!botKeyFile.exists() || botKeyFile.isDirectory())
			return false;
		
		return true;
	}
	
	private static void refreshDirectory (File botDirectory) throws IOException
	{
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
		
		new File (botDataFilePath).createNewFile();
		new File (botKeyFilePath).createNewFile();
	}
	
	public static Bot readBotFromFile (int ID) throws IOException
	{
		String uuid = UUID.nameUUIDFromBytes(BigInteger.valueOf(ID).toByteArray()).toString();	
		String botID = String.valueOf(ID);
		
		constructBotPaths (uuid, botID);
		
		File botFile = new File (botDirectoryPath);			

		if (checkFileIntegrity(botFile, uuid))
		{
			try 
			{
				Decryptor de = new Decryptor(new File (botDataFilePath), new File(botKeyFilePath));				
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
