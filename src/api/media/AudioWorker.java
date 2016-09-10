package api.media;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import api.client.Client;

public class AudioWorker extends MediaWorker 
{
	private static final long serialVersionUID = -1301713478825710500L;
	
	public AudioWorker(Client client) 
	{
		super(client);
	}

	protected Audio[] get (String IDs) throws Exception
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
	
	public Audio getFromJSON(JSONObject data) throws Exception
	{	
		Audio audio = new Audio();

		audio.ID = new MediaID(getIntFromJSON(data, "owner_id"), getIntFromJSON(data, "id"));
		
		audio.artist = getStringFromJSON(data, "artist");
		audio.title = getStringFromJSON(data, "title");
		audio.duration = getLongFromJSON(data, "duration");
		
	    audio.lyricsID = getIntFromJSON(data, "lyrics_id");		
		audio.albumID = getIntFromJSON(data, "album_id");
		audio.genreID = getIntFromJSON(data, "genre_id");
		
		audio.genre = this.getGenre(audio.genreID);
		
		audio.date = getLongFromJSON(data, "date");
		
		if (audio.lyricsID == 0) 
			audio.lyrics="";
		else
			audio.lyrics = getLyrics (audio);
		
		return audio;
	}
	
	String getLyrics(Audio audio) throws Exception
	{
		String str = client.executeCommand("audio.getLyrics?"+
				"&lyrics_id="+audio.lyricsID);
		
		JSONObject obj = new JSONObject(str);
		obj = getObjectFromJSON(obj, "response");
		
		return obj.getString("text");
	}
		
	String getGenre (int genreID) throws FileNotFoundException, JSONException
	{
		String genres = "";
		
		Scanner in = new Scanner(new File("cfg/audiogenres.json"));
		while(in.hasNext())
			genres += in.nextLine() + "\r\n";
		in.close();
		
		JSONObject obj = new JSONObject(genres);
		JSONArray data = obj.getJSONArray("genres");
		int count = data.length();
		
		for (int i=0;i<count;i++)
		{
			obj = data.getJSONObject(i);
			if (obj.getInt("id") == genreID)
				return obj.getString("name");
		}
		return "";
	}
	
	public Audio getByID(MediaID ID) throws Exception
	{
		return (Audio)super.getByID(ID);
	}
	
	public Audio[] getByIDs(MediaID[] IDs) throws Exception
	{
		return (Audio[])super.getByIDs(IDs);
	}
}
