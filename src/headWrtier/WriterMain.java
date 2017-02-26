package headWrtier;
/**
 * ===================================================================================================================
 * File: Header Writer
 * Made by: Toby Zhang on Feb. 25, 2017
 * <p>
 * Purpose: Reads a given BSQ file, takes user input for sizes, outputs a new file with the header written
 * Dependencies: none noted.
 * <p>
 * Limitations: input must be a valid BSQ image file, must know exact details of sizes.
 * ====================================================================================================================
 */
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