package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.*;
import java.net.*;
import java.awt.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

public class Main extends Application {
    private Socket socket = null;
    private BufferedReader in = null;
    private PrintWriter networkOut = null;
    private BufferedReader networkIn = null;

    String LOCAL_PATH ="local_Shared/";
    String SERVER_IP = "127.0.0.1";
    int SERVER_PORT = 16789;
    //ListView<String> list;

    @Override
    public void start(Stage primaryStage) throws Exception{

        primaryStage.setTitle("Game");

        GridPane myGrid = new GridPane();
        myGrid.setAlignment(Pos.CENTER);
        myGrid.setHgap(10);
        myGrid.setVgap(10);
        myGrid.setPadding(new Insets(25, 25, 25, 25));

        Label lb1 = new Label("Account:");
        TextField tf1 = new TextField();
        Label lb2 = new Label("Password");
        TextField tf2 = new TextField();

        myGrid.add(lb1, 0,2);
        myGrid.add(tf1, 1,2);
        myGrid.add(lb2, 0,3);
        myGrid.add(tf2, 1,3);

        final Label label = new Label();

        Button login = new Button("Login");
        myGrid.add(login, 1, 4);
        Button register = new Button("Register");
        myGrid.add(register, 0, 5);
        Button forget = new Button("Forget Password");
        myGrid.add(forget, 0, 6);

        myGrid.add(label, 1, 5);

        login.setOnAction(actionEvent -> {
            //String input = null;
            String message = null;
            int errorCode = 0;
            //label.setText(tf1.getText() + tf2.getText());
            networkOut.println("UID " + tf1.getText());
            try {
                message = networkIn.readLine();
            } catch (IOException e) {
                System.err.println("Error reading response to UID.");
            }

            networkOut.println("PWD "+tf2.getText());
            try {
                message = networkIn.readLine();
            } catch (IOException e) {
                System.err.println("Error reading response to UID.");
            }

            /*errorCode = getErrorCode(message);
            if (errorCode != 200) {
                System.out.println("Login unsuccessful: " + getErrorMessage(message));
                return false;
            }
            return true;*/
        });



        Scene scene = new Scene(myGrid, 550, 657);

        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
