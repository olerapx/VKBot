package application;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

public class BotTabController implements Initializable
{
	final static String resourcePath = "resources.locale.BotTab.messages";
	final static String fxmlPath = "BotTab.fxml";
	
	@FXML private ResourceBundle resources;
	@FXML private AnchorPane root;
	@FXML private AnchorPane botCardPane;

	public void initialize(URL location, ResourceBundle resources) 
	{
		this.resources = resources;
		
		ResourceBundle bundle = Main.loadLocale (Locale.getDefault(), BotCardController.resourcePath);
		try 
		{
			botCardPane.getChildren().add((Node) FXMLLoader.load(this.getClass().getResource(BotCardController.fxmlPath), bundle));				
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	

}
