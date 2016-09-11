package api.client;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;

import javax.swing.text.Element;
import javax.swing.text.ElementIterator;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONException;
import org.json.JSONObject;

import api.exceptions.VKException;
import api.user.User;
import api.user.UserWorker;
import util.sig4j.signal.*;

/**
 * Performs all network operations.
 */
public class Client implements Serializable
{
	private static final long serialVersionUID = 5840493055914657790L;
	
	public transient CloseableHttpClient httpClient;
	public String token="";
	public User me;
	
	public final transient Signal1<String> onCaptchaNeeded = new Signal1<>();
	public final transient Signal0 onInvalidData = new Signal0();
	public final transient Signal1<String> onSuspectLogin = new Signal1<>();
	public final transient Signal0 onSuccess = new Signal0();
	
	public final void receiveCaptcha(String captcha)
	{
		this.captchaKey = captcha;
	}
	
	public final void receiveData(String email, String pass)
	{
		this.login = email;
		this.pass = pass;
	}
	
	public final void receivePhoneConfirmed()
	{
		this.phoneConfirmed = true;
	}
	
	QueryScheduler scheduler;
	
	final String apiVersion = "5.53";
	
	final String ID = "4977827";
	final String scope="friends,photos,audio,video,docs,status,wall,messages,offline";
	final String redirectUri = "https://oauth.vk.com/blank.html";
	final String display = "popup";
	final String responseType = "token";
	String login="";
	String pass="";

	String ip_h="";
	String lg_h="";
	String to="";
	
	String captchaSid="";
	String captchaKey="";
	String captchaURL="";
	
	boolean phoneConfirmed = false;
		
	transient CloseableHttpResponse response;
	String stringResponse;
		
	public Client()
	{
		buildClient();

		scheduler = new QueryScheduler(1000, 3);
	}	
	
	private void buildClient()
	{
		RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
		CookieStore cookieStore = new BasicCookieStore();
		HttpClientContext context = HttpClientContext.create();
		context.setCookieStore(cookieStore);
		httpClient = HttpClients.custom().setDefaultRequestConfig(globalConfig).setDefaultCookieStore(cookieStore).build();	
	}

	public void connect(String email, String pass) throws Exception
	{	
		this.login = email;
		this.pass = pass;
		
		authorize();		
		login();
		
		while(!response.containsHeader("location") && token=="")
			handleProblem();		
			
		if (token=="")
			getToken();
		
		UserWorker uw = new UserWorker(this);
		me = uw.getMe();	
		
		pass = "";
		
		onSuccess.emit();
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
		
		ip_h = getAttributeOfElement(doc, "input", HTML.Attribute.NAME, "ip_h", HTML.Attribute.VALUE);
		lg_h = getAttributeOfElement(doc, "input", HTML.Attribute.NAME, "lg_h", HTML.Attribute.VALUE);
	    to = getAttributeOfElement(doc, "input", HTML.Attribute.NAME, "to", HTML.Attribute.VALUE);
	}
	
	private String getAttributeOfElement (HTMLDocument doc, String elementName, HTML.Attribute knownAttribute, String knownValue, HTML.Attribute desiredAttribute)
	{
	    ElementIterator it = new ElementIterator(doc); 
	    Element elem; 

	    while((elem=it.next()) != null)
	    { 
	    	if (elem.getName().equals(elementName))
	        {
	    	  String name = (String) elem.getAttributes().getAttribute(knownAttribute);
	    	  if (name==null) continue;
    	  	    	  
	    	  if (name.equals(knownValue))
	    		  return  (String) elem.getAttributes().getAttribute(desiredAttribute);
	        }
	    }	    
	    return "";
	}
	
	private String getAttributeOfElement (HTMLDocument doc, String elementName, HTML.Attribute desiredAttribute)
	{
	    ElementIterator it = new ElementIterator(doc); 
	    Element elem; 
	   		    	    
	    while((elem=it.next()) != null)
	    { 
	      if(elem.getName().equals(elementName))
	    	  return (String) elem.getAttributes().getAttribute(desiredAttribute);	
	    }
	    return "";
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

		if (hasAttributeValue(doc, "input", HTML.Attribute.NAME, "captcha_sid"))
  		  handleCaptcha(doc);
		else if (hasAttributeValue(doc, "div", HTML.Attribute.CLASS, "oauth_access_header"))
  		  handleConfirmApplicationRights(doc);
		else handleInvalidData(doc);
	}
	
