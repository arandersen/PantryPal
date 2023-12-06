package project;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.sound.sampled.*;

import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;

public class SpecifyIngredientsPageController implements Controller {
    private SpecifyIngredientsPage view;
    private Model model;

    private TargetDataLine targetDataLine;
    private AudioFormat audioFormat;
    private static final String TEMP_AUDIO_FILE_PATH = "tempAudio.wav";
    public String mealType;
    private String transcribedText;

    public SpecifyIngredientsPageController(SpecifyIngredientsPage view, Model model) {
        this.view = view;
        this.model = model;

        this.view.setRecordHoldAction(event -> {
            try {
                handleRecordHoldButton(event);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });

        this.view.setRecordReleaseAction(event -> {
            try {
                handleRecordReleasetButton(event);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });

        this.view.setCancelButtonAction(event -> {
            try {
                handleCancelButton(event);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }

    // Returns the audio format to use for the recording for Specify Ingredient Page
    // and Specify Meal Type Page
    // NOTE: This is the same format that is used for the Whisper transcribeAudio
    // method

    public void handleRecordHoldButton(MouseEvent event) throws IOException {
        try {
            System.out.println("Starting to Record");
            audioFormat = getAudioFormat();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
            targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
            targetDataLine.open(audioFormat);
            targetDataLine.start();

            Thread recordingThread = new Thread(() -> {
                try {
                    AudioInputStream inputStream = new AudioInputStream(targetDataLine);
                    File audioFile = new File(TEMP_AUDIO_FILE_PATH);
                    AudioSystem.write(inputStream, AudioFileFormat.Type.WAVE, audioFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            recordingThread.start();
            System.out.println("Recording started...");
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

    }

    // Returns the audio format to use for the recording for SpecifyMealTypePage
    // and Specify Meal Type Page

    public Recipe handleRecordReleasetButton(MouseEvent event) throws IOException {
        if (targetDataLine != null) {
            targetDataLine.stop();
            targetDataLine.close();
            System.out.println("Recording stopped.");

            try {
                // Transcripe Audio
                String transcribedText = model.performRequest("POST", null, null, null, TEMP_AUDIO_FILE_PATH, null,
                        null, null, null, null, null);
                this.transcribedText = transcribedText;
                System.out.println("Transcription: " + transcribedText);

                // Send the transcribed text to ChatGPT and get a response
                String response = model.performRequest("POST", "generateRecipe", null, null, null, mealType,
                        transcribedText, null, null, null, null);
                System.out.println("ChatGPT Response: " + response);
                Recipe recipe = createRecipe(response);
                DetailedRecipePage temp = new DetailedRecipePage(recipe, true);
                Main.setPage(temp);
                Main.setController(new DetailedRecipePageController(temp, model));
                return recipe;
                // Handle the UI update or user notification with the generated recipe response
            } catch (Exception e) {
                e.printStackTrace();
                Main.showAlert("Error", "Server temporarily unavailable. Please try again later.");
                // Handle exceptions appropriately
            }
        }
        return null;
    }

    private boolean handleCancelButton(ActionEvent event) throws IOException {
        try {
            // Add Recipe Information
            String JSON = model.performRequest("GET", "getRecipeList", null, null, null, null, null, null, null, null,
                    null);
            List<Recipe> recipes = Main.extractRecipeInfo(JSON);
            RecipeListPage listPage = new RecipeListPage(recipes);
            Main.setPage(listPage);
            Main.setController(new RecipeListPageController(listPage, model));
            return true;
        } catch (Exception e) {
            Main.showAlert("Error", "Server temporarily unavailable. Please try again later.");
            return false;
        }
    }

    public Recipe createRecipe(String gptResponse) {
        int firstNewLineIndex = gptResponse.indexOf("\n");

        // Check if the newline character is present
        if (firstNewLineIndex == -1) {
            // Handle the case where there is no newline character
            // For example, you can set the title to the entire response
            // and set the instructions to an empty string or some default value
            return new Recipe(gptResponse, "", transcribedText, mealType);
        } else {
            // Split the string into title and instructions
            String recipeTitle = gptResponse.substring(0, firstNewLineIndex);
            String recipeInstructions = gptResponse.substring(firstNewLineIndex + 1); // +1 to skip the newline
                                                                                      // character
            return new Recipe(recipeTitle, recipeInstructions, transcribedText, mealType);
        }
    }

    // Returns the audio format to use for the recording for Specify Ingredient Page
    // and Specify Meal Type Page
    private AudioFormat getAudioFormat() {
        float sampleRate = 16000; // typically 44100 for music
        int sampleSizeInBits = 16;
        int channels = 1; // mono
        boolean signed = true;
        boolean bigEndian = false;
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }
}
