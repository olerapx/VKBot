package attachment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import client.Client;
import media.MediaID;
import media.PhotoWorker;
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
		
		if (type.equals("link")) return getLinkFromJSON(data);
				
		try
		{
			int ownerID;
			
			if (type.equals("wall") || type.equals("wall_reply"))
				ownerID = data.getInt("from_id");			
			else ownerID = data.getInt("owner_id");
			
			MediaID ID = new MediaID(ownerID, data.getInt("id"));
			
			return new MediaAttachment(type, ID);
		} catch(JSONException ex)
		{
			return new MediaAttachment (Attachment.Type.ATTACH_OTHER, new MediaID(0,0));
		}
	}
	
	public Link getLinkFromJSON (JSONObject data) throws JSONException
	{
		Link link = new Link();
		
		link.URL = data.getString("url");
		link.title = data.getString("title");
		link.description = data.getString("description");
		
		if(data.has("caption"))
			link.caption = data.getString("caption");
		else link.caption = "";
		
		if (data.has("preview_url"))
			link.previewURL = data.getString("preview_url");
		else link.previewURL = "";
		
		if(data.has("photo"))
			link.photo = new PhotoWorker(client).getFromJSON(data.getJSONObject("photo"));
		else link.photo = null;
		
		if(data.has("is_external"))
			link.isExternal = data.getInt("is_external")!=0;
		else link.isExternal = false;
		
		return link;
	}
}
