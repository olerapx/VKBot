package api.worker;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import api.client.Client;
import api.object.VKObject;

public abstract class Worker 
{
	protected Client client;
	
	public abstract VKObject getFromJSON(JSONObject data)  throws JSONException, ClientProtocolException, IOException;
	
	public Worker (Client client)
	{
		this.client=client;
	}
}