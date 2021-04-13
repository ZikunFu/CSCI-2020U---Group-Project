package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.*;
import java.net.*;
import java.awt.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.util.Duration;
import org.w3c.dom.*;
import java.io.File;
import javafx.scene.text.Text;

public class Main extends Application {
    private Canvas canvas;
    private Socket socket = null;
    private BufferedReader in = null;
    private PrintWriter networkOut = null;
    private BufferedReader networkIn = null;

    //String LOCAL_PATH ="local_Shared/";
    //String SERVER_IP = "127.0.0.1";
    //int SERVER_PORT = 16789;
    //ListView<String> list;
    public  static String SERVER_ADDRESS = "localhost";
    public  static int    SERVER_PORT = 16789;

    @Override
    public void start(Stage primaryStage) throws Exception{
        //socket
        /*
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: "+SERVER_ADDRESS);
        } catch (IOException e) {
            System.err.println("IOEXception while connecting to server: "+SERVER_ADDRESS);
        }
        if (socket == null) {
            System.err.println("socket is null");
        }
        try {
            networkOut = new PrintWriter(socket.getOutputStream(), true);
            networkIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.err.println("IOEXception while opening a read/write connection");
        }

         */


        //UI
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Game");

        GridPane myGrid = new GridPane();
        myGrid.setAlignment(Pos.CENTER);
        myGrid.setHgap(10);
        myGrid.setVgap(10);
        myGrid.setPadding(new Insets(25, 25, 25, 25));

        Label lb1 = new Label("Account:");
        TextField tf1 = new TextField();
        Label lb2 = new Label("Password:");
        TextField tf2 = new TextField();

        myGrid.add(lb1, 0,2);
        myGrid.add(tf1, 1,2);
        myGrid.add(lb2, 0,3);
        myGrid.add(tf2, 1,3);

        final Label label = new Label();

        Button login = new Button("Login");
        myGrid.add(login, 1, 4);
        Button register = new Button("Register");
        myGrid.add(register, 1, 5);

        myGrid.add(label, 1, 6);
        //label.setText("222");

        //right half
        Group root1 = new Group();

        //Scene scene = new Scene(root1, 300, 500);

        canvas = new Canvas();
        canvas.widthProperty().bind(primaryStage.widthProperty());
        canvas.heightProperty().bind(primaryStage.heightProperty());
        root1.getChildren().add(canvas);

        //Group root2 = new Group();
        //canvas = new Canvas();
        //canvas.widthProperty().bind(primaryStage.widthProperty());
        //canvas.heightProperty().bind(primaryStage.heightProperty());
        //root2.getChildren().add(canvas);

        /*
        VBox right = new VBox(30);
        right.getChildren().add(root1);

        VBox vb = new VBox(30);
        vb.getChildren().addAll(myGrid, right);
         */
        //vb.setAlignment(Pos.CENTER);

        //Hhox
        /*
        VBox vb1 = new VBox(30);
        vb1.getChildren().addAll(myGrid, vb);
        vb1.setSpacing(10);
         */
        //vb1.setAlignment(Pos.CENTER);


        //show
        Scene scene = new Scene(myGrid, 500, 500);

        primaryStage.setScene(scene);
        primaryStage.show();
        //draw(root1);
        //drawAnimation(root2);


        login.setOnAction(actionEvent -> {

            String message = null;

            networkOut.println("Login" + tf1.getText() + tf2.getText());
            try {
                message = networkIn.readLine();
            } catch (IOException e) {
                System.err.println("Error reading response to login.");
            }

            if(message == "correct"){
                label.setText("Login Successfully");
            }
            else if(message == "invalidAccount"){
                label.setText("Invalid Account");
            }
            else if(message == "invalidPassword"){
                label.setText("Invalid Password");
            }
        });

        register.setOnAction(actionEvent -> {

            String message = null;

            networkOut.println("Register" + tf1.getText() + tf2.getText());
            try {
                message = networkIn.readLine();
            } catch (IOException e) {
                System.err.println("Error reading response to Register.");
            }

            if(message == "invalidAccount"){
                label.setText("Invalid Account");
            }
            else if(message == "correct"){
                label.setText("Register Successfully");
            }

        });
    }

    private void draw(Group root) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

//        Drawing 3 lines(Y)
        gc.setStroke(javafx.scene.paint.Color.RED);
        gc.strokeLine(100,100,60,60);

        gc.setStroke(javafx.scene.paint.Color.GREEN);
        gc.strokeLine(100,100,140,60);

        gc.setStroke(javafx.scene.paint.Color.BLUE);
        gc.strokeLine(100,100,100,170);

//        Drawing 3 lines(Y)
        gc.setStroke(javafx.scene.paint.Color.RED);
        gc.strokeLine(200,100,160,60);

        gc.setStroke(javafx.scene.paint.Color.GREEN);
        gc.strokeLine(200,100,240,60);

        gc.setStroke(Color.BLUE);
        gc.strokeLine(200,100,200,170);
    }

    private int frameWidth = 32;
    private int frameHeight = 36;
    private int numFrameswidth = 3;
    private int numFramesheight = 8;
    private int sourceHeightOffset = 0;
    private int sourceWidthOffset = 0;
    private int frameIndexwidth = 0;
    //    private int frameIndexheight = 0;
    private int widthIndex = 0;
    private int heightIndex = 0;

    private void drawAnimation(Group root) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
//loading image sprite using relative path
        Image image = new Image(getClass().getClassLoader().getResource("ducks.png").toString());

        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(1333.3333), new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent actionEvent) {
//                background rect fpr the characters
                gc.setFill(Color.BLACK);
                gc.fillRect(50, 50, frameWidth, frameHeight);


                widthIndex = 9 + frameIndexwidth;
//                calculating the offset height based on the frameIndex
                sourceWidthOffset = frameWidth*widthIndex;

                gc.drawImage(image, sourceWidthOffset, sourceHeightOffset, frameWidth, frameHeight, 50, 50, frameWidth, frameHeight);
//                we want to vary frameIndex from 9 to 11 (not included) using the %
                frameIndexwidth = (frameIndexwidth+1) % numFrameswidth;
//                we want to vary frameIndex from 0 to 2 (not included) using the %
                if(frameIndexwidth == 0){
                    heightIndex = (heightIndex + 1) % numFramesheight;
                }
//                calculating the offset height based on the frameIndex
                sourceHeightOffset = frameHeight*heightIndex;

            }
        }));

//      Starting the timeline
        timeline.playFromStart();



    }

    public static void main(String[] args) {
        launch(args);
    }
}
