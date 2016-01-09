package media;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import worker.Worker;

public class AudioWorker extends Worker 
{

	public AudioWorker(CloseableHttpClient client, String access_token) {
		super(client, access_token);
		// TODO Auto-generated constructor stub
	}

	public Audio getById(int ownerId, int audioId) throws ClientProtocolException, IOException, JSONException
	{
		String aid = ""+ownerId+"_"+audioId;
		InputStream stream = executeCommand("https://api.vk.com/method/"+
				"audio.getById?"+
				"&audios="+aid+
				"&access_token="+token);
		
		JSONObject obj = new JSONObject(IOUtils.toString(stream, "UTF-8"));
		JSONObject data = obj.getJSONArray("response").getJSONObject(0);
		Audio audio = new Audio();
		audio.id = data.getInt("aid");
		audio.ownerId = data.getInt("owner_id");
		audio.artist = data.getString("artist");
		audio.title = data.getString("title");
		audio.duration = data.getInt("duration");
		try{
			audio.lyricsId = data.getInt("lyrics_id");
		} catch (JSONException ex){
			audio.lyricsId = 0;
		}
		
		try{
			audio.albumId = data.getInt("album_id");
		} catch (JSONException ex){
			audio.albumId=0;
		}
		
		try{
			audio.genreId = data.getInt("genre_id");
		} catch (JSONException ex){
			audio.genreId=0;
		}	
		
		try{
			audio.date = data.getInt("date");
		}catch (JSONException ex){
				audio.date=0;
		}	
		
		if (audio.lyricsId==0) 
		{
			audio.lyrics="";
			return audio;
		}
					
		stream = executeCommand("https://api.vk.com/method/"+
				"audio.getLyrics?"+
				"&lyrics_id="+audio.lyricsId+
				"&access_token="+token);
		obj = new JSONObject(IOUtils.toString(stream, "UTF-8"));
		obj = obj.getJSONObject("response");
		audio.lyrics = obj.getString("text");
		
		return audio;
	}
}
