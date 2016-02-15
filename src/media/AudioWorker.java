package media;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import client.VKClient;
import worker.Worker;

public class AudioWorker extends Worker 
{
	public AudioWorker(VKClient client) 
	{
		super(client);
	}

	private Audio[] get (String ids) throws ClientProtocolException, IOException, JSONException
	{
		InputStream stream = executeCommand("https://api.vk.com/method/"+
				"audio.getById?"+
				"&audios="+ids+
				"&v=5.45"+
				"&access_token="+client.token);
				
		JSONObject obj = new JSONObject(IOUtils.toString(stream, "UTF-8"));
		JSONArray response = obj.getJSONArray("response");
		
		int count = response.length();
		Audio[] audios = new Audio[count];
		
		for (int i=0;i<count;i++)
		{
			JSONObject data = response.getJSONObject(i);
			
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
			
			audio.genre = this.getGenre(audio.genreID);
			
			if(data.has("date"))
				audio.date = data.getInt("date");
			else audio.date=0;
			
			if (audio.lyricsID == 0) 
			{
				audio.lyrics="";
			}
			else
			{
				stream = executeCommand("https://api.vk.com/method/"+
						"audio.getLyrics?"+
						"&lyrics_id="+audio.lyricsID+
						"&v=5.45"+
						"&access_token="+client.token);
				obj = new JSONObject(IOUtils.toString(stream, "UTF-8"));
				obj = obj.getJSONObject("response");
				audio.lyrics = obj.getString("text");	
			}
			audios[i] = audio;
		}		
		return audios;
	}
	
	public Audio getByID(int ownerID, int audioID) throws ClientProtocolException, IOException, JSONException
	{
		String aid = ""+ownerID+"_"+audioID;
		
		return this.get(aid)[0];
	}
	
	public Audio[] getByIDs(Integer[][] IDs) throws ClientProtocolException, IOException, JSONException
	{
		String ids="";
		for (int i=0;i<IDs.length-1;i++)
		{
			ids+=IDs[i][0].toString()+"_" + IDs[i][1].toString()+",";
		}
		ids+=IDs[IDs.length-1][0].toString()+"_" + IDs[IDs.length-1][1].toString();
		
		return this.get(ids);
	}
	
	String getGenre (int genreID) throws FileNotFoundException, JSONException
	{
		String s = "";
		Scanner in = new Scanner(new File("cfg/audiogenres.json"));
		while(in.hasNext())
		s += in.nextLine() + "\r\n";
		in.close();
		
		JSONObject obj = new JSONObject(s);
		JSONArray data = obj.getJSONArray("genres");
		int count = data.length();
		
		for (int i=0;i<count;i++)
		{
			obj = data.getJSONObject(i);
			if (obj.getInt("id")==genreID)
				return obj.getString("name");
		}
		return "";
	}
}
