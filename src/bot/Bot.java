package bot;

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

import api.client.Client;
import api.user.User;
import api.worker.WorkerInterface;
import application.Main;
import javafx.beans.property.*;
import scripts.JythonRunner;
import scripts.ScriptRunner;
import util.Date;

public class Bot
{	
	final public static String resourcePath = "resources.locale.Bot.messages";
	
	public static ResourceBundle resources = Main.loadLocale(Locale.getDefault(), resourcePath);
	
	Client client;
	WorkerInterface workerInterface;
	User user;
	ScriptRunner runner;
	File scriptFile;
	
	SimpleStringProperty firstNameProperty;
	SimpleStringProperty lastNameProperty;
	SimpleIntegerProperty IDProperty;
	
	SimpleStringProperty cityWithAgeProperty;
	
	SimpleStringProperty onlineProperty;

    SimpleStringProperty statusProperty;
	
	public SimpleStringProperty getFirstNameProperty() 
	{
		return firstNameProperty;
	}

	public SimpleStringProperty getLastNameProperty() 
	{
		return lastNameProperty;
	}

	public SimpleIntegerProperty getIDProperty()
	{
		return IDProperty;
	}

	public SimpleStringProperty getCityWithAgeProperty() 
	{
		return cityWithAgeProperty;
	}

	public SimpleStringProperty getOnlineProperty()
	{
		return onlineProperty;
	}

	public SimpleStringProperty getStatusProperty() 
	{
		return statusProperty;
	}    
	
	//TODO: string getters
    
    boolean hasProblem=false;
    Problem problemType = Problem.UNKNOWN;
    
	public Bot (Client client)
	{
		this.client = client;
		
		this.workerInterface = new WorkerInterface (client);
		this.user = client.me;
		
		initProperties();
	}
	
	private void initProperties()
	{
		getUserProperties();
		
		statusProperty = new SimpleStringProperty("Idle");
	}
	
	private void getUserProperties()
	{	
		firstNameProperty = new SimpleStringProperty(user.firstName());
		lastNameProperty = new SimpleStringProperty (user.lastName());
		IDProperty = new SimpleIntegerProperty (user.ID());
		
		long userAge = user.ageInYears();
		
		String city = user.cityName()+((userAge==-1L)?"":", ");
				
		cityWithAgeProperty = new SimpleStringProperty(city + Date.formatYears(userAge));
			
		if (user.online() == User.Online.OFFLINE)			
			onlineProperty = new SimpleStringProperty(Date.formatTimestamp(user.lastOnline()));
		else onlineProperty = new SimpleStringProperty(resources.getString("Bot.online.online"));
	}
	
	public void setScript(File script)
	{
		this.scriptFile = script;

		//this.runner = new JythonRunner(workerInterface); //TODO Jython or Nashorn
	}
	
	public void start()
	{
		
	}
	
	public void stop()
	{
		
	}
	
	public User getUser () {return client.me;}
	
	public void updateUser()
	{
		try
		{
			this.user = workerInterface.userWorker().getMe();
			getUserProperties();
		} 
		catch (Exception e) 
		{
			
		}
	}
}
