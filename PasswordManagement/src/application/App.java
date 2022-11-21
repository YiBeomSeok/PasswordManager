package application;
	
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class App extends Application {
	FXMLLoader loginLoader;
	Parent root;

	@Override
	public void start(Stage primaryStage) throws IOException {
		loginLoader = new FXMLLoader(getClass().getResource("MainView.fxml"));
		
		root = loginLoader.load();
		primaryStage.setScene(new Scene(root));
		
		primaryStage.setTitle("Password Manager");
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
