package api.dialog;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import api.attachment.Attachment;
import api.attachment.AttachmentWorker;
import api.attachment.MediaAttachment;
import api.client.Client;
import api.user.User;
import api.user.UserWorker;
import api.worker.Worker;

public class MessageWorker extends Worker
{
	public MessageWorker(Client client) 
	{
		super(client);
	}
	
	private void sendMessageTo (Message msg, String dest) throws Exception
	{
		String url = "messages.send?"+ dest;
					
		url+="&message=" + URLEncoder.encode(msg.data.text(), "UTF-8".replace(".", "&#046;"));
				
		if (hasMediaAttachment(msg))
		{
			url+="&attachment=";
			
			for (int i=0;i<msg.data.attachments().length;i++)
			{
				Attachment att = msg.data.attachments()[i];
				if(!att.canAttach()) continue;
				
				url+=msg.data.attachments()[i].toString();
				if (i<msg.data.attachments().length-1) url+=",";
			}
		}
		
		if (msg.forwardMessagesIDs.length>0)
		{
			url+="&forward_messages=";
			for (int i=0;i<msg.forwardMessagesIDs.length;i++)
			{
				url+=msg.forwardMessagesIDs[i];
				if (i<msg.forwardMessagesIDs.length-1) url+=",";
			}			
		}
		
		String str = client.executeCommand(url);
		
	    updateMessage(new JSONObject (str), msg);
	}
	
	private boolean hasMediaAttachment(Message msg)
	{
		if (msg.data.attachments()==null) return false;
		
		for (int i=0;i<msg.data.attachments().length;i++)
			if (msg.data.attachments()[i] instanceof MediaAttachment) return true;
		
		return false;
	}
	
	private void updateMessage (JSONObject response, Message msg) throws Exception
	{		
		msg.messageID = response.getInt("response");
		msg.data.userID = client.me.ID();
		msg.isOut = true;
		msg.isRead = true;
		msg.hasEmoji = true;
		msg.data.date=System.currentTimeMillis()/1000L;
	}

	public void sendMessageToUser (Message msg, int receiverID) throws Exception
	{
		String dest = "user_id="+receiverID;
		sendMessageTo(msg, dest);
	}
	
	public void sendMessageToUser (Message msg, String domain) throws Exception
	{
		String dest = "domain="+domain;
		sendMessageTo(msg, dest);
	}
	
	public void sendMessageToUser (Message msg, User user) throws Exception
	{
		String dest = "user_id="+user.ID();
		sendMessageTo(msg, dest);
	}
	
	public void sendMessageToConference (Message msg, int receiverID) throws Exception
	{
		String dest = "peer_id="+(2000000000+receiverID);
		sendMessageTo(msg, dest);
	}
	
	public void sendMessageToGroup (Message msg, int receiverID) throws Exception
	{
		String dest = "peer_id="+(-receiverID);
		sendMessageTo(msg, dest);
	}
	
	public void sendMessageToChat (Message msg, Dialog chat) throws Exception
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
		
	private Message[] get(String IDs) throws Exception
	{
		String str = client.executeCommand("messages.getById?"+
				"&message_ids="+IDs);
			
		JSONObject obj = new JSONObject(str);
		JSONArray items = obj.getJSONObject("response").getJSONArray("items");
		
		int count = items.length();
		Message[] messages = new Message[count];
		
		for (int i=0;i<count;i++)
		{
			JSONObject data = items.getJSONObject(i);
		    messages[i] = getFromJSON(data);
		}
				
		return messages;
	}
	
	public Message getFromJSON(JSONObject response) throws JSONException
	{
		Message msg = new Message("");
				
		msg.messageID = response.getInt("id");
		msg.isOut = response.getInt("out") !=0;
		msg.isRead = response.getInt("read_state")!=0;
		
		msg.data = getDataFromJSON(response);
	    
	    if (response.has("emoji"))
	    	msg.hasEmoji = response.getInt("emoji")!=0;
	    	    
	    msg.forwardMessagesIDs = new Integer[0]; 
	    return msg;
	}
	
	public MessageData getDataFromJSON(JSONObject data) throws JSONException
	{
		MessageData msg = new MessageData();	
		
		msg.userID = data.getInt("user_id");
		msg.date = data.getLong("date");
		
		if (data.has("title"))
			msg.title = data.getString("title");
		
	    msg.text = data.getString("body");
	    	    
	    if (data.has("attachments"))
	    {
	    	JSONArray atts = data.getJSONArray("attachments");
	    	msg.attachments = new AttachmentWorker(this.client).getFromJSONArray(atts);
	    }
	    else msg.attachments = new Attachment[0];
	    
	    if (data.has("fwd_messages"))
	    {
	    	JSONArray fwds = data.getJSONArray("fwd_messages");
	    	
	    	int count = fwds.length();
	    	msg.forwardMessages = new MessageData[count];
	    	
	    	for (int i=0;i<count;i++)
	    		msg.forwardMessages[i] = getDataFromJSON(fwds.getJSONObject(i));
	    }
	    else msg.forwardMessages = new MessageData[0];
	    
	    return msg;
	}
	
	public Message getByID (int ID) throws Exception
	{
		String id = ""+ID;	
		return this.get(id)[0];
	}
	
	public Message[] getByIDs(Integer[] IDs) throws Exception
	{
		if (IDs.length<=0) return new Message[0];

		String ids = "";

		for (int i=0;i<IDs.length-1;i++)
			ids+=IDs[i].toString()+",";
		ids+=IDs[IDs.length-1].toString();
		
		return this.get(ids);
	}
	
	
	
