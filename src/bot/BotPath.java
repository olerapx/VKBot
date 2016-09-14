package bot;

import java.math.BigInteger;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;

import util.FileSystem;

public class BotPath 
{
	String botDirectoryPath;
	String botDataFilePath;
	String botKeyFilePath;
	
	String uuid;
	String botID;
	
	public String getBotDirectoryPath() {return botDirectoryPath;}

	public String getBotDataFilePath() {return botDataFilePath;}

	public String getBotKeyFilePath() {return botKeyFilePath;}

	public String getUuid() {return uuid;}

	public String getBotID() {return botID;}
	
	public BotPath (Bot bot)
	{
		uuid = UUID.nameUUIDFromBytes(BigInteger.valueOf(bot.getID()).toByteArray()).toString();	
		botID = String.valueOf(bot.getID());
		
		constructBotPaths();
	}
	
	public BotPath(int ID)
	{
		uuid = UUID.nameUUIDFromBytes(BigInteger.valueOf(ID).toByteArray()).toString();	
		botID = String.valueOf(ID);
		
		constructBotPaths();
	}
	
	private void constructBotPaths()
	{
		botDirectoryPath = System.getProperty("user.dir") + FilenameUtils.separatorsToSystem(FileSystem.getBotsPath()) + botID;
		botDataFilePath = botDirectoryPath + FilenameUtils.separatorsToSystem("/") + uuid + FileSystem.getBotDataExt();
		botKeyFilePath = botDirectoryPath + FilenameUtils.separatorsToSystem("/") + uuid + FileSystem.getBotKeyExt();	
	}
}
