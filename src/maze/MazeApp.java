package maze;

import java.io.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;

public class MazeApp extends Application {
    private static final String SAVE_DIRECTORY = "saves";
    private static final String DEFAULT_SAVE_FILE = "MazeConfiguration.ser";
    private File currentSaveFile = null;

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        // Top menu
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem saveItem = new MenuItem("Save");
        MenuItem loadItem = new MenuItem("Load");
        MenuItem exitItem = new MenuItem("Exit");

        fileMenu.getItems().addAll(saveItem, loadItem, exitItem);
        menuBar.getMenus().add(fileMenu);

        // Content
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);

        Text text = new Text("No saved maze exists.");
        text.setVisible(false);

        AnchorPane mazeDisplayPane = new AnchorPane();
        mazeDisplayPane.setPrefSize(800, 800);

        Slider sizeSlider = new Slider(5, 15, 10);
        sizeSlider.setMaxWidth(200);
        sizeSlider.setBlockIncrement(1);
        sizeSlider.setShowTickLabels(true);
        sizeSlider.setShowTickMarks(true);

        Button generateButton = new Button("Refresh Maze");


        vbox.getChildren().addAll(text, mazeDisplayPane, sizeSlider, generateButton);

        // Layout
        root.setTop(menuBar);
        root.setCenter(vbox);

        // Setup the Scene
        Scene scene = new Scene(root, 800, 800);
        primaryStage.setTitle("Maze Generator");
        primaryStage.setScene(scene);
        primaryStage.show();
        MazeEditorController controller = new MazeEditorController(text, mazeDisplayPane, sizeSlider, generateButton);
        controller.initialize();
        setupMenuActions(controller, saveItem, loadItem, exitItem, primaryStage, text);
    }

    private void setupMenuActions(MazeEditorController controller, MenuItem saveItem, MenuItem loadItem, MenuItem exitItem, Stage stage, Text text) {
        saveItem.setOnAction(e -> {
            try {
                if (currentSaveFile == null) {
                    File saveDir = new File(SAVE_DIRECTORY);
                    currentSaveFile = new File(saveDir, DEFAULT_SAVE_FILE);
                }
                controller.saveMaze(currentSaveFile);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        loadItem.setOnAction(e -> {
            File saveDir = new File(SAVE_DIRECTORY);
            if(currentSaveFile == null) {
                text.setVisible(true);
            } else {
                text.setVisible(false);
                currentSaveFile = new File(saveDir, DEFAULT_SAVE_FILE);
                controller.loadMaze(currentSaveFile);
            }
        });

        exitItem.setOnAction(e -> stage.close());
    }

    public static void main(String[] args) {
        launch(args);
    }


}
