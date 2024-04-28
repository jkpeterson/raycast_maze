import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ExitScreen {
	//
	// Class: ExitScreen
	//
	// Description:
	// Screen that shows the final time the player took to navigate the maze
	// Offers buttons to return to title screen or play a new maze.
	
	//
	// showExit()
	//
	// Input: primaryStage, start and end times, whether maze was random
	// Output: Nothing
	// Handles all of the exit screen, displays the timer results and the continue button
	// which will either generate a new maze or send the user to the title screen.
    public static void showExit(Stage primaryStage, long startTime, long endTime, Boolean isRandomMaze) {

    	//screen is shown after the player completes a maze
        long elapsedTime = endTime - startTime;
        long minutes = (elapsedTime / 1000) / 60;
        long seconds = (elapsedTime / 1000) % 60;

        VBox endLayout = new VBox(20);
        endLayout.setAlignment(Pos.CENTER);

        Text congratsText = new Text("Congratulations! You reached the exit.");
        congratsText.setFont(Font.font(20));
        congratsText.setStyle("-fx-fill: white;");

        String elapsedTimeString = String.format("Time %02d : %02d", minutes, seconds);
        Text timeText = new Text(elapsedTimeString);
        timeText.setFont(Font.font(15));
        timeText.setStyle("-fx-fill: white;");

        Button backToMainButton = new Button("Back to Main");
        backToMainButton.setOnAction(e -> {
            primaryStage.close();
            new TitleScreen().start(new Stage());
        });

        Button continueButton = new Button("Play Another");
        if (isRandomMaze) {
            continueButton.setOnAction(e -> {
                primaryStage.close();
                try {
                    new RaycastMaze(true, null).start(new Stage());
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });
            endLayout.getChildren().addAll(congratsText, timeText, continueButton, backToMainButton);
        } else {
            endLayout.getChildren().addAll(congratsText, timeText, backToMainButton);
        }


        // Load background image
        Image backgroundImage = new Image("Images/end_background.png");
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true));
        endLayout.setBackground(new Background(background));

        double currentWidth = primaryStage.getWidth();
        double currentHeight = primaryStage.getHeight();

        Scene exitScene = new Scene(endLayout, currentWidth, currentHeight);
        primaryStage.setScene(exitScene);
    }
}
