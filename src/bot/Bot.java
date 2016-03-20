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
	File script;

	public Bot (Client client, File script)
	{
		this.client = client;
		this.script = script;
		
		this.workerInterface = new WorkerInterface (client);
		this.runner = new JythonRunner(workerInterface);
		this.user = client.me;
	}
	
	public void setScript(File script)
	{
		this.script = script;
	}
	
	public void start()
	{
		
	}
	
	public void stop()
	{
		
	}
}
