package worker;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import client.VKClient;
import dialog.Attachment;

public class Worker 
{
	protected VKClient client;
	
	public Worker (VKClient client)
	{
		this.client=client;
	}
	
	protected InputStream executeCommand(String command) throws ClientProtocolException, IOException
	{
		HttpPost post = new HttpPost(command);
		
		CloseableHttpResponse response;
		response = client.httpClient.execute(post);		
		InputStream stream = response.getEntity().getContent();
		
		return stream;
	}
	
	protected Attachment[] getAttachments(JSONArray att) throws JSONException
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
	
			if (type.equals("wall")) ownerID =currAttachment.getInt("from_id");
			
			else ownerID =currAttachment.getInt("owner_id");
			
			currAttachmentID+=""+ownerID+"_" + currAttachment.getInt("id");
			
			atts[j] = new Attachment(currAttachmentID);
		}
		return atts;
	}
}