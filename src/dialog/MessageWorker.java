package dialog;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import client.VKClient;
import user.User;
import user.UserWorker;
import worker.Worker;

public class MessageWorker extends Worker
{
	public MessageWorker(VKClient client) 
	{
		super(client);
	}
	
	private void sendMessageTo (Message msg, String dest) throws ClientProtocolException, IOException
	{
		String url = "https://api.vk.com/method/"+
				"messages.send?"+
				 dest+
				"&v=5.45"+
				"&access_token="+this.client.token;
			
		if (msg.body!=null)
			url+="&message="+URLEncoder.encode(msg.body, "UTF-8".replace(".", "&#046;"));
		
		if (msg.attachments!=null)
		{
			url+="&attachment=";
			for (int i=0;i<msg.attachments.length;i++)
			{
				url+=msg.attachments[i].toString();
				if (i<msg.attachments.length-1) url+=",";
			}			
		}
		
		if (msg.fwds!=null)
		{
			url+="&forward_messages=";
			for (int i=0;i<msg.fwds.length;i++)
			{
				url+=msg.fwds[i];
				if (i<msg.fwds.length-1) url+=",";
			}			
		}			
		executeCommand(url);
		msg.date=System.currentTimeMillis()/1000L;
	}

	public void sendMessageToUser (Message msg, int receiverID) throws ClientProtocolException, IOException
	{
		String dest = "user_id="+receiverID;
		sendMessageTo(msg, dest);
	}
	
	public void sendMessageToUser (Message msg, String domain) throws ClientProtocolException, IOException
	{
		String dest = "domain="+domain;
		sendMessageTo(msg, dest);
	}
	
	public void sendMessageToConference (Message msg, int receiverID) throws ClientProtocolException, IOException
	{
		String dest = "peer_id="+(2000000000+receiverID);
		sendMessageTo(msg, dest);
	}
	
	public void sendMessageToGroup (Message msg, int receiverID) throws ClientProtocolException, IOException
	{
		String dest = "peer_id="+(-receiverID);
		sendMessageTo(msg, dest);
	}
	
	public void sendMessageToChat (Message msg, Dialog chat) throws ClientProtocolException, IOException
	{
		String dest;
		
		if (chat instanceof ConferenceDialog)
			dest = "peer_id="+(2000000000 + chat.ID());
		else if (chat instanceof GroupDialog)
			dest = "peer_id="+(-chat.ID());
		else 
			dest = "user_id="+chat.ID();
		
		sendMessageTo(msg, dest);
	}
	
	private Message getFromResponse(JSONObject item) throws JSONException
	{	
		Message msg = new Message("bla");
		
		msg.messageID = item.getInt("id");
		msg.userID = item.getInt("user_id");
		msg.out = item.getInt("out") !=0;
		msg.date = item.getLong("date");
	    msg.title = item.getString("title");
	    msg.body = item.getString("body");
	    
	    if (item.has("emoji"))
	    	msg.emoji = item.getInt("emoji")!=0;
	    
	    if (item.has("attachments"))
	    {
	    	JSONArray atts = item.getJSONArray("attachments");
	    	msg.attachments = getAttachments(atts);
	    }
	    else msg.attachments = null;
	    
	    msg.fwds = null;
				
		return msg;
	}
	
	public Message getByID (int ID) throws ClientProtocolException, IOException, JSONException
	{
		InputStream stream = executeCommand("https://api.vk.com/method/"+
				"messages.getById?"+
				"&message_ids="+ID+
				"&v=5.45"+
				"&access_token="+client.token);
			
		JSONObject obj = new JSONObject(IOUtils.toString(stream, "UTF-8"));
		JSONObject data = obj.getJSONObject("response");
	
		return getFromResponse(data.getJSONArray("items").getJSONObject(0));
	}
	
	private Dialog[] getDialogs(String command) throws ClientProtocolException, IOException, JSONException
	{
		InputStream stream = executeCommand(command);
			
		JSONObject obj = new JSONObject(IOUtils.toString(stream, "UTF-8"));
		JSONObject data = obj.getJSONObject("response");
		JSONArray items = data.getJSONArray("items");
		int itemsCount = items.length();
						
		Dialog[] dialogs = new Dialog[itemsCount];
		
		for (int i=0;i<itemsCount;i++)
		{
			JSONObject item = items.getJSONObject(i);
			JSONObject msg = item.getJSONObject("message");
			
			Dialog dialog;
			if(msg.has("chat_id"))
			{
				dialog = new ConferenceDialog();
				dialog.ID = msg.getInt("chat_id");
			}
			else
			{
				int ID = msg.getInt("user_id");
				if(ID<0)
				{
					dialog = new GroupDialog();
					dialog.ID = -ID;
				}
				else
				{
					dialog = new PrivateDialog();
					dialog.ID = ID;
				}
			}
			if (data.has("unread"))
				dialog.isUnread =  item.getInt("unread");
			else dialog.isUnread=0;
			
			if (dialog instanceof ConferenceDialog)
			{
				dialog.title = msg.getString("title");
				fillConferenceData ((ConferenceDialog)dialog, msg);
			}
			else if (dialog instanceof PrivateDialog)
			{
				fillPrivateData ((PrivateDialog)dialog, msg);
			}
			//TODO: group
			
			dialog.lastMessage = getFromResponse(msg);		
			dialogs[i] = dialog;
	}
	
	return dialogs;
	}
	
	public Dialog[] getDialogs(int offset, int count, boolean isUnread) throws ClientProtocolException, IOException, JSONException
	{
		if (count<0 || count>200) count=200;
		int unread = (isUnread)? 1:0;
		
		String command = "https://api.vk.com/method/"+
				"messages.getDialogs?"+
				"&offset="+offset+
				"&count="+count+
				"&unread="+unread+
				"&v=5.45"+
				"&access_token="+client.token;
				
		return this.getDialogs(command);
	}
	
	public Dialog[] getDialogs(int offset, int count) throws ClientProtocolException, IOException, JSONException
	{
		if (count<0 || count>200) count=200;
		
		String command = "https://api.vk.com/method/"+
				"messages.getDialogs?"+
				"&offset="+offset+
				"&count="+count+
				"&v=5.45"+
				"&access_token="+client.token;
		
		return this.getDialogs(command);
	}
	
	private void fillConferenceData (ConferenceDialog dialog, JSONObject item) throws JSONException, ClientProtocolException, IOException
	{
		UserWorker uw = new UserWorker(client);
		JSONArray IDs = item.getJSONArray("chat_active");
		int count = IDs.length();
		
		Integer[] ids = new Integer[count];
				
	    for (int i=0;i<count;i++)
	    	ids[i] = IDs.getInt(i);
	    
	    dialog.users = uw.getByIDs(ids);
	    
	    dialog.admin = uw.getByID(item.getInt("admin_id"));
	    
	    if (item.has("photo_200"))
	    	dialog.photoURL = item.getString("photo_200");
	    else if (item.has("photo_100"))
	    	dialog.photoURL = item.getString("photo_100");
	    else if (item.has("photo_50"))
	    	dialog.photoURL = item.getString("photo_50");
	    else dialog.photoURL = "";
	}
	
	private void fillPrivateData (PrivateDialog dialog, JSONObject item) throws JSONException, ClientProtocolException, IOException
	{
		User user = new UserWorker(client).getByID(item.getInt("user_id"));
		dialog.title = user.firstName()+" "+user.lastName();
		
		dialog.user = user;
	}
}
