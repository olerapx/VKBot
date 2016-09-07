package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

public class BotTaskWindowController implements Initializable
{
	final static String resourcePath = "resources.locale.BotTaskWindow.messages";
	final static String fxmlPath = "BotTaskWindow.fxml";
	
	@FXML private ResourceBundle resources;
	@FXML private AnchorPane root;
		
	public void initialize(URL location, ResourceBundle resources) 
	{
		this.resources = resources;
	}
	
	public void initWindow()
	{
		
	}
}
