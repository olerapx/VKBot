package api.attachment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import api.client.Client;
import api.media.MediaID;
import api.media.Photo;
import api.media.PhotoWorker;
import api.worker.Worker;

public class AttachmentWorker extends Worker 
{
	public AttachmentWorker(Client client) 
	{
		super(client);
	}

	public static Attachment[] getFromJSONArray(JSONArray data) throws JSONException
	{
		int attCount = data.length();
		
		Attachment[] atts = new Attachment[attCount];
		
		for (int j=0;j<attCount;j++)				
			atts[j] = getFromJSON(getObjectFromJSONArray(data, j));
		
		return atts;
	}
	
	public static Attachment getFromJSON(JSONObject data) throws JSONException
	{
		String type = getStringFromJSON(data, "type");	
		
		data = getObjectFromJSON(data, type);
		
		if (type.equals("link")) return getLinkFromJSON(data);
				
		try
		{
			int ownerID;
			
			if (type.equals("wall") || type.equals("wall_reply"))
				ownerID = getIntFromJSON(data, "from_id");	
			else ownerID = getIntFromJSON(data, "owner_id");	
			
			MediaID ID = new MediaID(ownerID, getIntFromJSON(data, "id"));
			
			return new MediaAttachment(type, ID);
		} catch(JSONException ex)
		{
			return new MediaAttachment (Attachment.Type.ATTACH_OTHER, new MediaID(0,0));
		}
	}
	
	public static Link getLinkFromJSON (JSONObject data) throws JSONException
	{
		Link link = new Link();
		
		
		link.URL = getStringFromJSON(data, "url");
		link.title = getStringFromJSON(data, "title");
		
		link.description = getStringFromJSON(data, "description");		
	    link.caption = getStringFromJSON(data, "caption");		
		link.previewURL = getStringFromJSON(data, "preview_url");
		
		if(data.has("photo"))
		{
			JSONObject photo = getObjectFromJSON(data, "photo");
			link.photo = PhotoWorker.getFromJSON(photo);
		}
		else link.photo = new Photo();
		
		link.isExternal = getBooleanFromJSON(data, "is_external");
		
		return link;
	}
}
