package status;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import worker.Worker;

public class StatusWorker extends Worker {
	
	public StatusWorker(CloseableHttpClient client, String access_token) {
		super(client, access_token);
	}
	
	public void setStatus (String status) throws ClientProtocolException, IOException
	{
		executeCommand("https://api.vk.com/method/"+
				"status.set?"+
				"text="+status+
				"&access_token="+token);
	}
	
	public String getStatus () throws ClientProtocolException, IOException, JSONException //TODO: User/ UserID/ GroupID
	{
		InputStream stream = executeCommand("https://api.vk.com/method/"+
				"status.get?"+
				"&access_token="+token);

		JSONObject obj = new JSONObject(IOUtils.toString(stream, "UTF-8"));
		return obj.getJSONObject("response").getString("text");
	}
}
