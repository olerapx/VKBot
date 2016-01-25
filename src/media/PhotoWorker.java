package media;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import client.VKClient;
import worker.Worker;

public class PhotoWorker extends Worker {

	public PhotoWorker(VKClient client) {
		super(client);
	}

	public Photo getByID(int ownerID, int photoID) throws ClientProtocolException, IOException, JSONException
	{
		String pid = ""+ownerID+"_"+photoID;
		InputStream stream = executeCommand("https://api.vk.com/method/"+
					"photos.getById?"+
					"&photos="+pid+
					"&v=5.42"+
					"&access_token="+client.token);
				
		JSONObject obj = new JSONObject(IOUtils.toString(stream, "UTF-8"));
		JSONObject data = obj.getJSONArray("response").getJSONObject(0);
		
		Photo photo = new Photo();
		photo.ID = data.getInt("id");
		photo.albumID = data.getInt("album_id");
		photo.ownerID = data.getInt("owner_id");

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
		
		return photo;
	}
}
