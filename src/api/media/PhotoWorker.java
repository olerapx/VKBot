package api.media;

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
	
	protected Photo[] get (String IDs) throws Exception
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
	
	public static Photo getFromJSON(JSONObject data) throws JSONException
	{
		Photo photo = new Photo();
		photo.ID = new MediaID(getIntFromJSON(data, "owner_id"), getIntFromJSON(data, "id"));			
		photo.albumID = data.getInt("album_id");	

		if (data.has("photo_2560")) photo.URL = data.getString("photo_2560");
		else if (data.has("photo_1280")) photo.URL = data.getString("photo_1280");
		else if (data.has("photo_807")) photo.URL = data.getString("photo_807");
		else if (data.has("photo_604")) photo.URL = data.getString("photo_604");
		else if (data.has("photo_130")) photo.URL = data.getString("photo_130");
		else if (data.has("photo_75")) photo.URL = data.getString("photo_75");
		else photo.URL = "";
		
		photo.text = getStringFromJSON(data, "text");
		photo.date = getLongFromJSON(data, "date");
		
		photo.userID = getIntFromJSON(data, "user_id");
					
		JSONObject like = getObjectFromJSON(data, "likes");		
		if (like!=null)
			photo.likes = LikeWorker.getLike (like);
		else photo.likes = new Like();
		
		photo.canComment = getBooleanFromJSON(data, "can_comment");		
		photo.canRepost = getBooleanFromJSON(data, "can_repost");
		
		JSONObject comments = getObjectFromJSON(data, "comments");
		if (comments!=null) photo.commentsCount = getIntFromJSON(comments, "count");
		
		JSONObject reposts = getObjectFromJSON(data, "reposts");
		if (reposts!=null) photo.repostsCount = getIntFromJSON(reposts, "count");
		
		return photo;
	}
	
	public Photo getByID(MediaID ID) throws Exception
	{
		return (Photo)super.getByID(ID);
	}
	
	public Photo[] getByIDs(MediaID[] IDs) throws Exception
	{
		return (Photo[])super.getByIDs(IDs);
	}
	
	public PhotoComment[] getComments (Photo photo, int offset, int count)  throws Exception
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
				
		for (int i=0;i<commentsCount;i++)						
			comments[i] = (PhotoComment) CommentWorker.getFromJSON(items.getJSONObject(i), photo);
		
		return comments;
	}
}
