package media;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import client.VKClient;

public class VideoWorker extends MediaWorker 
{
	public VideoWorker(VKClient client) 
	{
		super(client);
	}
	
	protected Video[] get (String IDs) throws ClientProtocolException, IOException, JSONException
	{
		String str  = client.executeCommand("https://api.vk.com/method/"+
				"video.get?"+
				"&videos="+IDs+
				"&extended=1"+
				"&v=5.45"+
				"&access_token="+client.token);
		
		JSONObject obj = new JSONObject(str);
		JSONObject response = obj.getJSONObject("response");
		JSONArray items = response.getJSONArray("items");
		
		if (items.length()==0) return null; //not found or don't have key
		
		int count = items.length();
		Video[] videos = new Video[count];
		
		for (int i=0;i<count;i++)
		{
			JSONObject data = items.getJSONObject(i);
					
			Video video = new Video();
			video.ID = new MediaID(data.getInt("owner_id"), data.getInt("id"));	
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
			
			if (data.has("likes"))
			{
				JSONObject like = data.getJSONObject("likes");		
				video.likes = getLike (like);
			}
			else video.likes = null;
			
			video.canComment = data.getInt("can_comment")!=0;
			video.canRepost = data.getInt("can_repost")!=0;
			
			if(data.has("comments"))
				video.commentsCount = data.getInt("comments");
			else video.commentsCount = 0;	
			
			videos[i] = video;
		}
		return videos;
	}
	
	public Video getByID(MediaID ID) throws ClientProtocolException, IOException, JSONException
	{
		return (Video)super.getByID(ID);
	}
	
	public Video[] getByIDs(MediaID[] IDs) throws ClientProtocolException, IOException, JSONException
	{
		return (Video[])super.getByIDs(IDs);
	}
}
