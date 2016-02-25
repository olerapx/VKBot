package media;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import client.Client;

public class AudioWorker extends MediaWorker 
{
	public AudioWorker(Client client) 
	{
		super(client);
	}

	protected Audio[] get (String IDs) throws ClientProtocolException, IOException, JSONException
	{
		String str = client.executeCommand("audio.getById?"+
				"&audios="+IDs);
				
		JSONObject obj = new JSONObject(str);
		JSONArray response = obj.getJSONArray("response");
		
		int count = response.length();
		Audio[] audios = new Audio[count];
		
		for (int i=0;i<count;i++)			
			audios[i] = getFromJSON(response.getJSONObject(i));	
		
		return audios;
	}
	
	public Audio getFromJSON(JSONObject data) throws JSONException, ClientProtocolException, IOException
	{	
		Audio audio = new Audio();

		audio.ID = new MediaID(data.getInt("owner_id"), data.getInt("id"));
		audio.artist = data.getString("artist");
		audio.title = data.getString("title");
		audio.duration = data.getLong("duration");
		
		if(data.has("lyrics_id"))
			audio.lyricsID = data.getInt("lyrics_id");
		
		if (data.has("album_id"))
			audio.albumID = data.getInt("album_id");
		
		if (data.has("genre_id"))
			audio.genreID = data.getInt("genre_id");
		
		audio.genre = this.getGenre(audio.genreID);
		
		if(data.has("date"))
			audio.date = data.getLong("date");
		
		if (audio.lyricsID == 0) 
			audio.lyrics="";
		else
			audio.lyrics = getLyrics (audio);
		
		return audio;
	}
	
	String getLyrics(Audio audio) throws ClientProtocolException, IOException, JSONException
	{
		String str = client.executeCommand("audio.getLyrics?"+
				"&lyrics_id="+audio.lyricsID);
		
		JSONObject obj = new JSONObject(str);
		obj = obj.getJSONObject("response");
		return obj.getString("text");
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
	
	public Audio getByID(MediaID ID) throws ClientProtocolException, IOException, JSONException
	{
		return (Audio)super.getByID(ID);
	}
	
	public Audio[] getByIDs(MediaID[] IDs) throws ClientProtocolException, IOException, JSONException
	{
		return (Audio[])super.getByIDs(IDs);
	}
}
