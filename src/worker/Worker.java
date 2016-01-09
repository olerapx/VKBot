package worker;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;

import client.VKClient;

public class Worker 
{
	protected VKClient client;
	
	public Worker (VKClient client)
	{
		this.client=client;
	}
	
	protected InputStream executeCommand(String command) throws ClientProtocolException, IOException
	{
		HttpPost post = new HttpPost(command);
		
		CloseableHttpResponse response;
		response = client.httpClient.execute(post);
		post.abort();		
		InputStream stream = response.getEntity().getContent();
		
		return stream;
	}
}
