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
	final String resourcePath = "resources.locale.BotTab.messages";
	final String botCardResourcePath = "resources.locale.BotCard.messages";
	final String fxmlPath = "BotTab.fxml";
	
	@FXML private ResourceBundle resources;
	@FXML private AnchorPane root;
	@FXML private AnchorPane botCardPane;

	public void initialize(URL location, ResourceBundle resources) 
	{
		this.resources = resources;
		
		ResourceBundle bundle = Main.loadLocale (Locale.getDefault(), botCardResourcePath);
		try 
		{
			botCardPane.getChildren().add((Node) FXMLLoader.load(this.getClass().getResource("BotCard.fxml"), bundle));
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	

}
