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
import javafx.scene.layout.FlowPane;

public class MainTabController implements Initializable
{
	final static String resourcePath = "resources.locale.MainTab.messages";
	final static String fxmlPath = "MainTab.fxml";
	
	@FXML private ResourceBundle resources;
	@FXML private AnchorPane root;
	@FXML private FlowPane flowPane;

	public void initialize(URL location, ResourceBundle resources) 
	{
		this.resources = resources;
		
		ResourceBundle bundle = Main.loadLocale (Locale.getDefault(), BotCardController.resourcePath);
				
		flowPane.prefWidthProperty().bind(root.widthProperty());
		flowPane.prefHeightProperty().bind(root.heightProperty());
		
		try 
		{
			flowPane.getChildren().add((Node) FXMLLoader.load(this.getClass().getResource(BotCardController.fxmlPath), bundle));
			flowPane.getChildren().add((Node) FXMLLoader.load(this.getClass().getResource(BotCardController.fxmlPath), bundle));
			flowPane.getChildren().add((Node) FXMLLoader.load(this.getClass().getResource(BotCardController.fxmlPath), bundle));
			flowPane.getChildren().add((Node) FXMLLoader.load(this.getClass().getResource(BotCardController.fxmlPath), bundle));
			flowPane.getChildren().add((Node) FXMLLoader.load(this.getClass().getResource(BotCardController.fxmlPath), bundle));
			flowPane.getChildren().add((Node) FXMLLoader.load(this.getClass().getResource(BotCardController.fxmlPath), bundle));
			flowPane.getChildren().add((Node) FXMLLoader.load(this.getClass().getResource(BotCardController.fxmlPath), bundle));
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}