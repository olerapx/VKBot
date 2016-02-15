package media;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import client.VKClient;
import worker.Worker;

public class DocWorker extends Worker 
{
	public DocWorker(VKClient client) 
	{
		super(client);
	}
	
	private Doc[] get(String ids) throws ClientProtocolException, IOException, JSONException
	{
		InputStream stream = executeCommand("https://api.vk.com/method/"+
				"docs.getById?"+
				"&docs="+ids+
				"&v=5.45"+
				"&access_token="+client.token);
		
		JSONObject obj = new JSONObject(IOUtils.toString(stream, "UTF-8"));
		JSONArray response = obj.getJSONArray("response");
		
		int count = response.length();
		Doc[] docs = new Doc[count];
		
		for (int i=0;i<count;i++)
		{
			JSONObject data = response.getJSONObject(i);
			
			Doc doc = new Doc();
			doc.ID = data.getInt("id");
			doc.ownerID = data.getInt("owner_id");
			
			doc.title = data.getString("title");
			doc.size = data.getLong("size");
			doc.extention = data.getString("ext");
			doc.URL = data.getString("url");
			doc.addingDate = data.getLong("date");
			doc.type = data.getInt("type");
			
			if (data.has("preview"))
			{
				data = data.getJSONObject("preview").getJSONObject("photo");
				doc.photoURL = data.getJSONArray("sizes").getJSONObject(0).getString("src");
			}
			else doc.photoURL="";

			docs[i] = doc;
		}
		return docs;
	}
	
	public Doc getByID (int ownerID, int docID) throws ClientProtocolException, IOException, JSONException
	{
		String did = "" + ownerID + "_" + docID;
		
		return this.get(did)[0];
	}
	
	public Doc[] getByIDs(Integer[][] IDs) throws ClientProtocolException, IOException, JSONException
	{
		String ids="";
		for (int i=0;i<IDs.length-1;i++)
		{
			ids+=IDs[i][0].toString()+"_" + IDs[i][1].toString()+",";
		}
		ids+=IDs[IDs.length-1][0].toString()+"_" + IDs[IDs.length-1][1].toString();
		
		return this.get(ids);
	}
}