	public Message[] getHistory(Dialog dialog, int offset, int count) throws Exception
	{
		return this.getHistory(dialog, offset, count, 0, false);
	}
	
	public Message[] getHistory(Dialog dialog, int offset, int count, boolean reverse) throws Exception
	{
		return this.getHistory(dialog, offset, count, 0, reverse);
	}
	
	public Message[] getHistory(Dialog dialog, int offset, int count, Message startMessage) throws Exception
	{
		return this.getHistory(dialog, offset, count, startMessage.messageID, false);
	}
	
	public Message[] getHistoryFromLastUnread (Dialog dialog, int offset, int count) throws Exception
	{
		return this.getHistory(dialog, offset, count, -1, false);
	}
	
	private Message[] getHistory(Dialog dialog, int offset, int count, int startMessageID, boolean reverse) throws Exception
	{
		String dest;
		
		if (dialog instanceof PrivateDialog)
			dest = "&user_id="+dialog.ID;
		else if (dialog instanceof ConferenceDialog)
			dest = "&peer_id="+(2000000000+dialog.ID);
		else 
			dest = "&peer_id="+(-dialog.ID);
		
		return getHistory(dest, offset, count, startMessageID, reverse);
	}

	public Message[] getHistory(User user, int offset, int count) throws Exception
	{
		String dest = "&user_id="+user.ID();
		return this.getHistory(dest, offset, count, 0, false);
	}
	
	public Message[] getHistory(User user, int offset, int count, boolean reverse) throws Exception
	{
		String dest = "&user_id="+user.ID();
		return this.getHistory(dest, offset, count, 0, reverse);
	}
	
	public Message[] getHistory(User user, int offset, int count, Message startMessage) throws Exception
	{
		String dest = "&user_id="+user.ID();
		return this.getHistory(dest, offset, count, startMessage.messageID, false);
	}
	
	public Message[] getHistoryFromLastUnread (User user, int offset, int count) throws Exception
	{
		String dest = "&user_id="+user.ID();
		return this.getHistory(dest, offset, count, -1, false);
	}
		
	private Message[] getHistory(String dest, int offset, int count, int startID, boolean reverse) throws Exception
	{
		if (count<0 || count>200) count=200;
				
		String startMessageID = (startID==0) ? ""  : "&start_message_id=" + startID;
		
		String rev = (startID==0) ? "&rev=" + (reverse? 1: 0) :""; 
		
		String str = client.executeCommand("messages.getHistory?"+
												dest+
											    startMessageID+
											    "&offset="+offset+
											    "&count="+count+
											    rev);
		
		JSONObject obj = new JSONObject(str);
		JSONObject data = obj.getJSONObject("response");		
		JSONArray items = data.getJSONArray("items");
		
		int itemsCount = items.length();
		
		Message[] msgs = new Message[itemsCount];
		
		for (int i=0;i<itemsCount;i++)
			msgs[i] = getFromJSON(items.getJSONObject(i));
			
		return msgs;
	}
			
	
	
	public Dialog[] getDialogs(int offset, int count, boolean isUnread) throws Exception
	{
		if (count<0 || count>200) count=200;
		int unread = (isUnread)? 1:0;
		
		String command = "messages.getDialogs?"+
				"&offset="+offset+
				"&count="+count+
				"&unread="+unread;
				
		return this.getDialogs(command);
	}
	
	private Dialog[] getDialogs(String command) throws Exception
	{
		String str = client.executeCommand(command);
			
		JSONObject obj = new JSONObject(str);
		JSONArray items = obj.getJSONObject("response").getJSONArray("items");
		int itemsCount = items.length();
						
		Dialog[] dialogs = new Dialog[itemsCount];
		
		for (int i=0;i<itemsCount;i++)
		{
			JSONObject item = items.getJSONObject(i);			
			dialogs[i] = getDialog(item);
	    }
	
		return dialogs;
	}
	
	private Dialog getDialog(JSONObject item) throws Exception
	{
		JSONObject msg = item.getJSONObject("message");
		
		Dialog dialog;
		
		if(msg.has("chat_id"))
		{
			dialog = new ConferenceDialog();
			dialog.ID = msg.getInt("chat_id");
			
			dialog.title = msg.getString("title");
			fillConferenceData ((ConferenceDialog)dialog, msg);
		}
		else
		{
			int ID = msg.getInt("user_id");
			if (ID<0)
			{
				dialog = new GroupDialog();
				dialog.ID = -ID;
				fillGroupData ((GroupDialog)dialog, msg);
			}
			else
			{
				dialog = new PrivateDialog();
				dialog.ID = ID;
				fillPrivateData ((PrivateDialog)dialog, msg);
			}
		}
		
		if (item.has("unread"))
			dialog.unreadMessagesNumber =  item.getInt("unread");
				
		dialog.lastMessage = getFromJSON(msg);		
		return dialog;	
	}
	
	public Dialog[] getDialogs(int offset, int count) throws Exception
	{
		if (count<0 || count>200) count=200;
		
		String command = "messages.getDialogs?"+
				"&offset="+offset+
				"&count="+count;
		
		return this.getDialogs(command);
	}
	
	private void fillConferenceData (ConferenceDialog dialog, JSONObject item) throws Exception
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
	
	private void fillPrivateData (PrivateDialog dialog, JSONObject item) throws Exception
	{
		User user = new UserWorker(client).getByID(item.getInt("user_id"));
		
		dialog.title = user.firstName()+" "+user.lastName();		
		dialog.user = user;
	}
	
	private void fillGroupData (GroupDialog dialog, JSONObject item) throws Exception
	{

	}
}
