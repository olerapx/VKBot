import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

public class StatusWorker extends Worker {
	
	public StatusWorker(CloseableHttpClient client, String access_token) {
		super(client, access_token);
	}
	
	public void setStatus (String status) throws ClientProtocolException, IOException
	{
		String url = "https://api.vk.com/method/"+
				"status.set?"+
				"text="+status+
				"&access_token="+token;
		
		HttpPost post = new HttpPost(url);
		httpClient.execute(post);
		post.abort();
	}
	
	public String getStatus () throws ClientProtocolException, IOException, JSONException //TODO: User/ UserID/ GroupID
	{
		HttpPost post = new HttpPost("https://api.vk.com/method/"+
				"status.get?"+
				"&access_token="+token);
		
		CloseableHttpResponse response;
		response = httpClient.execute(post);
		post.abort();
		
		InputStream stream = response.getEntity().getContent();
		JSONObject obj = new JSONObject(IOUtils.toString(stream, "UTF-8"));
		return obj.getJSONObject("response").getString("text");
	}
}
