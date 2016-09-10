package bot;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.commons.io.FilenameUtils;

import api.client.Client;
import api.user.User;
import api.worker.WorkerInterface;
import application.Main;
import javafx.beans.property.*;
import scripts.JythonRunner;
import scripts.NashornRunner;
import scripts.ScriptRunner;
import util.Date;

public class Bot implements Serializable
{	
	private static final long serialVersionUID = -4719943341841483336L;
	
	final public static String resourcePath = "resources.locale.Bot.messages";
	public static ResourceBundle resources = Main.loadLocale(Locale.getDefault(), resourcePath);
	
	Client client;
	WorkerInterface workerInterface;
	User user;
	ScriptRunner runner;
	File scriptFile;
	
    boolean hasProblem = false;
    Problem problemType = Problem.UNKNOWN;
	
	transient SimpleStringProperty firstNameProperty;
	transient SimpleStringProperty lastNameProperty;
	transient SimpleIntegerProperty IDProperty;
	
	transient SimpleStringProperty cityWithAgeProperty;
	
	transient SimpleStringProperty onlineProperty;

	transient SimpleStringProperty statusProperty;
	
	public SimpleStringProperty getFirstNameProperty() {return firstNameProperty;}

	public SimpleStringProperty getLastNameProperty() {return lastNameProperty;}

	public SimpleIntegerProperty getIDProperty() {return IDProperty;}
	
	public SimpleStringProperty getCityWithAgeProperty() { return cityWithAgeProperty;}
	
	public SimpleStringProperty getOnlineProperty()	{return onlineProperty;}
	
	public SimpleStringProperty getStatusProperty() {return statusProperty;}    
	
	public String getFirstName () {return firstNameProperty == null? "" : firstNameProperty.get();}
	
	public String getLastName () {return lastNameProperty == null? "" : lastNameProperty.get();}
	
	public int getID () {return IDProperty == null? -1 : IDProperty.get();}
	
	public String getCityWithAge () {return cityWithAgeProperty == null? "" : cityWithAgeProperty.get();}
	
	public String getOnline () {return onlineProperty == null? "" : onlineProperty.get();}
	
	public String getStatus () {return statusProperty == null? "" : statusProperty.get();}
        
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
		
		statusProperty = new SimpleStringProperty(resources.getString("Bot.state.idle"));
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
		
		String ext = FilenameUtils.getExtension(script.getAbsolutePath());

		switch(ext)
		{
		case "py":
			this.runner = new JythonRunner (workerInterface); 
			break;
		case "js":
			this.runner = new NashornRunner (workerInterface);
			break;
		default:
				throw new IllegalArgumentException(ext);
		}
	}
	
	
	public void start()
	{
		if (runner == null || scriptFile == null) return;
		
		
	}
	
	public void stop()
	{
		if (runner == null || scriptFile == null) return;
		
		
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
	
	private void writeObject(ObjectOutputStream oos) throws IOException 
	{
		oos.defaultWriteObject();
		
		oos.writeObject(statusProperty.get());
	}

	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException 
	{
		ois.defaultReadObject();
		
		statusProperty = new SimpleStringProperty((String) ois.readObject());
		getUserProperties();	
	}
}
