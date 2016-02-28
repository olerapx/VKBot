package api.media;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import api.client.Client;
import api.media.comment.CommentWorker;
import api.media.comment.PhotoComment;

public class PhotoWorker extends MediaWorker 
{
	public PhotoWorker(Client client) 
	{
		super(client);
	}
	
	protected Photo[] get (String IDs) throws ClientProtocolException, IOException, JSONException
	{
		String str  = client.executeCommand("photos.getById?"+
				"&photos="+IDs+
				"&extended=1");
		
		JSONObject obj = new JSONObject(str);
		JSONArray response = obj.getJSONArray("response");
		
		int count = response.length();
		Photo[] photos = new Photo[count];
		
		for (int i=0;i<count;i++)	
			photos[i] = getFromJSON(response.getJSONObject(i));
			
		return photos;
	}
	
	public Photo getFromJSON(JSONObject data) throws JSONException
	{
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
					
		if (data.has("likes"))
		{
			JSONObject like = data.getJSONObject("likes");		
			photo.likes = new LikeWorker(client).getLike (like);
		}
		else photo.likes = new Like();
		
		photo.canComment = data.getInt("can_comment")!=0;
		photo.canRepost = data.getInt("can_repost")!=0;
		
		if(data.has("comments"))
			photo.commentsCount = data.getJSONObject("comments").getInt("count");
		
		if(data.has("reposts"))
			photo.repostsCount = data.getJSONObject("reposts").getInt("count");
		
		return photo;
	}
	
	public Photo getByID(MediaID ID) throws ClientProtocolException, IOException, JSONException
	{
		return (Photo)super.getByID(ID);
	}
	
	public Photo[] getByIDs(MediaID[] IDs) throws ClientProtocolException, IOException, JSONException
	{
		return (Photo[])super.getByIDs(IDs);
	}
	
	public PhotoComment[] getComments (Photo photo, int offset, int count)  throws ClientProtocolException, IOException, JSONException
	{
		if (count>100 || count <0) count = 100;
		
		MediaID ID = photo.ID();
		
		String str  = client.executeCommand("photos.getComments?"+
					"&owner_id="+ID.ownerID()+
					"&photo_id="+ID.mediaID()+
					"&need_likes=1"+
					"&offset="+offset+
					"&count="+count+
					"&preview_length=0"+
					"&extended=0");
				
		JSONObject obj = new JSONObject(str);
		JSONObject data = obj.getJSONObject("response");
		
		JSONArray items = data.getJSONArray("items");	
		int commentsCount = items.length();
		
		PhotoComment[] comments = new PhotoComment[commentsCount];
		
		CommentWorker cw = new CommentWorker(client);
		
		for (int i=0;i<commentsCount;i++)						
			comments[i] = (PhotoComment) cw.getFromJSON(items.getJSONObject(i), photo);
		
		return comments;
	}
}