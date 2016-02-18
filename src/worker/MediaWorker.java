package worker;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import client.VKClient;
import media.Media;
import media.MediaID;

public abstract class MediaWorker extends Worker 
{

	public MediaWorker(VKClient client) 
	{
		super(client);
	}
	
	protected abstract Media[] get (String IDs)  throws ClientProtocolException, IOException, JSONException;
	
	public Media getByID (MediaID ID) throws ClientProtocolException, IOException, JSONException
	{
		String id = ""+ID.ownerID()+"_"+ID.mediaID();
		if (!ID.accessKey().equals(""))
			id+="_"+ID.accessKey();

		return this.get(id)[0];
	}
	
	public Media getByID (int ownerID, int mediaID) throws ClientProtocolException, IOException, JSONException
	{
		MediaID ID = new MediaID(ownerID, mediaID);
		return this.getByID(ID);
	}
	
	public Media[] getByIDs (MediaID[] IDs) throws ClientProtocolException, IOException, JSONException
	{
		String ids="";
		for (int i=0;i<IDs.length-1;i++)
		{
			MediaID ID = IDs[i];
			ids+=ID.ownerID()+"_" + ID.mediaID();
			if (!ID.accessKey().equals("")) ids+="_" + ID.accessKey();
			ids+=",";
		}
		MediaID ID = IDs[IDs.length-1];
		ids+=ID.ownerID()+"_" + ID.mediaID();
		if (!ID.accessKey().equals("")) ids+="_" + ID.accessKey();
		
		return this.get(ids);
	}
}
