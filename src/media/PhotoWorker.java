package media;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import worker.Worker;

public class PhotoWorker extends Worker {

	public PhotoWorker(CloseableHttpClient client, String access_token) {
		super(client, access_token);
	}

	public Photo getById(int ownerId, int photoId) throws ClientProtocolException, IOException, JSONException
	{
		String pid = ""+ownerId+"_"+photoId;
		InputStream stream = executeCommand("https://api.vk.com/method/"+
				"photos.getById?"+
				"&photos="+pid+
				"&access_token="+token);
		
		JSONObject obj = new JSONObject(IOUtils.toString(stream, "UTF-8"));
		JSONObject data = obj.getJSONArray("response").getJSONObject(0);
		
		Photo photo = new Photo();
		photo.id = data.getInt("pid");
		photo.albumId = data.getInt("aid");
		photo.ownerId = data.getInt("owner_id");
		
		try{
		photo.url=data.getString("src_xxbig");
		}catch (JSONException ex){
			photo.url=null;
		}
		
		photo.text = data.getString("text");
		photo.date = data.getLong("created");
		
		try{
			photo.userId = data.getInt("user_id");
		} catch(JSONException ex){
			photo.userId=0;
		}
		
		return photo;
	}
}
