package application;
	
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;


public class Main extends Application 
{
	private static ResourceBundle bundle;
	
	public void start(Stage primaryStage) 
	{
		try 
		{
			bundle = loadLocale (Locale.getDefault(), "resources.locale.MainWindow.messages");
			
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("MainWindow.fxml"), bundle);
			Scene scene = new Scene(root);
			scene.setRoot(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setMinWidth(1100);
			primaryStage.setMinHeight(600);
			primaryStage.show();
			test();
		} catch(Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public void test() throws Exception
	{

	}
	
	public static void main(String[] args) 
	{
		launch(args);
	}
	
	public static ResourceBundle loadLocale(Locale locale, String resourcePath)
	{
		Locale.setDefault(locale);
		return ResourceBundle.getBundle(resourcePath, Locale.getDefault());
	}
}
