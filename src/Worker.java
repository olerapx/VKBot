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
}
