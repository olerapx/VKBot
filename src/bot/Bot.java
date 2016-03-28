package bot;

import java.io.File;

import api.client.Client;
import api.user.User;
import api.worker.WorkerInterface;
import scripts.JythonRunner;
import scripts.ScriptRunner;

public class Bot 
{
	Client client;
	WorkerInterface workerInterface;
	User user;
	ScriptRunner runner;
	File scriptFile = null;
	
	public Bot (Client client)
	{
		this.client = client;
		
		this.workerInterface = new WorkerInterface (client);
		this.runner = new JythonRunner(workerInterface);
		this.user = client.me;
	}
	
	public User getUser () {return client.me;}
	
	public void setScript(File script)
	{
		this.scriptFile = script;
	}
	
	public void start()
	{
		
	}
	
	public void stop()
	{
		
	}
}
