package worker;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;

public class Worker 
{
	protected CloseableHttpClient httpClient;
	protected String token;
	
	public Worker (CloseableHttpClient client, String access_token)
	{
		this.httpClient=client;
		this.token=access_token;
	}
	
	protected InputStream executeCommand(String command) throws ClientProtocolException, IOException
	{
		HttpPost post = new HttpPost(command);
		
		CloseableHttpResponse response;
		response = httpClient.execute(post);
		post.abort();		
		InputStream stream = response.getEntity().getContent();
		
		return stream;
	}
}
