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
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.util.Duration;
import javafx.scene.control.Label;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class Main extends Application {
    private Canvas canvas;
    //private double screenWidth = 100;
    //private double screenHeight = 600;

    private Socket socket = null;
    private BufferedReader in = null;
    private PrintWriter networkOut = null;
    private BufferedReader networkIn = null;

    ListView<String> list;
    public  static String SERVER_ADDRESS = "localhost";
    public  static int    SERVER_PORT = 16789;
    @Override
    public void start(Stage primaryStage) throws Exception{
        //Thread for establishing server connection
        new Thread(()-> {
            while (true) {
                try {
                    socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                    if (socket != null) {
                        networkOut = new PrintWriter(socket.getOutputStream(), true);
                        networkIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        break;
                    }
                    System.out.println("trying to connect...");
                } catch (UnknownHostException e) {
                    System.err.println("Unknown host: "+SERVER_ADDRESS);
                } catch (IOException e) {
                    System.err.println("IOEXception while connecting to server: "+SERVER_ADDRESS);
                }

            } }).start();

        //login
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
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

        //LOGO
        Group root = new Group();
        Scene scene = new Scene(root, 780, 450);

        //        Create Canvas object and add it into the scene
        canvas = new Canvas();
        canvas.widthProperty().bind(primaryStage.widthProperty());
        canvas.heightProperty().bind(primaryStage.heightProperty());
        root.getChildren().addAll(myGrid, canvas);

        primaryStage.setTitle("Graphics - Hello World");
        primaryStage.setScene(scene);
        primaryStage.show();



        //Creating mainScene
        BorderPane border1 = new BorderPane();
        Button profile = new Button("Profile");
        Button bag = new Button("Bag");
        Button battle = new Button("Battle");
        list = new ListView<>();
        VBox vbox = new VBox();
        vbox.getChildren().addAll(profile,bag,battle);
        border1.setLeft(vbox);
        border1.setRight(list);
        Scene main_scene = new Scene(border1,600,400);
        //
        BorderPane border2 = new BorderPane();
        Scene prebattle_scene = new Scene(border2,600,400);



//        drawing graphics - shapes and image
        draw(root);

//        draw an animation
        drawAnimation(root);


        //Login
        login.setOnAction(actionEvent -> {

            String message = null;

            networkOut.println("Login " + tf1.getText() + " " + tf2.getText());
            try {
                message = networkIn.readLine();
            } catch (IOException e) {
                System.err.println("Error reading response to login.");
            }

            if(message.equals("correct")){
                label.setText("Login Successfully");
                primaryStage.setScene(main_scene);
            }
            else if(message.equals("invalidAccount")){
                label.setText("Invalid Account");
            }
            else if(message.equals("invalidPassword")){
                label.setText("Invalid Password");
            }
        });

        //Register
        String account;
        register.setOnAction(actionEvent -> {

            String message = null;

            networkOut.println("Register " + tf1.getText() + " " + tf2.getText());
            try {
                message = networkIn.readLine();
            } catch (IOException e) {
                System.err.println("Error reading response to Register.");
            }

            if(message.equals("invalidAccount")){
                label.setText("Invalid Account");
            }
            else if(message.equals("correct")){
                label.setText("Register Successfully");
            }

        });
        profile.setOnAction(actionEvent -> {
        networkOut.println("Profile");
            try {
                System.out.println(networkIn.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        bag.setOnAction(actionEvent -> {
            networkOut.println("Bag");
            try {
                System.out.println(networkIn.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        battle.setOnAction(actionEvent -> {
            networkOut.println("Battle");
            primaryStage.setScene(prebattle_scene);
        });

    }

    private int frameWidth = 480;
    private int frameHeight = 310;
    private int numFrames = 3;
    private int sourceHeightOffset = 0;
    private int sourceWidthOffset = 0;
    private int frameIndex = 0;


   private void drawAnimation(Group root) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
//loading image sprite using relative path
        Image image = new Image(getClass().getClassLoader().getResource("images/bruce.jpg").toString());

        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(288), new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent actionEvent) {
//                background rect fpr the characters

                gc.drawImage(image, sourceWidthOffset, sourceHeightOffset, frameWidth, frameHeight, 300, 170, frameWidth, frameHeight);

//                we want to vary frameIndex from 0 to numFrames (not included) using the %
                frameIndex = (frameIndex+1) % numFrames;

//                calculating the offset height based on the frameIndex
                sourceHeightOffset = frameHeight*frameIndex;

            }
        }));

//      Starting the timeline
        timeline.playFromStart();
    }

    private void draw(Group root) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

//        C
        gc.setStroke(Color.RED);
        gc.strokeLine(320,20,350,30);
        gc.setStroke(Color.RED);
        gc.strokeLine(320,20,310,60);
        gc.setStroke(Color.RED);
        gc.strokeLine(310,60,350,60);

        //R
        gc.setStroke(Color.RED);
        gc.strokeArc(330, 20, 50, 90, 45, 45, ArcType.ROUND);
        gc.setStroke(Color.RED);
        gc.strokeLine(365,50,375,65);

        //A
        gc.setStroke(Color.RED);
        gc.strokeLine(400,20,380,60);
        gc.setStroke(Color.RED);
        gc.strokeLine(400,20,420,60);
        gc.setStroke(Color.RED);
        gc.strokeLine(390,40,410,40);

        //Z
        gc.setStroke(Color.RED);
        gc.strokeLine(425,20,450,20);
        gc.setStroke(Color.RED);
        gc.strokeLine(425,60,450,60);
        gc.setStroke(Color.RED);
        gc.strokeLine(450,20,425,60);

        //Y
        gc.setStroke(Color.RED);
        gc.strokeLine(455,20,465,40);
        gc.setStroke(Color.RED);
        gc.strokeLine(475,20,455,60);

        //F
        gc.setStroke(Color.RED);
        gc.strokeLine(310,80,350,70);
        gc.setStroke(Color.RED);
        gc.strokeLine(315,80,310,120);
        gc.setStroke(Color.RED);
        gc.strokeLine(320,100,340,95);

        //I
        gc.setStroke(Color.RED);
        gc.strokeLine(360,80,355,120);

        //G
        gc.setStroke(Color.ORANGE);
        gc.strokeArc(380, 80, 50, 45, 80, 180, ArcType.ROUND);
        gc.setStroke(Color.RED);
        gc.strokeLine(390,100,405,100);

        //H
        gc.setStroke(Color.RED);
        gc.strokeLine(420,80,415,120);
        gc.setStroke(Color.RED);
        gc.strokeLine(420,100,440,100);
        gc.setStroke(Color.RED);
        gc.strokeLine(445,80,440,120);

        //T
        gc.setStroke(Color.RED);
        gc.strokeLine(452,80,475,80);
        gc.setStroke(Color.RED);
        gc.strokeLine(463,80,458,120);

        //Line
        gc.setFill(Color.DEEPPINK);
        gc.fillPolygon(new double[] {300, 480, 350}, new double[] {150, 150, 140}, 3);

    }


    public static void main(String[] args) {
        launch(args);
    }
}
