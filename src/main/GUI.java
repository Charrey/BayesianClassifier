package main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;


/**
 * Created by René on 2-12-2015.
 */
public class GUI extends Application {


    public static void main(String[] args){
        launch();
    }


    private TextArea input;
    private Button validateButton, agreeButton, declineButton;
    private Label statusLabel;
    private VBox statPanel;
    private GridPane gridPanel;

    private int rowCount = 0;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Text classifier");

        Font f = new Font("Arial", 25);

        BorderPane mainPaine = new BorderPane();
        input = new TextArea();
        HBox borderpannel = new HBox();
        validateButton = new Button("validate");
        validateButton.setFont(f);

        validateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                double[] result = MathManager.getTrueProbSentece(Word.sanitize(input.getText()));
                System.out.println("result: c: "+result[0]+" nc: "+result[1]);

            }
        });

        agreeButton = new Button("agree");
        agreeButton.setFont(f);
        declineButton = new Button("decline");
        declineButton.setFont(f);
        statusLabel = new Label("ready to validate");
        statusLabel.setMinWidth(400);
        statusLabel.setFont(f);
        borderpannel.getChildren().addAll(validateButton, statusLabel, agreeButton, declineButton);
        mainPaine.setBottom(borderpannel);
        mainPaine.setCenter(input);

        statPanel = new VBox();
        statPanel.setMinWidth(250);

        gridPanel = new GridPane();


        Label statTitle = new Label("Statistics");
        statTitle.setFont(new Font("Arial", 30));
        statTitle.setStyle("-fx-font-weight: bold");
        statPanel.getChildren().addAll(statTitle);
        mainPaine.setRight(statPanel);
        statPanel.getChildren().add(gridPanel);
        gridPanel.setMinWidth(250);
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(50);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(25);
        ColumnConstraints column3 = new ColumnConstraints();
        column3.setPercentWidth(25);
        gridPanel.getColumnConstraints().addAll(column1, column2, column3);
        /*
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });*/

        //StackPane root = new StackPane();
        //root.getChildren().add(btn);
        setStatus(Status.READY);
        addStat("Description", "C", "not C");
        primaryStage.setScene(new Scene(mainPaine, 1000, 1000));
        primaryStage.show();

        addStat("Documents classfied as", DataManager.getTotalDocumentTrueCount(), DataManager.getTotalDocumentCount()-DataManager.getTotalDocumentTrueCount());
    }

    public void addStat(String desc, double C, double nC){
        addStat(desc, String.valueOf(C), String.valueOf(nC));
    }

    public void addStat(String desc, String C, String nC){
        gridPanel.add(new Label(desc), 0, rowCount);
        gridPanel.add(new Label(C), 1, rowCount);
        gridPanel.add(new Label(nC), 2, rowCount);

        rowCount ++;
    }

    public void setStatus(Status status){
        setStatus(status, false);
    }

    public void setStatus(Status status, boolean clasified){
        Thread t = new Thread(new Task() {
            @Override
            protected Object call() throws Exception {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        switch (status){
                            case READY:
                                validateButton.setText("Validate");
                                validateButton.setDisable(false);
                                agreeButton.setDisable(true);
                                declineButton.setDisable(true);
                                statusLabel.setText("Ready to validate");
                                break;
                            case VALIDATING:
                                validateButton.setDisable(true);
                                statusLabel.setText("Validating");
                                break;
                            case BUILDING:
                                //TODO
                                break;
                            case DONE:
                                agreeButton.setDisable(false);
                                declineButton.setDisable(false);
                                statusLabel.setText("Classified as "+clasified);
                                break;
                        }
                    }
                });
                return null;
            }
        });
        t.setDaemon(true);
        t.start();
    }

}
