package client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

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

import user.User;
import user.UserWorker;

public class VKClient 
{
	public CloseableHttpClient httpClient;
	public String token="";
	public User me;
	
	String ID = "4977827";
	String scope="friends,photos,audio,video,docs,status,wall,messages,offline";
	String redirectUri = "https://oauth.vk.com/blank.html";
	String display = "popup";
	String responseType = "token";
	String email="";
	String pass="";

	String ip_h="";
	String lg_h="";
	String to="";
	
	String captchaSid="";
	String captchaKey="";
	String captchaURL="";
	
	CloseableHttpResponse response;
	
	public VKClient(String email, String pass) throws Exception
	{
		 RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
		 CookieStore cookieStore = new BasicCookieStore();
		 HttpClientContext context = HttpClientContext.create();
		 context.setCookieStore(cookieStore);
		 httpClient = HttpClients.custom().setDefaultRequestConfig(globalConfig).setDefaultCookieStore(cookieStore).build();	
		 
		this.email=email;
		this.pass=pass;
		connect();
	}	

	public void connect() throws Exception
	{	
		//post to get code		 
		HttpPost post = new HttpPost("https://oauth.vk.com/authorize?" +
				"client_id="+ID+
				"&redirect_uri="+redirectUri+
				"&display="+display+
				"&scope="+scope+	
				"&response_type="+responseType);
		response = httpClient.execute(post);
		//post.abort();
		
		InputStream stream = response.getEntity().getContent();	

		HTMLDocument doc = streamToHtml(stream);
		stream.close();
		
	    ElementIterator it = new ElementIterator(doc); 
	    Element elem; 
	    	    	    	    
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
				"&_origin=https://oauth.vk.com"+
				"&to="+to+
				"&expire=0"+
				"&email="+email+
				"&pass="+pass);
		response = httpClient.execute(post);
		//post.abort();
	
		//application rights
		String HeaderLocation = response.getFirstHeader("location").getValue();
		post = new HttpPost(HeaderLocation);
		response = httpClient.execute(post);
		
		System.out.println(HeaderLocation);
		
		while(response.containsHeader("location")==false)
		{
			handleCaptcha();
			
			HeaderLocation = response.getFirstHeader("location").getValue();
			post = new HttpPost(HeaderLocation);
			response = httpClient.execute(post);
			//post.abort();
		}
			
		// get that token    
		HeaderLocation = response.getFirstHeader("location").getValue();
		post = new HttpPost(HeaderLocation);
		response = httpClient.execute(post);
	//	post.abort();

		HeaderLocation = response.getFirstHeader("location").getValue();

		token = HeaderLocation.split("#")[1].split("&")[0].split("=")[1];	
		UserWorker uw = new UserWorker(this);
		me = uw.getMe();		
	}
	
	private void handleCaptcha() throws UnsupportedOperationException, IOException, BadLocationException //TODO: Make that shit work
	{
		InputStream stream = response.getEntity().getContent(); 
		HTMLDocument doc = streamToHtml(stream);
		stream.close();
		
	    ElementIterator it = new ElementIterator(doc); 
	    Element elem; 
	   		    	    
	    while((elem=it.next()) != null)
	    { 
	      if(elem.getName().equals("img"))
	    	  captchaURL= (String)elem.getAttributes().getAttribute(HTML.Attribute.SRC);	
	      
	      else if (elem.getName().equals("input"))
	      {
	    	  String name = (String)elem.getAttributes().getAttribute(HTML.Attribute.NAME);
	    	  if (name==null) continue;
    	  	    	  
	    	  if (name.equals("captcha_sid"))
	    		  captchaSid = (String) elem.getAttributes().getAttribute(HTML.Attribute.VALUE);
	    	  else if (name.equals("lg_h"))
	    	  {
	    		  lg_h=(String) elem.getAttributes().getAttribute(HTML.Attribute.VALUE);
	    	  }
	      }
	    }
	    
		System.out.println("Captcha image:\n"+ captchaURL+"\n Input captcha:\n");
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        captchaKey= input.readLine();
        
		HttpPost post = new HttpPost("https://login.vk.com/?act=login&soft=1"+
				"&q=1"+
				"&ip_h="+ip_h+
				"&lg_h="+lg_h+
				"&_origin=https://oauth.vk.com"+
				"&to="+to+
				"&expire=0"+
				"&email="+email+
				"&pass="+pass+
				"&captcha_sid="+captchaSid+
				"&captcha_key="+captchaKey);
		
		response = httpClient.execute(post);
		
		//post.abort();
	}
	
	private HTMLDocument streamToHtml (InputStream stream) throws IOException, BadLocationException
	{
		HTMLEditorKit kit = new HTMLEditorKit(); 
		HTMLDocument doc = (HTMLDocument) kit.createDefaultDocument(); 
		doc.putProperty("IgnoreCharsetDirective", Boolean.TRUE);
		Reader HTMLReader = new InputStreamReader(stream); 
		kit.read(HTMLReader, doc, 0); 
		return doc;
	}
}
