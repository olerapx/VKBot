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

public class VideoWorker extends Worker 
{
	public VideoWorker(VKClient client) 
	{
		super(client);
	}
	
	public Video getByID (int ownerID, int videoID) throws ClientProtocolException, IOException, JSONException
	{
		String vid = ""+ownerID+"_"+videoID;
		InputStream stream = executeCommand("https://api.vk.com/method/"+
				"video.get?"+
				"&videos="+vid+
				"&v=5.45"+
				"&access_token="+client.token);
				
		JSONObject obj = new JSONObject(IOUtils.toString(stream, "UTF-8"));
		JSONObject data = obj.getJSONObject("response");
				
		JSONArray array = data.getJSONArray("items");
		if (array.length()==0) return null; //not found or don't have key
		
		data = array.getJSONObject(0);
				
		Video video = new Video();
		video.ID = data.getInt("id");
		video.ownerID = data.getInt("owner_id");
		video.title = data.getString("title");
		video.description = data.getString("description");
		video.duration = data.getLong("duration");
		
		if(data.has("photo_640"))
			video.photoURL = data.getString("photo_640");
		else if (data.has("photo_320"))
			video.photoURL = data.getString("photo_320");
		else if (data.has("photo_130"))
			video.photoURL = data.getString("photo_130");
		else video.photoURL = "";
		
		video.creationDate = data.getLong("date");
		if (data.has("adding_date"))
			video.addingDate = data.getLong("adding_date");
		else video.addingDate=0;
		
		video.viewsNumber = data.getInt("views");
		video.commentsNumber = data.getInt("comments");
		
		if (data.has("player"))
			video.playURL = data.getString("player");
		else video.playURL = "";
		
		if (data.has("live"))
			video.isLive = true;
		else video.isLive = false;
		
		video.accessKey="";
		
		return video;		
	}
}