	private boolean hasAttributeValue (HTMLDocument doc, String elementName, HTML.Attribute attribute, String value)
	{
	    ElementIterator it = new ElementIterator(doc); 
	    Element elem;       
	   		    	    
	    while((elem=it.next()) != null)
	    {       
	      if (elem.getName().equals(elementName))
	      {
	    	  String name = (String) elem.getAttributes().getAttribute(attribute);
	    	  if (name==null) continue;
    	  	    	  
	    	  if (name.equals(value))
	    		  return true;
	      }
	    }
	    return false;
	}
	
	private void handleCaptcha(HTMLDocument doc) throws Exception
	{
	    getCaptcha(doc);
	    
	    File file = new File("file.jpg");	  

	    captchaKey = "";
	    onCaptchaNeeded.emit(captchaURL);
	    	    
	    while (captchaKey.equals(""))
	    {
	    	Thread.sleep(1);
	    }
		
        sendCaptcha();
		
	    file.delete();
	}
			
	private void getCaptcha(HTMLDocument doc) throws Exception
	{		
	    captchaURL = getAttributeOfElement(doc, "img", HTML.Attribute.CLASS, "captcha_img", HTML.Attribute.SRC);
	    captchaSid = getAttributeOfElement(doc, "input", HTML.Attribute.NAME, "captcha_sid", HTML.Attribute.VALUE);
	    lg_h = getAttributeOfElement(doc, "input", HTML.Attribute.NAME, "lg_h", HTML.Attribute.VALUE);
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
		lg_h = getAttributeOfElement(doc, "input", HTML.Attribute.NAME, "lg_h", HTML.Attribute.VALUE);
		
		login = "";
		pass = "";
		onInvalidData.emit();
		
		while (login.equals("") || pass.equals(""))
	    {
			Thread.sleep(1);
	    }

        login();
	}
	
	/**
	 * If user logins first time and has to add the application.
	 */
	private void handleConfirmApplicationRights(HTMLDocument doc) throws Exception
	{
		String name = getAttributeOfElement(doc, "form", HTML.Attribute.ACTION);		
		postQuery(name);
	  
	    getTokenFirstTime();
	}
	
	private void getTokenFirstTime() throws Exception
	{
		  String headerLocation = response.getFirstHeader("location").getValue();
		  token = headerLocation.split("#")[1].split("&")[0].split("=")[1];	
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
		command+="&v=" + apiVersion;
		command+="&access_token="+token;
		
		str = postQuery(command);
		JSONObject obj = null;

		try 
		{
			obj = new JSONObject(str);
		} 
		catch (JSONException ex)
		{
			return str;
		}
		
		if (obj.has("error"))
		{
			obj = obj.getJSONObject("error");			
			int code = obj.getInt("error_code");
			
			if (code == 17)
			{
				handleSuspectLogin(obj.getString("redirect_uri"));
				return executeCommand(command);
			}			
			else throw new VKException(obj.getString("error_msg"), code);
		}				
		return str;
	}
	
	/**
	 * If user logins from unusual place and phone confirmation is needed.
	 */
	private void handleSuspectLogin(String URL) throws Exception
	{
	    onSuspectLogin.emit(URL);
	    
	    while (!phoneConfirmed)
	    {
	    	Thread.sleep(1);
	    }
	}
	
	/**
	 * Sends the POST query and writes a server response to the class member.
	 * @param command
	 * @return Response's String representation.
	 */
	private String postQuery(String query) throws Exception
	{	
		while (!scheduler.canPostQuery());
		
		HttpPost post = new HttpPost(query);
		
		response = httpClient.execute(post);		
		stringResponse = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
		post.reset();

		return stringResponse;
	}

	private void writeObject(ObjectOutputStream oos) throws IOException 
	{
		oos.defaultWriteObject();	
	}

	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException 
	{
		ois.defaultReadObject();
		
		buildClient(); //TODO: try comment
	}
}
