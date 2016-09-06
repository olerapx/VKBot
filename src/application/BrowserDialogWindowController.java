package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import util.sig4j.signal.Signal1;

public class BrowserDialogWindowController implements Initializable
{
	public final Signal1<Boolean> sendBrowserResult = new Signal1<>();
	
	final static String resourcePath = "resources.locale.BrowserDialogWindow.messages";
	final static String fxmlPath = "BrowserDialogWindow.fxml";
	
	@FXML private ResourceBundle resources;
	@FXML private AnchorPane root;	
		
	String URL;
	
	public void initialize(URL location, ResourceBundle resources) 
	{
		this.resources = resources;		
	}

	public void setURL (String URL)
	{
		this.URL = URL;
	}
}
