package api.client;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;

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
import org.json.JSONObject;

import api.exceptions.VKException;
import api.user.User;
import api.user.UserWorker;

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
	String login="";
	String pass="";

	String ip_h="";
	String lg_h="";
	String to="";
	
	String captchaSid="";
	String captchaKey="";
	String captchaURL="";
		
	CloseableHttpResponse response;
	String stringResponse;
		
	public Client(String email, String pass) throws Exception
	{
		buildClient();
		 
		this.login=email;
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
		
		while(!response.containsHeader("location") && token=="")
			handleProblem();		
			
		if (token=="")
			getToken();
		
		UserWorker uw = new UserWorker(this);
		me = uw.getMe();		
	}

	private void authorize() throws Exception
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
	
	HTMLDocument stringToHtml (String string) throws Exception
	{
		HTMLEditorKit kit = new HTMLEditorKit(); 
		HTMLDocument doc = (HTMLDocument) kit.createDefaultDocument(); 
		doc.putProperty("IgnoreCharsetDirective", Boolean.TRUE);
		Reader HTMLReader = new StringReader(string); 
		kit.read(HTMLReader, doc, 0); 
		return doc;
	}
	
	private void login() throws Exception
	{
		String post = "https://login.vk.com/?act=login&soft=1"+
				"&q=1"+
				"&ip_h="+ip_h+
				"&lg_h="+lg_h+
				"&_origin=https://oauth.vk.com"+
				"&to="+to+
				"&expire=0"+
				"&email="+login+
				"&pass="+pass;

		postQuery(post);
		postQuery(response.getFirstHeader("location").getValue());
	}

	private void handleProblem() throws Exception
	{	    		
		HTMLDocument doc = stringToHtml(stringResponse);
		boolean errorFound = false;
						
	    ElementIterator it = new ElementIterator(doc); 
	    Element elem; 
	   		    	    
	    while((elem=it.next()) != null)
	    {       
	      if (elem.getName().equals("input"))
	      {
	    	  String name = (String) elem.getAttributes().getAttribute(HTML.Attribute.NAME);
	    	  if (name==null) continue;
    	  	    	  
	    	  if (name.equals("captcha_sid"))
	    	  {
	    		  errorFound = true;
	    		  handleCaptcha(doc);
	    	  }
	      }
	      else if (elem.getName().equals("div"))
	      {
	    	  String name = (String) elem.getAttributes().getAttribute(HTML.Attribute.CLASS);
    
	    	  if (name.equals("oauth_access_header"))
	    	  {
	    		  errorFound = true;
	    		  handleConfirmApplicationRights(doc);
	    	  }
	      }
	    }
	    if (!errorFound) handleInvalidData(doc);
	}
	
	private void handleCaptcha(HTMLDocument doc) throws Exception
	{
	    getCaptcha(doc);
	    
	    File file = new File("file.jpg");	  

	    downloadCaptcha(file);
	    	    
		System.out.println("Input captcha:");
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        captchaKey= input.readLine();
        
        sendCaptcha();
		
	    file.delete();
	}
	
	private void getCaptcha(HTMLDocument doc) throws Exception
	{		
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
	
	private void downloadCaptcha(File file) throws Exception
	{
	    FileUtils.copyURLToFile(new URL(captchaURL), file);
	    
	    Desktop desktop = null;
	    if (Desktop.isDesktopSupported()) 
	    {
	        desktop = Desktop.getDesktop();
	        desktop.open(file);
	    }
	}
	
	private void sendCaptcha() throws Exception
	{
		String post = "https://login.vk.com/?act=login&soft=1"+
				"&q=1"+
				"&ip_h="+ip_h+
				"&lg_h="+lg_h+
				"&_origin=https://oauth.vk.com"+
				"&to="+to+
				"&expire=0"+
				"&email="+login+
				"&pass="+pass+
				"&captcha_sid="+captchaSid+
				"&captcha_key="+captchaKey;
		
		postQuery(post);
		postQuery(response.getFirstHeader("location").getValue());
	}
	
	private void handleInvalidData(HTMLDocument doc) throws Exception
	{
	    ElementIterator it = new ElementIterator(doc); 
	    Element elem; 
	    	    	    
	    while((elem=it.next()) != null)
	    { 	      
	      if (elem.getName().equals("input"))
	      {
	    	  String name = (String) elem.getAttributes().getAttribute(HTML.Attribute.NAME);
	    	  if (name==null) continue;

	    	  if (name.equals("lg_h"))
	    	  {
	    		  lg_h=(String) elem.getAttributes().getAttribute(HTML.Attribute.VALUE);
	    	  }
	      }
	    }
	    
	    System.out.println("Wrong login or password:\nLogin: "+login);
	    
		System.out.println("Input login:");
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        login = input.readLine();
        
		System.out.println("Input pass:");
        input = new BufferedReader(new InputStreamReader(System.in));
        pass = input.readLine();

        login();
	}
	
	private void handleConfirmApplicationRights(HTMLDocument doc) throws Exception
	{
	    ElementIterator it = new ElementIterator(doc); 
	    Element elem; 
	    	   		    	    
	    while((elem=it.next()) != null)
	    {       
	      if (elem.getName().equals("form"))
	      {
	    	  String name = (String) elem.getAttributes().getAttribute(HTML.Attribute.ACTION);
	    	  postQuery(name);
	    	  
	  		  String headerLocation = response.getFirstHeader("location").getValue();
			  token = headerLocation.split("#")[1].split("&")[0].split("=")[1];	
	      }
	    }
	}
	
	private void getToken() throws Exception
	{
		postQuery(response.getFirstHeader("location").getValue());

		String headerLocation = response.getFirstHeader("location").getValue();
		token = headerLocation.split("#")[1].split("&")[0].split("=")[1];	
	}
	
	/**
	 * Sends the VKApi command to execute. Automatically adds address, API version and token to the end of the command.
	 * @param command
	 * @return Response's String representation.
	 */
	public String executeCommand(String command) throws Exception
	{	
		String str="";
		
		command = "https://api.vk.com/method/" + command;
		command+="&v=5.45";
		command+="&access_token="+token;
		
		str = postQuery(command);
		
		JSONObject obj = new JSONObject(str);
		if (obj.has("error"))
		{
			obj = obj.getJSONObject("error");
			
			int code = obj.getInt("error_code");
			
			if (code==17)
				handleSuspectLogin(obj.getString("redirect_uri"));
			
			else throw new VKException(obj.getString("error_msg"), code);
		}
							
		return str;
	}
	
	private void handleSuspectLogin(String URL) throws Exception
	{
		String str =postQuery(URL);
		
		HTMLDocument doc = stringToHtml(str);

		
	    ElementIterator it = new ElementIterator(doc); 
	    Element elem; 
	    	   		    	    
	    while((elem=it.next()) != null)
	    {       
	      if (elem.getName().equals("form"))
	      {
	    	  String name = (String) elem.getAttributes().getAttribute(HTML.Attribute.ACTION);

	    	  name = "https://vk.com"+name;
	  	      str = postQuery(name);
	      }
	    }
	    doc = stringToHtml(str);
	    it = new ElementIterator(doc); 
	    
	    String leftNumber="", rightNumber = "";
	   
	    while((elem=it.next()) != null)
	    {       
	      if (elem.getName().equals("div"))
	      {
	    	  String name = (String) elem.getAttributes().getAttribute(HTML.Attribute.CLASS);
	    	  if (name.equals("label ta_r"))
	    	  {
	              int count = elem.getElementCount();
	                for (int i = 0; i < count; i++) 
	                {
	                    Element child = elem.getElement(i);
	                    int startOffset = child.getStartOffset();
	                    int endOffset = child.getEndOffset();
	                    int length = endOffset - startOffset;
	   	    		 leftNumber =(doc.getText(startOffset, length));
	                }
	    	  }
	      }
	      else if (elem.getName().equals("span"))
	      {
	    		 System.out.println("bvavava");
	    	  String name = (String) elem.getAttributes().getAttribute(HTML.Attribute.CLASS);
	    	  if (name.equals("phone_postfix"))
	    	  {
	              int count = elem.getElementCount();
	                for (int i = 0; i < count; i++) 
	                {
	                    Element child = elem.getElement(i);
	                    int startOffset = child.getStartOffset();
	                    int endOffset = child.getEndOffset();
	                    int length = endOffset - startOffset;
	   	    		 rightNumber =(doc.getText(startOffset, length));
	                }
	    	  }
	      }	      
	    } 
	    
	    System.out.println("Need to confirm the phone number:\n"+leftNumber+"********"+rightNumber);
	  
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        String number = input.readLine();
        
        
		
	}
	
	/**
	 * Sends the POST query and writes a server response to the class member.
	 * @param command
	 * @return Response's String representation.
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	private String postQuery(String query) throws Exception
	{	
		HttpPost post = new HttpPost(query);
		
		response = httpClient.execute(post);		
		stringResponse = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
		post.reset();

		return stringResponse;
	}
}
