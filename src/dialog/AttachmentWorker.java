package dialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import client.Client;
import media.MediaID;
import worker.Worker;

public class AttachmentWorker extends Worker 
{
	public AttachmentWorker(Client client) 
	{
		super(client);
	}

	public Attachment[] getFromJSONArray(JSONArray data) throws JSONException
	{
		int attCount = data.length();
		
		Attachment[] atts = new Attachment[attCount];
		
		for (int j=0;j<attCount;j++)				
			atts[j] = getFromJSON(data.getJSONObject(j));
		
		return atts;
	}
	
	public Attachment getFromJSON(JSONObject data) throws JSONException
	{
		String type = data.getString("type");						
		data = data.getJSONObject(type);
		
		int ownerID;
		
		try
		{
			if (type.equals("wall") || type.equals("wall_reply"))
				ownerID =data.getInt("from_id");			
			else  ownerID =data.getInt("owner_id");
			
			MediaID ID = new MediaID(ownerID, data.getInt("id"));
			
			return new Attachment(type, ID);
		} 
		catch(JSONException ex)
		{
			return new Attachment (Attachment.Type.ATTACH_OTHER, new MediaID(0,0));
		}
	}
}
