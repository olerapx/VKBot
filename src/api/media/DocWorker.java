package api.media;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import api.client.Client;

public class DocWorker extends MediaWorker 
{
	public DocWorker(Client client) 
	{
		super(client);
	}
	
	protected Doc[] get(String IDs) throws Exception
	{
		String str  = client.executeCommand("docs.getById?"+
				"&docs="+IDs);
		
		JSONObject obj = new JSONObject(str);
		JSONArray response = obj.getJSONArray("response");
		
		int count = response.length();
		Doc[] docs = new Doc[count];
		
		for (int i=0;i<count;i++)
			docs[i] = getFromJSON(response.getJSONObject(i));
		
		return docs;
	}
	
	public Doc getFromJSON(JSONObject data) throws JSONException
	{
		Doc doc = new Doc();
		doc.ID = new MediaID(getIntFromJSON(data, "owner_id"), getIntFromJSON(data, "id"));	
		
		doc.title = getStringFromJSON(data, "title");
		doc.size = getLongFromJSON(data, "size");
		doc.extention = getStringFromJSON(data, "ext");
		doc.URL = getStringFromJSON(data, "url");
		doc.addingDate = getLongFromJSON(data, "date");
		doc.type = getIntFromJSON(data, "type");
		

		data = getObjectFromJSON(data, "preview");
		if (data!=null)
		{
			data = getObjectFromJSON(data, "photo");
			doc.photoURL = data.getJSONArray("sizes").getJSONObject(0).getString("src");
		}
		else doc.photoURL="";

		return doc;
	}
	
	public Doc getByID(MediaID ID) throws Exception
	{
		return (Doc)super.getByID(ID);
	}
	
	public Doc[] getByIDs(MediaID[] IDs) throws Exception
	{
		return (Doc[])super.getByIDs(IDs);
	}
}
