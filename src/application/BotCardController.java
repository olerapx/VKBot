package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import util.Effects;

public class BotCardController implements Initializable
{
	final static String resourcePath = "resources.locale.BotCard.messages";
	final static String fxmlPath = "BotCard.fxml";
	
	@FXML private ResourceBundle resources;
	@FXML private AnchorPane root;
	
	@FXML private Button buttonToggle;
	@FXML private Button buttonLoad;
	@FXML private Button buttonSettings;
	@FXML private Button buttonInfo;
	@FXML private Button buttonRemove;

	public void initialize(URL location, ResourceBundle resources) 
	{	
		this.resources = resources;
	}
		
	@FXML private void onToggle()
	{
		toggleButtonActive (buttonToggle);
	}
	
	@FXML private void onLoad()
	{
		toggleButtonActive (buttonLoad);
	}

	@FXML private void onSettings()
	{
	}
	
	@FXML private void onInfo()
	{
	}
	
	@FXML private void onRemove()
	{

	}
	
	private void toggleButtonActive(Button button)
	{	
		if (button.getId()!= "button-active")
			setButtonActive(button);
		else
			setButtonInactive(button);
	}
	
	private void setButtonActive(Button button)
	{
		button.setId("button-active");
		button.setEffect(Effects.imageButtonActive);
		root.requestFocus();
	}
	
	private void setButtonInactive(Button button)
	{
		button.setId("button");
		button.setEffect(null);
	}
}
