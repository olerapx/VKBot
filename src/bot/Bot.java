package bot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.script.ScriptException;

import org.apache.commons.io.FilenameUtils;

import api.client.Client;
import api.user.User;
import api.worker.WorkerInterface;
import application.Main;
import javafx.beans.property.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import scripts.JythonRunner;
import scripts.NashornRunner;
import scripts.ScriptRunner;
import util.Date;
import util.FileSystem;

/**
 * Represents a single bot.
 */
public class Bot implements Serializable
{	
	private static final long serialVersionUID = -4719943341841483336L;
	
	final public static String resourcePath = "resources.locale.Bot.messages";
	public static ResourceBundle resources = Main.loadLocale(Locale.getDefault(), resourcePath);
	
	Client client;
	WorkerInterface workerInterface;
	User user;
	ScriptRunner runner;
	File scriptFile;
	
    boolean hasProblem = false;
    Problem problemType = Problem.UNKNOWN;
    
    //TODO: avatar property
	
	transient SimpleStringProperty firstNameProperty;
	transient SimpleStringProperty lastNameProperty;
	transient SimpleIntegerProperty IDProperty;
	
	transient SimpleStringProperty cityWithAgeProperty;
	
	transient SimpleStringProperty onlineProperty;

	transient SimpleStringProperty statusProperty;
	
	transient SimpleObjectProperty<Image> avatarProperty;
	
	public SimpleStringProperty getFirstNameProperty() {return firstNameProperty;}

	public SimpleStringProperty getLastNameProperty() {return lastNameProperty;}

	public SimpleIntegerProperty getIDProperty() {return IDProperty;}
	
	public SimpleStringProperty getCityWithAgeProperty() { return cityWithAgeProperty;}
	
	public SimpleStringProperty getOnlineProperty()	{return onlineProperty;}
	
	public SimpleStringProperty getStatusProperty() {return statusProperty;}    
	
	public SimpleObjectProperty<Image> getAvatarProperty() {return this.avatarProperty;}
	
		
	public String getFirstName() {return firstNameProperty.get();}
	
	public String getLastName() {return lastNameProperty.get();}
	
	public int getID() {return IDProperty.get();}
	
	public String getCityWithAge() {return cityWithAgeProperty.get();}
	
	public String getOnline() {return onlineProperty.get();}
	
	public String getStatus() {return statusProperty.get();}
	
	public Image getAvatar() {return avatarProperty.get();}
        
	
	public Bot(Client client)
	{
		this.client = client;
		
		this.workerInterface = new WorkerInterface (client);
		this.user = client.me;
		
		initProperties();
	}
	
	private void initProperties()
	{
		getUserProperties();
		
		avatarProperty = new SimpleObjectProperty<Image> (new Image (user.previewPhotoURL(), false));
		statusProperty = new SimpleStringProperty(resources.getString("Bot.state.idle"));
	}
	
	private void getUserProperties()
	{	
		firstNameProperty = new SimpleStringProperty(user.firstName());
		lastNameProperty = new SimpleStringProperty (user.lastName());
		IDProperty = new SimpleIntegerProperty (user.ID());
		
		long userAge = user.ageInYears();
		
		String city = user.cityName()+((userAge==-1L)?"":", ");
				
		cityWithAgeProperty = new SimpleStringProperty(city + Date.formatYears(userAge));
			
		if (user.online() == User.Online.OFFLINE)			
			onlineProperty = new SimpleStringProperty(Date.formatTimestamp(user.lastOnline()));
		else onlineProperty = new SimpleStringProperty(resources.getString("Bot.online.online"));
	}
	
	public boolean isExist()
	{
		return FileSystem.isBotExist(this);
	}
	
	public void setScript(File script)
	{
		stop();
		
		this.scriptFile = script;
		
		String ext = FilenameUtils.getExtension(script.getAbsolutePath());

		switch(ext)
		{
		case "py":
			this.runner = new JythonRunner (workerInterface); 
			break;
		case "js":
			this.runner = new NashornRunner (workerInterface);
			break;
		default:
				throw new IllegalArgumentException(ext);
		}
	}
		
	public void start() throws FileNotFoundException, ScriptException
	{
		if (runner == null || scriptFile == null) return;
		
		runner.execFile(scriptFile);
	}
	
	public void stop()
	{
		if (runner == null || scriptFile == null) return;
		
		runner.stop();
	}
	
	public User getUser () {return client.me;}
	
	/**
	 * Retrieves an up-to-date represented user information.
	 */
	public void updateUser()
	{
		try
		{
			this.user = workerInterface.userWorker().getMe();
			getUserProperties();
			
			avatarProperty = new SimpleObjectProperty<Image> (new Image (user.previewPhotoURL(), true));
		} 
		catch (Exception e) 
		{
			
		}
	}
	
	private void writeObject(ObjectOutputStream oos) throws IOException 
	{
		oos.defaultWriteObject();
		
		oos.writeObject(statusProperty.get());
		ImageIO.write(SwingFXUtils.fromFXImage(avatarProperty.getValue(), null), "png", oos);
	}

	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException 
	{
		ois.defaultReadObject();
		
		statusProperty = new SimpleStringProperty((String) ois.readObject());
		avatarProperty = new SimpleObjectProperty<Image> (SwingFXUtils.toFXImage(ImageIO.read(ois), null));
		getUserProperties();	
	}
}
