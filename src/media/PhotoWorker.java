package media;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import client.VKClient;
import worker.MediaWorker;

public class PhotoWorker extends MediaWorker 
{
	public PhotoWorker(VKClient client) 
	{
		super(client);
	}
	
	protected Photo[] get (String IDs) throws ClientProtocolException, IOException, JSONException
	{
		InputStream stream = executeCommand("https://api.vk.com/method/"+
				"photos.getById?"+
				"&photos="+IDs+
				"&v=5.45"+
				"&access_token="+client.token);
		
		JSONObject obj = new JSONObject(IOUtils.toString(stream, "UTF-8"));
		JSONArray response = obj.getJSONArray("response");
		
		int count = response.length();
		Photo[] photos = new Photo[count];
		
		for (int i=0;i<count;i++)
		{
			JSONObject data = response.getJSONObject(i);
			
			Photo photo = new Photo();
			photo.ID = new MediaID(data.getInt("owner_id"), data.getInt("id"));			
			photo.albumID = data.getInt("album_id");	

			if (data.has("photo_2560")) photo.URL = data.getString("photo_2560");
			else if (data.has("photo_1280")) photo.URL = data.getString("photo_1280");
			else if (data.has("photo_807")) photo.URL = data.getString("photo_807");
			else if (data.has("photo_604")) photo.URL = data.getString("photo_604");
			else if (data.has("photo_130")) photo.URL = data.getString("photo_130");
			else if (data.has("photo_75")) photo.URL = data.getString("photo_75");
			else photo.URL = "";
			
			photo.text = data.getString("text");
			photo.date = data.getLong("date");
			
			if (data.has("user_id"))
				photo.userID = data.getInt("user_id");
			else photo.userID=0;
						
			photos[i] = photo;
		}
		return photos;
	}
	
	public Photo getByID(MediaID ID) throws ClientProtocolException, IOException, JSONException
	{
		return (Photo)super.getByID(ID);
	}
	
	public Photo[] getByIDs(MediaID[] IDs) throws ClientProtocolException, IOException, JSONException
	{
		return (Photo[])super.getByIDs(IDs);
	}
}
