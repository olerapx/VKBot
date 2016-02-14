package message;
import java.io.IOException;
import java.net.URLEncoder;

import org.apache.http.client.ClientProtocolException;

import client.VKClient;
import worker.Worker;

public class MessageWorker extends Worker
{
	public MessageWorker(VKClient client) {
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
		sendMessageTo(msg,dest);
	}
	
	public void sendMessageToConference (Message msg, int receiverID) throws ClientProtocolException, IOException
	{
		String dest = "peer_id="+(2000000000+receiverID);
		sendMessageTo(msg,dest);
	}
	
	public void sendMessageToGroup (Message msg, int receiverID) throws ClientProtocolException, IOException
	{
		String dest = "peer_id="+(-receiverID);
		sendMessageTo(msg,dest);
	}
}
