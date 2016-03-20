package application;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;

public class MainWindowController implements Initializable
{
	final String resourcePath = "resources.locale.MainWindow.messages";
	final String fxmlPath = "MainWindow.fxml";
	
	@FXML private AnchorPane root;
	@FXML private ResourceBundle resources;
	@FXML private MenuItem menuFileClose;
	@FXML private RadioMenuItem menuLangEn;
	@FXML private RadioMenuItem menuLangRu;
	@FXML private TabPane tabPane;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		resources = arg1;
		
		menuLangEn.setOnAction(new LangChangeHandler());
		menuLangRu.setOnAction(new LangChangeHandler());
		
		tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.SELECTED_TAB);
		addMainTab();
		addBotTab("Boo");
	}
	
	private void addMainTab()
	{
		try 
		{
			AnchorPane pane = new AnchorPane();
			pane.getChildren().add((Node) FXMLLoader.load(this.getClass().getResource("MainTab.fxml"), resources));
			tabPane.getTabs().get(0).setContent(pane);
			tabPane.getTabs().get(0).setOnClosed(new TabCloseHandler());
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	private void addBotTab(String name)
	{
		try 
		{
			AnchorPane pane = new AnchorPane();
			pane.getChildren().add((Node) FXMLLoader.load(this.getClass().getResource("BotTab.fxml"), resources));
			Tab tab = new Tab();
			tab.setContent(pane);
			tab.setText("Boo");
			tab.setClosable(true);
			tab.setOnClosed(new TabCloseHandler());
			tabPane.getTabs().add(tab);				
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	@FXML private void onMenuFileClose()
	{
        Platform.exit();
        System.exit(0);
	}
	
	private void reload() throws IOException
	{
		Scene scene = root.getScene();
		scene.setRoot(FXMLLoader.load(getClass().getResource(fxmlPath), resources));
	}
	
	private class LangChangeHandler implements EventHandler<ActionEvent>
	{
	    public void handle(ActionEvent evt) 
	    {
	        if (evt.getSource().equals(menuLangEn)) 
	        {
	        	resources = Main.loadLocale(new Locale("en", "US"), resourcePath);
	        } else if (evt.getSource().equals(menuLangRu)) 
	        {
	        	resources = Main.loadLocale(new Locale("ru", "RU"), resourcePath);
	        }	       
	        try 
	        {
				reload();
			} catch (IOException e) 
	        {
				e.printStackTrace();
			}
	    }
	}
	
	private class TabCloseHandler implements EventHandler<Event>
	{

		public void handle(Event arg0) 
		{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("YOU, SCUM");
			alert.setContentText("YOU CLOSED THE TAB: " + ((Tab)arg0.getSource()).getText()+ ", DIDN'T YOU?");

			alert.showAndWait();
		}
	}
}
