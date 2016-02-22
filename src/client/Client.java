package client;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.ElementIterator;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
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

public class Client 
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
		
	public Client(String email, String pass) throws Exception
	{
		buildClient();
		 
		this.email=email;
		this.pass=pass;
		connect();
	}	
	
	private void buildClient()
	{
		RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
		CookieStore cookieStore = new BasicCookieStore();
		HttpClientContext context = HttpClientContext.create();
		context.setCookieStore(cookieStore);
		httpClient = HttpClients.custom().setDefaultRequestConfig(globalConfig).setDefaultCookieStore(cookieStore).build();	
	}

	public void connect() throws Exception
	{	
		authorize();		
		login();
		
		while(!response.containsHeader("location"))
			handleCaptcha();		
			
		getToken();
		
		UserWorker uw = new UserWorker(this);
		me = uw.getMe();		
	}

	private void authorize() throws ClientProtocolException, IOException, BadLocationException
	{
		String post = "https://oauth.vk.com/authorize?" +
				"client_id="+ID+
				"&redirect_uri="+redirectUri+
				"&display="+display+
				"&scope="+scope+	
				"&response_type="+responseType;		

		HTMLDocument doc = stringToHtml(postQuery(post));
		
	    ElementIterator it = new ElementIterator(doc); 
	    Element elem; 
	    	    	    	    
	    while((elem=it.next()) != null)
	    { 
	      if(elem.getName().equals("input"))
	      { 
	    	  String name = (String) elem.getAttributes().getAttribute(HTML.Attribute.NAME);
	    	  if (name==null) continue;
	    	  
	    	  if (name.equals("ip_h"))
	    		 ip_h = (String) elem.getAttributes().getAttribute(HTML.Attribute.VALUE);	  		  
	    	  else if (name.equals("lg_h"))
	    		 lg_h=(String) elem.getAttributes().getAttribute(HTML.Attribute.VALUE);
	    	  else if (name.equals("to"))
	    		 to=(String) elem.getAttributes().getAttribute(HTML.Attribute.VALUE);
	      } 
	    }
	}
	
	HTMLDocument stringToHtml (String string) throws IOException, BadLocationException
	{
		HTMLEditorKit kit = new HTMLEditorKit(); 
		HTMLDocument doc = (HTMLDocument) kit.createDefaultDocument(); 
		doc.putProperty("IgnoreCharsetDirective", Boolean.TRUE);
		Reader HTMLReader = new StringReader(string); 
		kit.read(HTMLReader, doc, 0); 
		return doc;
	}
	
	private void login() throws ClientProtocolException, IOException
	{
		String post = "https://login.vk.com/?act=login&soft=1"+
				"&q=1"+
				"&ip_h="+ip_h+
				"&lg_h="+lg_h+
				"&_origin=https://oauth.vk.com"+
				"&to="+to+
				"&expire=0"+
				"&email="+email+
				"&pass="+pass;
		postQuery(post);
		postQuery(response.getFirstHeader("location").getValue());
	}

	private void handleCaptcha() throws UnsupportedOperationException, IOException, BadLocationException
	{
	    getCaptcha();
	    
	    File file = new File("file.jpg");	    
	    downloadCaptcha(file);
	    	    
		System.out.println("Input captcha:");
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        captchaKey= input.readLine();
        
        sendCaptcha();
		
	    file.delete();
	}
	
	private void getCaptcha() throws UnsupportedOperationException, IOException, BadLocationException
	{
		HTMLDocument doc = stringToHtml(IOUtils.toString(response.getEntity().getContent(), "UTF-8"));
		
	    ElementIterator it = new ElementIterator(doc); 
	    Element elem; 
	   		    	    
	    while((elem=it.next()) != null)
	    { 
	      if(elem.getName().equals("img"))
	    	  captchaURL= (String) elem.getAttributes().getAttribute(HTML.Attribute.SRC);	
	      
	      else if (elem.getName().equals("input"))
	      {
	    	  String name = (String) elem.getAttributes().getAttribute(HTML.Attribute.NAME);
	    	  if (name==null) continue;
    	  	    	  
	    	  if (name.equals("captcha_sid"))
	    		  captchaSid = (String) elem.getAttributes().getAttribute(HTML.Attribute.VALUE);
	    	  else if (name.equals("lg_h"))
	    	  {
	    		  lg_h=(String) elem.getAttributes().getAttribute(HTML.Attribute.VALUE);
	    	  }
	      }
	    }
	}
	
	private void downloadCaptcha(File file) throws MalformedURLException, IOException
	{
	    FileUtils.copyURLToFile(new URL(captchaURL), file);
	    
	    Desktop desktop = null;
	    if (Desktop.isDesktopSupported()) 
	    {
	        desktop = Desktop.getDesktop();
	        desktop.open(file);
	    }
	}
	
	private void sendCaptcha() throws ClientProtocolException, IOException
	{
		String post = "https://login.vk.com/?act=login&soft=1"+
				"&q=1"+
				"&ip_h="+ip_h+
				"&lg_h="+lg_h+
				"&_origin=https://oauth.vk.com"+
				"&to="+to+
				"&expire=0"+
				"&email="+email+
				"&pass="+pass+
				"&captcha_sid="+captchaSid+
				"&captcha_key="+captchaKey;
		
		postQuery(post);
		postQuery(response.getFirstHeader("location").getValue());
	}
	
	private void getToken() throws ClientProtocolException, IOException
	{
		postQuery(response.getFirstHeader("location").getValue());

		String headerLocation = response.getFirstHeader("location").getValue();
		token = headerLocation.split("#")[1].split("&")[0].split("=")[1];	
	}
	
	/**
	 * Sends the VKApi command to execute. Automatically adds address, API version and token to the end of the command.
	 * @param command
	 * @return Response's String representation.
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String executeCommand(String command) throws ClientProtocolException, IOException
	{
		command = "https://api.vk.com/method/" + command;
		command+="&v=5.45";
		command+="&access_token="+token;
		
		return postQuery(command);
	}
	
	/**
	 * Sends the POST query and writes a server response to the class member.
	 * @param command
	 * @return Response's String representation.
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	private String postQuery(String query) throws ClientProtocolException, IOException
	{
		HttpPost post = new HttpPost(query);
		
		response = httpClient.execute(post);		
		String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
		post.reset();
		
		return str;
	}
}
