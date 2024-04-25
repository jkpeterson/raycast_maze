import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ExitScreen {
    public static void showExit(Stage primaryStage, long startTime, long endTime) {
        long elapsedTime = endTime - startTime;
        long minutes = (elapsedTime / 1000) / 60;
        long seconds = (elapsedTime / 1000) % 60;

        VBox endLayout = new VBox(20);
        endLayout.setAlignment(Pos.CENTER);

        Text congratsText = new Text("Congratulations! You reached the exit.");
        congratsText.setFont(Font.font(20));

        String elapsedTimeString = String.format("Time %02d : %02d", minutes, seconds);
        Text timeText = new Text(elapsedTimeString);
        timeText.setFont(Font.font(15));

        Button backToMainButton = new Button("Back to Main");
        backToMainButton.setOnAction(e -> {
            primaryStage.close();
            new TitleScreen().start(new Stage());
        });

        endLayout.getChildren().addAll(congratsText, timeText, backToMainButton);

        double currentWidth = primaryStage.getWidth();
        double currentHeight = primaryStage.getHeight();

        Scene exitScene = new Scene(endLayout, currentWidth, currentHeight);
        primaryStage.setScene(exitScene);
    }
}
