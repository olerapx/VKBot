package user;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import worker.Worker;

public class UserWorker extends Worker 
{
	public UserWorker(CloseableHttpClient client, String access_token) {
		super(client, access_token);
	}
	private User get(String ids) throws JSONException, UnsupportedOperationException, IOException
	{
		if (ids==null) ids="";
		HttpPost post = new HttpPost("https://api.vk.com/method/"+
				"users.get?"+
				"&user_ids="+ids+
				"&fields=domain,nickname,maiden_name,wall_comments,"
				+ "can_post,can_see_all_posts,can_see_audio,can_write_private_message,"
				+ "is_friend,can_send_friend_request,has_photo,photo_id,"
				+ "photo_max_orig,sex,bdate,online,followers_count,common_count"+
				"&access_token="+token);
		
		CloseableHttpResponse response;
		response = httpClient.execute(post);
		post.abort();
		
		InputStream stream = response.getEntity().getContent();
		JSONObject obj = new JSONObject(IOUtils.toString(stream, "UTF-8"));
		
		User user = new User();
		JSONObject data= obj.getJSONArray("response").getJSONObject(0);
		
		user.id = data.getInt("uid");
		try{ 
			user.domain= data.getString("domain");
		} catch (JSONException ex){
			user.domain=null;
		}
		
		user.firstName = data.getString("first_name");
		user.lastName=data.getString("last_name");

		try{ 
			user.nickname= data.getString("nickname");
		} catch (JSONException ex){
			user.nickname=null;
		}
		
		try{ 
			user.maidenName= data.getString("maiden_name");
		} catch (JSONException ex){
			user.maidenName=null;
		}
		
		try{ 
			user.isDeactivated= data.getBoolean("deactivated");
		} catch (JSONException ex){
			user.isDeactivated=false;
		}
		
		user.canComment =(data.getInt("wall_comments")!=0);
		user.canPost = (data.getInt("can_post")!=0);
		user.canSeeAllPosts= (data.getInt("can_see_all_posts")!=0);
		user.canSeeAudio= (data.getInt("can_see_audio")!=0);
		user.canWriteMessage= (data.getInt("can_write_private_message")!=0);
		user.isFriend = (data.getInt("is_friend")!=0);
		user.canAddToFriends =(data.getInt("can_send_friend_request")!=0);
		user.hasPhoto = (data.getInt("has_photo")!=0);
				
		try{ 
			user.photoId= data.getInt("photo_id");
		} catch (JSONException ex){
			user.photoId=null;
		}
		
		try{ 
			user.photoURL= data.getString("photo_max_orig");
		} catch (JSONException ex){
			user.photoURL=null;
		}
		
		user.sex = data.getInt("sex");
		try{
		user.birthDate = data.getString("bdate");
		} catch (JSONException ex){
			user.birthDate=null;
		}
		
		user.isOnline = (data.getInt("online")!=0);
		user.followersCount = data.getInt("followers_count");
		user.commonCount = data.getInt("common_count");
		
		return user;
	}

	
	public User getById(Integer id) throws ClientProtocolException, IOException, JSONException
	{
		return this.get(id.toString());
	}
	
	public User getMe() throws UnsupportedOperationException, JSONException, IOException
	{
		return this.get(null);
	}
	
	public User[] getFriends(User user) throws ClientProtocolException, IOException //TODO:sort
, JSONException
	{		
		InputStream stream = executeCommand("https://api.vk.com/method/"+
				"friends.get?"+
				"&user_id="+user.id+
				"&order=hints"+
				"&access_token="+token);
		
		JSONObject obj = new JSONObject(IOUtils.toString(stream, "UTF-8"));
		JSONArray data= obj.getJSONArray("response");
		
		int count = data.length();

		User[] friends = new User[count];
		for (int i=0;i<count;i++)
			friends[i] = getById(data.getInt(i));
		
		return friends;
	}
	
	public Integer addToFriends(User user, String text) throws ClientProtocolException, IOException, JSONException
	{
		if (user.canAddToFriends== false) return -1;
		
		InputStream stream = executeCommand("https://api.vk.com/method/"+
				"friends.add?"+
				"&user_id="+user.id+
				"&text"+URLEncoder.encode(text, "UTF-8")+
				"&follow=0"+
				"&access_token="+token);
		
		JSONObject obj = new JSONObject(IOUtils.toString(stream, "UTF-8"));
		
		return obj.getInt("response");
	}
}
