package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class BotCardController  implements Initializable
{
	final String resourcePath = "resources.locale.BotCard.messages";
	final String fxmlPath = "BotCard.fxml";
	
	@FXML private ResourceBundle resources;
	@FXML private AnchorPane root;
	@FXML private Button buttonRemove;

	public void initialize(URL location, ResourceBundle resources) 
	{
		
		this.resources = resources;
	}
}
