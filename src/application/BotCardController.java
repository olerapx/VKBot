package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;

public class BotCardController  implements Initializable
{
	final static String resourcePath = "resources.locale.BotCard.messages";
	final static String fxmlPath = "BotCard.fxml";
	
	@FXML private ResourceBundle resources;
	@FXML private AnchorPane root;
	@FXML private Button buttonRemove;

	public void initialize(URL location, ResourceBundle resources) 
	{	
		this.resources = resources;
	}
	
	@FXML private void onAdd()
	{
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Adding a bot");
		alert.setContentText("Can't do it yet");

		alert.showAndWait();
	}
}
