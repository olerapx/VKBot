import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;

import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.ElementIterator;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class VKClient 
{
	public CloseableHttpClient httpClient;
	public String token="";
	
	public VKClient()
	{
		 RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
		 CookieStore cookieStore = new BasicCookieStore();
		 HttpClientContext context = HttpClientContext.create();
		 context.setCookieStore(cookieStore);
		 httpClient = HttpClients.custom().setDefaultRequestConfig(globalConfig).setDefaultCookieStore(cookieStore).build();	
	}	

	public void connect(String email, String pass) throws IOException, URISyntaxException, BadLocationException 
	{	
		String id = "4977827";
		String scope="friends,photos,audio,video,docs,status,wall,messages,offline";
		String redirectUri = "https://oauth.vk.com/blank.html";
		String display = "popup";
		String responseType = "token";
		
		//post to get code		 
		HttpPost post = new HttpPost("https://oauth.vk.com/authorize?" +
				"client_id="+id+
				"&redirect_uri="+redirectUri+
				"&display="+display+
				"&scope="+scope+	
				"&response_type="+responseType);
		CloseableHttpResponse response;
		response = httpClient.execute(post);
		post.abort();
		
		InputStream stream = response.getEntity().getContent();
		
		HTMLEditorKit kit = new HTMLEditorKit(); 
		HTMLDocument doc = (HTMLDocument) kit.createDefaultDocument(); 
		doc.putProperty("IgnoreCharsetDirective", Boolean.TRUE);
		Reader HTMLReader = new InputStreamReader(stream); 
		kit.read(HTMLReader, doc, 0); 
		
	    ElementIterator it = new ElementIterator(doc); 
	    Element elem; 
	    
	    String ip_h="", lg_h="", to="";
	    	    
	    while((elem=it.next()) != null)
	    { 
	      if(elem.getName().equals("input"))
	      { 
	    	  String name = (String)elem.getAttributes().getAttribute(HTML.Attribute.NAME);
	    	  if (name==null) continue;
	    	  
	    	  if (name.equals("ip_h"))
	    		 ip_h = (String) elem.getAttributes().getAttribute(HTML.Attribute.VALUE);	  		  
	    	  else if (name.equals("lg_h"))
	    		 lg_h=(String) elem.getAttributes().getAttribute(HTML.Attribute.VALUE);
	    	  else if (name.equals("to"))
	    		 to=(String) elem.getAttributes().getAttribute(HTML.Attribute.VALUE);
	      } 
	    }
		 
		//authorization post
		post = new HttpPost("https://login.vk.com/?act=login&soft=1"+
				"&q=1"+
				"&ip_h="+ip_h+
				"&lg_h="+lg_h+
				"&_origin=oauth.vk.com"+
				"&to="+to+
				"&expire=0"+
				"&email="+email+
				"&pass="+pass);
		response = httpClient.execute(post);
		post.abort();
				
		//application rights
		String HeaderLocation = response.getFirstHeader("location").getValue();
		post = new HttpPost(HeaderLocation);
		response = httpClient.execute(post);
		post.abort();
		
		// get that token
		HeaderLocation = response.getFirstHeader("location").getValue();
		post = new HttpPost(HeaderLocation);
		response = httpClient.execute(post);
		post.abort();

		HeaderLocation = response.getFirstHeader("location").getValue();

		token = HeaderLocation.split("#")[1].split("&")[0].split("=")[1];	
	}

}
