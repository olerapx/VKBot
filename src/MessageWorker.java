import java.io.IOException;
import java.net.URLEncoder;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;

public class MessageWorker extends Worker
{
	public MessageWorker(CloseableHttpClient client, String access_token) {
		super(client, access_token);
	}

	public void sendMessage (Message msg) throws ClientProtocolException, IOException
	{ 
		String url = "https://api.vk.com/method/"+
				"messages.send?"+
				"user_id="+msg.userId+
				"&access_token="+this.token;
		
		if (msg.body!=null)
			url+="&message="+URLEncoder.encode(msg.body, "UTF-8");
		
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
				
		HttpPost post = new HttpPost(url);
		httpClient.execute(post);
		post.abort();
	}	

	public void sendMessage (Message msg, int receiverId) throws ClientProtocolException, IOException
	{
		msg.userId=receiverId;
		sendMessage(msg);
	}
	
}
