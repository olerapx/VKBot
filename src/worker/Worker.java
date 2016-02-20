package worker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import client.VKClient;
import dialog.Attachment;

public abstract class Worker 
{
	protected VKClient client;
	
	public Worker (VKClient client)
	{
		this.client=client;
	}
	
	protected Attachment[] getAttachmentsFromJSON(JSONArray att) throws JSONException
	{
		int attCount = att.length();
		
		Attachment[] atts = new Attachment[attCount];
		
		for (int j=0;j<attCount;j++)
		{
			JSONObject currAttachment = att.getJSONObject(j);
			String currAttachmentID="";
			String type = currAttachment.getString("type");
			currAttachmentID+=type;
			
			currAttachment = currAttachment.getJSONObject(type);
			int ownerID;
	
			if (type.equals("wall") || type.equals("wall_reply")) ownerID =currAttachment.getInt("from_id");
			
			else ownerID =currAttachment.getInt("owner_id");
			
			currAttachmentID+=""+ownerID+"_" + currAttachment.getInt("id");
			
			atts[j] = new Attachment(currAttachmentID);
		}
		return atts;
	}
}