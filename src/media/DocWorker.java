package media;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import client.VKClient;
import worker.Worker;

public class DocWorker extends Worker 
{

	public DocWorker(VKClient client) {
		super(client);
	}
	
	public Doc getByID (int ownerID, int docID) throws ClientProtocolException, IOException, JSONException
	{
		String did = "" + ownerID + "_" + docID;
		InputStream stream = executeCommand("https://api.vk.com/method/"+
					"docs.getById?"+
					"&docs="+did+
					"&v=5.45"+
					"&access_token="+client.token);
				
		JSONObject obj = new JSONObject(IOUtils.toString(stream, "UTF-8"));
		JSONObject data = obj.getJSONArray("response").getJSONObject(0);
		
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

		return doc;
	}
}
