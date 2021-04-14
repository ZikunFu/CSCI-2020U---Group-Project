package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.util.Duration;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * this is about UI setting and set the functions for the game
 * we added a thread for kepp asking the server to get connected
 * and then make the login scene
 * then make a menu Scene for the game
 * at last there is a textarea for battle
 */
public class Main extends Application {
    private Canvas canvas;
    //private double screenWidth = 100;
    //private double screenHeight = 600;

    private Socket socket = null;
    private PrintWriter networkOut = null;
    private BufferedReader networkIn = null;

    ListView<String> list;
    public  static String SERVER_ADDRESS = "localhost";
    public  static int    SERVER_PORT = 16789;
    @Override
    public void start(Stage primaryStage){
        //Thread for establishing server connection

        new Thread(()-> {
            while (true) {
                System.out.println("connecting...");
                try {
                    socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                    if (socket != null) {
                        networkOut = new PrintWriter(socket.getOutputStream(), true);
                        networkIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        break;
                    }

                } catch (UnknownHostException e) {
                    System.err.println("Unknown host: "+SERVER_ADDRESS);
                } catch (IOException e) {
                    System.err.println("IOException while connecting to "+SERVER_ADDRESS);
                }

            } }).start();



        //login scene
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Game");

        GridPane myGrid = new GridPane();
        myGrid.setAlignment(Pos.CENTER_LEFT);
        myGrid.setHgap(47);
        myGrid.setVgap(48);
        myGrid.setPadding(new Insets(25, 25, 25, 25));
        myGrid.setStyle("-fx-background-color: BEIGE;");

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
        //adding image for login and register
        Image img1 = new Image("sample/resources/images/login.png",20,20,true,true);
        Image img2 = new Image("sample/resources/images/register.png",20,20,true,true);
        ImageView view1 = new ImageView(img1);
        ImageView view2 = new ImageView(img2);
        login.setGraphic(view1);
        register.setGraphic(view2);
        myGrid.add(label, 1, 6);

        //LOGO
        Group root = new Group();
        Scene scene = new Scene(root, 780, 450);


        //Create Canvas object and add it into the scene
        canvas = new Canvas();
        canvas.widthProperty().bind(primaryStage.widthProperty());
        canvas.heightProperty().bind(primaryStage.heightProperty());


        //root.getChildren().addAll(myGrid, canvas);
        root.getChildren().addAll(canvas, myGrid);

        primaryStage.setTitle("Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        //Creating mainScene
        BorderPane border1 = new BorderPane();
        border1.setStyle("-fx-background-color: BEIGE;");
        Button profile = new Button("profile");
        Button bag = new Button("bag");
        Button battle = new Button("battle");
        list = new ListView<>();
        VBox vbox = new VBox();
        vbox.setPrefWidth(400);
        vbox.setSpacing(50);
        profile.setMaxWidth(vbox.getPrefWidth());
        bag.setMaxWidth(vbox.getPrefWidth());
        battle.setMaxWidth(vbox.getPrefWidth());
        vbox.getChildren().addAll(profile,bag,battle);
        vbox.setAlignment(Pos.CENTER_RIGHT);
        border1.setLeft(vbox);
        border1.setRight(list);
        Scene main_scene = new Scene(border1,600,400);
        Image img3 = new Image("sample/resources/images/profile.png",20,20,true,true);
        Image img4 = new Image("sample/resources/images/bag.png",20,20,true,true);
        Image img5 = new Image("sample/resources/images/battle.png",20,20,true,true);
        ImageView view3 = new ImageView(img3);
        ImageView view4 = new ImageView(img4);
        ImageView view5 = new ImageView(img5);
        profile.setGraphic(view3);
        bag.setGraphic(view4);
        battle.setGraphic(view5);

        //creating pre-battle Scene
        BorderPane border2 = new BorderPane();
        border2.setStyle("-fx-background-color: BEIGE;");
        Label label1 = new Label("Waiting for the opponent");
        border2.setCenter(label1);
        Scene prebattle_scene = new Scene(border2,600,400);
        Button back = new Button("back");

        //Creating Battle Scene
        TextArea textArea = new TextArea();
        VBox vbox1 = new VBox();
        vbox1.setStyle("-fx-background-color: BEIGE;");
        vbox1.getChildren().addAll(textArea,back);
        vbox1.setAlignment(Pos.BOTTOM_CENTER);
        vbox1.setSpacing(80);
        vbox1.setPrefHeight(40);
        back.setMaxHeight(vbox1.getMaxHeight());
        back.setMinSize(110,110);
        Scene battle_scene = new Scene(vbox1,600,400);

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

            if(message==null){
                System.err.println("Error, message is null");
            }
            else if(message.equals("correct")){
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
        register.setOnAction(actionEvent -> {

            String message = null;

            networkOut.println("Register " + tf1.getText() + " " + tf2.getText());
            try {
                message = networkIn.readLine();
            } catch (IOException e) {
                System.err.println("Error reading response to Register.");
            }

            if(message==null){
                System.err.println("Error, message is null");
            }
            else if(message.equals("invalidAccount")){
                label.setText("Invalid Account");
            }
            else if(message.equals("correct")){
                label.setText("Register Successfully");
            }

        });

        profile.setOnAction(actionEvent -> {
            networkOut.println("profile");
            String text_profile;
            try {
                list.getItems().clear();
                text_profile = networkIn.readLine();
                String[] parts = text_profile.split(",");
                list.getItems().add("HP: "+parts[2]);
                list.getItems().add("Attack: "+parts[3]);
                list.getItems().add("Defence: "+parts[4]);
                list.getItems().add("Rank: "+parts[5]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        bag.setOnAction(actionEvent -> {
            String text_bag;
            networkOut.println("bag");
            try {
                list.getItems().clear();
                text_bag = networkIn.readLine();
                String[] parts = text_bag.split(" ");
                for( int i =0; i< parts.length; i++){
                    list.getItems().add(parts[i]);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        battle.setOnAction(actionEvent -> {
            networkOut.println("battle");
            primaryStage.setScene(prebattle_scene);
            new Thread(()-> {
                while (true){
                    try {
                        String message = networkIn.readLine();
                        if (message!=null){
                            if(message.equals("start")){
                                Platform.runLater(new Runnable(){
                                    @Override
                                    public void run() {
                                        primaryStage.setScene(battle_scene);
                                        try {
                                            String battle_lines;
                                            battle_lines = networkIn.readLine();
                                            String replace_string = battle_lines.replace('#','\n');
                                            textArea.setText(replace_string);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                break;
                            }
                            else {
                                System.err.println("?????"+message);
                            }
                        }
                    }
                    catch (IOException e) { e.printStackTrace(); }
                }

            }).start();
        });
        back.setOnAction(actionEvent ->{
            primaryStage.setScene(main_scene);
        });

    }

    final private int frameWidth = 480;
    final private int frameHeight = 310;
    final private int numFrames = 3;
    private int sourceHeightOffset = 0;
    private int sourceWidthOffset = 0;
    private int frameIndex = 0;

    /**
     * this method is used for the animation
     * add timeline for change the png by frame
     * @param root the stream of the png
     */
    private void drawAnimation(Group root) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
//loading image sprite using relative path
        Image image = new Image(getClass().getClassLoader().getResource("sample/resources/images/bruce.jpg").toString());

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

    /**
     * this is used for drawing the logo of the game     * @param root the draw
     */
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
