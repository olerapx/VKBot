package api.worker;

import api.client.Client;

public abstract class Worker 
{
	protected Client client;
		
	public Worker (Client client)
	{
		this.client=client;
	}
}