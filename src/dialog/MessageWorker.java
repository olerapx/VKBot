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
	
	private void sendMessageTo (Message msg, String dest) throws ClientProtocolException, IOException, JSONException
	{
		String url = "https://api.vk.com/method/"+
				"messages.send?"+
				 dest+
				"&v=5.45"+
				"&access_token="+this.client.token;
			
		if (msg.text!=null)
			url+="&message="+URLEncoder.encode(msg.text, "UTF-8".replace(".", "&#046;"));
		
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
		InputStream stream = executeCommand(url);
		updateMessage(stream, msg);
	}
	
	private void updateMessage (InputStream stream, Message msg) throws JSONException, IOException
	{		
		JSONObject obj = new JSONObject(IOUtils.toString(stream, "UTF-8"));
		msg.messageID = obj.getInt("response");
		msg.userID = client.me.ID();
		msg.isOut = true;
		msg.hasEmoji = true;
		msg.date=System.currentTimeMillis()/1000L;
	}

	public void sendMessageToUser (Message msg, int receiverID) throws ClientProtocolException, IOException, JSONException
	{
		String dest = "user_id="+receiverID;
		sendMessageTo(msg, dest);
	}
	
	public void sendMessageToUser (Message msg, String domain) throws ClientProtocolException, IOException, JSONException
	{
		String dest = "domain="+domain;
		sendMessageTo(msg, dest);
	}
	
	public void sendMessageToUser (Message msg, User user) throws ClientProtocolException, IOException, JSONException
	{
		String dest = "user_id="+user.ID();
		sendMessageTo(msg, dest);
	}
	
	public void sendMessageToConference (Message msg, int receiverID) throws ClientProtocolException, IOException, JSONException
	{
		String dest = "peer_id="+(2000000000+receiverID);
		sendMessageTo(msg, dest);
	}
	
	public void sendMessageToGroup (Message msg, int receiverID) throws ClientProtocolException, IOException, JSONException
	{
		String dest = "peer_id="+(-receiverID);
		sendMessageTo(msg, dest);
	}
	
	public void sendMessageToChat (Message msg, Dialog chat) throws ClientProtocolException, IOException, JSONException
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
		Message msg = new Message("");
		
		msg.messageID = item.getInt("id");
		msg.userID = item.getInt("user_id");
		msg.isOut = item.getInt("out") !=0;
		msg.date = item.getLong("date");
	    msg.title = item.getString("title");
	    msg.text = item.getString("body");
	    
	    if (item.has("emoji"))
	    	msg.hasEmoji = item.getInt("emoji")!=0;
	    
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
	
	public Message[] getByIDs(Integer[] IDs) throws ClientProtocolException, IOException, JSONException
	{
		String ids = "";
		for (int i=0;i<IDs.length-1;i++)
			ids+=IDs[i].toString()+",";
		ids+=IDs[IDs.length-1].toString();
		
		InputStream stream = executeCommand("https://api.vk.com/method/"+
				"messages.getById?"+
				"&message_ids="+ids+
				"&v=5.45"+
				"&access_token="+client.token);
			
		JSONObject obj = new JSONObject(IOUtils.toString(stream, "UTF-8"));
		JSONArray data = obj.getJSONObject("response").getJSONArray("items");
		
		int count = data.length();
		
		Message[] msgs = new Message[data.length()];
		for (int i=0;i<count;i++)
			msgs[i] = getFromResponse(data.getJSONObject(i));
		
		return msgs;
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
			dialogs[i] = getDialog(item);
	    }
	
	return dialogs;
	}
	
	private Dialog getDialog(JSONObject item) throws JSONException, ClientProtocolException, IOException
	{
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
		if (item.has("unread"))
			dialog.unreadMessagesNumber =  item.getInt("unread");
		else dialog.unreadMessagesNumber=0;
		
		if (dialog instanceof ConferenceDialog)
		{
			dialog.title = msg.getString("title");
			fillConferenceData ((ConferenceDialog)dialog, msg);
		}
		else if (dialog instanceof PrivateDialog)
		{
			fillPrivateData ((PrivateDialog)dialog, msg);
		}
		else if (dialog instanceof GroupDialog)
		{
			fillGroupData ((GroupDialog)dialog, msg);
		}
		
		dialog.lastMessage = getFromResponse(msg);		
		return dialog;	
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
	
	private void fillGroupData (GroupDialog dialog, JSONObject item) throws JSONException, ClientProtocolException, IOException
	{

	}
}
