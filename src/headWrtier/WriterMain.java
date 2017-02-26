package headWrtier;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WriterMain extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent pane = FXMLLoader.load(getClass().getResource("WriterMainFX.fxml"));
        primaryStage.setTitle("Write Header");
        primaryStage.setScene(new Scene(pane, 600, 400));
        primaryStage.show();
    }


    public static void main(String[] args) {launch(args);}
}