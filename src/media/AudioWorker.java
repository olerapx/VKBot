package media;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import client.VKClient;
import worker.Worker;

public class AudioWorker extends Worker 
{

	public AudioWorker(VKClient client) {
		super(client);
		// TODO Auto-generated constructor stub
	}

	public Audio getByID(int ownerID, int audioID) throws ClientProtocolException, IOException, JSONException
	{
		String aid = ""+ownerID+"_"+audioID;
		InputStream stream = executeCommand("https://api.vk.com/method/"+
				"audio.getById?"+
				"&audios="+aid+
				"&v=5.45"+
				"&access_token="+client.token);
				
		JSONObject obj = new JSONObject(IOUtils.toString(stream, "UTF-8"));
		JSONObject data = obj.getJSONArray("response").getJSONObject(0);
		Audio audio = new Audio();
		audio.ID = data.getInt("id");
		audio.ownerID = data.getInt("owner_id");
		audio.artist = data.getString("artist");
		audio.title = data.getString("title");
		audio.duration = data.getLong("duration");
		
		if(data.has("lyrics_id"))
			audio.lyricsID = data.getInt("lyrics_id");
		else audio.lyricsID = 0;

		
		if (data.has("album_id"))
			audio.albumID = data.getInt("album_id");
		else audio.albumID=0;

		
		if (data.has("genre_id"))
			audio.genreID = data.getInt("genre_id");
		else audio.genreID=0;
		
		if(data.has("date"))
			audio.date = data.getInt("date");
		else audio.date=0;
		
		if (audio.lyricsID == 0) 
		{
			audio.lyrics="";
			return audio;
		}
					
		stream = executeCommand("https://api.vk.com/method/"+
				"audio.getLyrics?"+
				"&lyrics_id="+audio.lyricsID+
				"&v=5.45"+
				"&access_token="+client.token);
		obj = new JSONObject(IOUtils.toString(stream, "UTF-8"));
		obj = obj.getJSONObject("response");
		audio.lyrics = obj.getString("text");
		
		return audio;
	}
}
