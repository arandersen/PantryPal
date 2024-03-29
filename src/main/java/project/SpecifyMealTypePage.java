package project;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.TargetDataLine;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.geometry.Pos;

class SpecifyMealTypePage extends VBox {

    // Audio
    private Button recordButton;
    private TargetDataLine targetDataLine;
    private AudioFormat audioFormat;
    private static final String TEMP_AUDIO_FILE_PATH = "tempAudio.wav";
    private String recorderButtonStyle = "-fx-background-radius: 100; -fx-font-style: italic; -fx-background-color: #D9D9D9;  -fx-font-weight: bold; -fx-font: 18 arial;";
    public String mealType;
    private String errorMsgStyle = "-fx-font-size: 20;-fx-font-weight: bold; -fx-text-fill: #DF0000;";
    private Text errorMsg;
    private Boolean errorFlag = false;

    private HBox promptContainer;
    private HBox subPromptContainer;
    private HBox recordButtonContainer;
    private HBox backButtonContainer;

    private String promptStyle = "-fx-font-size: 20;-fx-font-weight: bold;";
    private String subPromptStyle = "-fx-font-size: 20;";

    private Label prompt;
    private Label subPrompt;

    private String backButtonStyle = "-fx-background-radius: 100; -fx-font-style: italic; -fx-background-color: #D9D9D9;  -fx-font-weight: bold; -fx-font: 18 arial;";
    private Button backButton;



    SpecifyMealTypePage() {
        this.setPrefSize(600, 700); // Size of the header
        this.setStyle("-fx-background-color: #FFFFFF;");


        // Set up labels
        prompt = new Label("What kind of meal are you making?");
        prompt.setStyle(promptStyle);
        subPrompt = new Label("Say \"Breakfast\", \"Lunch,\" or \"Dinner.\"");
        subPrompt.setStyle(subPromptStyle);

        //button to record audio
        recordButton = new Button("Hold to Record");
        recordButton.setStyle(recorderButtonStyle);
        recordButton.setPrefSize(300, 50);
       
        backButton = new Button("x");
        backButton.setStyle(backButtonStyle);
        backButton.setPrefSize(50, 50);
        
        // UI Container
        promptContainer = new HBox(prompt);
        promptContainer.setAlignment(Pos.CENTER);
        subPromptContainer = new HBox(subPrompt);
        subPromptContainer.setAlignment(Pos.CENTER);
        recordButtonContainer = new HBox(recordButton);
        recordButtonContainer.setAlignment(Pos.CENTER);
        backButtonContainer = new HBox(backButton);
        backButtonContainer.setAlignment(Pos.CENTER);

        this.getChildren().addAll(promptContainer, subPromptContainer, recordButtonContainer, backButtonContainer);
        this.setSpacing(10);
        this.requestLayout();
    }
    

    public void setRecordHoldAction(EventHandler<? super MouseEvent> eventHandler){
        recordButton.setOnMousePressed(eventHandler);
    }

    public void setRecordReleaseAction(EventHandler<? super MouseEvent> eventHandler){
        recordButton.setOnMouseReleased(eventHandler);
    }

    public void setBackButtonAction(EventHandler<ActionEvent> eventHandler){
        backButton.setOnAction(eventHandler);
    }

    public void errorMsg(){
        errorMsg = new Text("Sorry, we didn't catch that. Please Try Again");
        errorMsg.setStyle(errorMsgStyle);
        this.getChildren().add(errorMsg); 
    }

}

