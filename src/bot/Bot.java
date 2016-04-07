package bot;

import java.io.File;
import java.io.Serializable;

import api.client.Client;
import api.user.User;
import api.worker.WorkerInterface;
import javafx.beans.property.*;
import scripts.JythonRunner;
import scripts.ScriptRunner;

public class Bot
{	
	Client client;
	WorkerInterface workerInterface;
	User user;
	ScriptRunner runner;
	File scriptFile;
	
	SimpleStringProperty firstNameProperty;
	SimpleStringProperty lastNameProperty;
	SimpleIntegerProperty IDProperty;
	
	SimpleStringProperty cityProperty;
	
	SimpleBooleanProperty isOnline;
	SimpleLongProperty lastOnline;
	
	SimpleIntegerProperty age;
	
	SimpleStringProperty status;
	//problem

	
	public Bot (Client client)
	{
		this.client = client;
		
		this.workerInterface = new WorkerInterface (client);
		this.user = client.me;
		
		initProperties();
	}
	
	private void initProperties()
	{
		firstNameProperty = new SimpleStringProperty(user.firstName());
		lastNameProperty = new SimpleStringProperty (user.lastName());
		IDProperty = new SimpleIntegerProperty (user.ID());
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
		} 
		catch (Exception e) 
		{
			
		}
		
		//set props
	}
}
