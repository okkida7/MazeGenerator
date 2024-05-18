package maze;

import javafx.application.Platform;
import java.io.*;
import java.util.List;

import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class MazeEditorController {
    private final Slider sizeSlider;
    private final Button generateButton;
    private final Pane mazeDisplayPane;
    private Pane mazePane;
    private MazeGenerator mazeGenerator;
    private MazeLoader mazeLoader;
    private final Text text;

    public MazeEditorController(Text text, Pane mazeDisplayPane, Slider sizeSlider, Button generateButton) {
        this.mazeDisplayPane = mazeDisplayPane;
        this.sizeSlider = sizeSlider;
        this.generateButton = generateButton;
        this.text = text;
    }

    public void initialize() {
        generateMaze();
        generateButton.setOnAction(e -> clearMaze());

        sizeSlider.valueProperty().addListener((obs, oldVal, newVal) -> updateMazeSize(oldVal.intValue(), newVal.intValue()));
    }

    public Pane getPane() {
        return mazePane;
    }

    private void generateMaze() {
        mazePane = new Pane();
        if(mazeGenerator == null) {
            mazeGenerator = new MazeGenerator((int) sizeSlider.getValue(), (int) sizeSlider.getValue());
            mazeGenerator.generateMaze(mazePane);

            int cellSize = mazeGenerator.getCellSize();
            int mazeWidth = (int) sizeSlider.getValue() * cellSize;
            int mazeHeight = (int) sizeSlider.getValue() * cellSize;
            MazeCell[][] maze = mazeGenerator.getMaze();
            CellRender[][] mazeCell = mazeGenerator.getMazeCell();

            Character player = new Character((int) sizeSlider.getValue(), mazeLoader, mazeGenerator, 0, 0, cellSize, maze, mazeCell);


            mazePane.setPrefSize(mazeWidth, mazeHeight);
            mazePane.setLayoutX((double) (800 - mazeWidth) / 2);
            mazePane.setLayoutY((double) (800 - mazeHeight) / 2);
            mazePane.getChildren().add(player.getGraphic());
            mazeDisplayPane.getChildren().add(mazePane);
            mazeDisplayPane.setFocusTraversable(true);
            mazeDisplayPane.setOnKeyPressed(e -> handleKeyPress(e, player, maze));
            Platform.runLater(mazeDisplayPane::requestFocus);
        }
    }



    private void clearMaze() {
        text.setVisible(false);
        mazeDisplayPane.getChildren().remove(mazePane);
        mazePane = null;
        mazeGenerator = null;
        generateMaze();
    }

    private void updateMazeSize(int oldSize, int newSize) {
        if(mazeGenerator != null)
        {
            mazeGenerator.setSize(newSize, newSize);
        } else if(mazeLoader != null){
            mazeLoader.setSize(newSize, newSize);
        } else {
        }

    }

    private void handleKeyPress(KeyEvent e, Character player, MazeCell[][] maze) {
        switch (e.getCode()) {
            case UP:
                player.moveUp();
                player.winCondition(text);
                break;
            case DOWN:
                player.moveDown();
                player.winCondition(text);
                break;
            case LEFT:
                player.moveLeft();
                player.winCondition(text);
                break;
            case RIGHT:
                player.moveRight();
                player.winCondition(text);
                break;
            default:
                break;
        }
        e.consume();
    }

    public void saveMaze(File file) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            if(mazeGenerator != null) {
                out.writeObject(mazeGenerator.getOpenDirections());
                out.writeObject(mazeGenerator.getOpenDirections1());
                out.writeObject(mazeGenerator.getCountValue());
                out.writeObject(mazeGenerator.getCountValue1());
                out.writeInt(mazeGenerator.getCountValue2());
            } else if (mazeLoader != null) {
                out.writeObject(mazeLoader.getOpenDirections());
                out.writeObject(mazeLoader.getOpenDirections1());
                out.writeObject(mazeLoader.getCountValue());
                out.writeObject(mazeLoader.getCountValue1());
                out.writeInt(mazeLoader.getCountValue2());
            }
            out.writeInt((int) sizeSlider.getValue());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMaze(File file) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            List<MazeGenerator.Direction> newDirections = (List<MazeGenerator.Direction>) in.readObject();
            List<MazeGenerator.Direction> newDirections1 = (List<MazeGenerator.Direction>) in.readObject();
            List<Integer> countValue = (List<Integer>) in.readObject();
            List<Integer> countValue1 = (List<Integer>) in.readObject();
            int countValue2 = in.readInt();
            int width = in.readInt();
            //Set<MazeGenerator.Direction> oppositeDirections = (Set<MazeGenerator.Direction>) in.readObject();
            rebuildMaze(width, width, newDirections, newDirections1, countValue, countValue1, countValue2);
            sizeSlider.setValue(width);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void rebuildMaze(int width, int height, List<MazeGenerator.Direction> newDirections, List<MazeGenerator.Direction> newDirections1, List<Integer> countValue, List<Integer> countValue1, int countValue2) {
        text.setVisible(false);
        mazeDisplayPane.getChildren().remove(mazePane);
        mazePane = null;
        mazeGenerator = null;
        if(mazeLoader == null) {
            mazePane = new Pane();
            mazeLoader = new MazeLoader(width, height, newDirections, newDirections1, countValue, countValue1, countValue2);
            mazeLoader.generateMaze(mazePane);

            int cellSize = mazeLoader.getCellSize();
            int mazeWidth = width * cellSize;
            int mazeHeight = height * cellSize;
            MazeCell[][] maze = mazeLoader.getMaze();
            CellRender[][] mazeCell = mazeLoader.getMazeCell();
            Character player = new Character(width, mazeLoader, mazeGenerator, 0, 0, cellSize, maze, mazeCell);

            mazePane.setPrefSize(mazeWidth, mazeHeight);
            mazePane.setLayoutX((double) (800 - mazeWidth) / 2);
            mazePane.setLayoutY((double) (800 - mazeHeight) / 2);
            mazePane.getChildren().add(player.getGraphic());
            mazeDisplayPane.getChildren().add(mazePane);
            mazeDisplayPane.setFocusTraversable(true);
            mazeDisplayPane.setOnKeyPressed(e -> handleKeyPress(e, player, maze));
            Platform.runLater(() -> mazeDisplayPane.requestFocus());
        }
    }

}
