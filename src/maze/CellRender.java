package maze;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

// Class to handle rendering of Cell object in the Maze
public class CellRender {
    // Walls of the cell represented as rectangles
    Rectangle wallTop;
    Rectangle wallBottom;
    Rectangle wallLeft;
    Rectangle wallRight;
    public int x, y, size;
    private boolean visited = false;

    // Constructor: Builds walls for each cell
    public CellRender(int x, int y, int size, Pane pane) {
        this.x = x;
        this.y = y;
        this.size = size;
        int thickness = 2;
        wallTop = new Rectangle(x * size, y * size, size, thickness);
        wallBottom = new Rectangle(x * size, (y + 1) * size - thickness, size, thickness);
        wallLeft = new Rectangle(x * size, y * size, thickness, size);
        wallRight = new Rectangle((x + 1) * size - thickness, y * size, thickness, size);

        wallTop.setFill(Color.BLACK);
        wallBottom.setFill(Color.BLACK);
        wallLeft.setFill(Color.BLACK);
        wallRight.setFill(Color.BLACK);

        addWallsToPane(pane);
    }

    // Handle removal of walls based on supplied direction
    public void removeWall(MazeGenerator.Direction direction, Pane pane) {
        switch (direction) {
            case UP:
                wallTop.setVisible(false);
                pane.getChildren().remove(wallTop);
                break;
            case DOWN:
                wallBottom.setVisible(false);
                pane.getChildren().remove(wallBottom);
                break;
            case LEFT:
                wallLeft.setVisible(false);
                pane.getChildren().remove(wallLeft);
                break;
            case RIGHT:
                wallRight.setVisible(false);
                pane.getChildren().remove(wallRight);
                break;
        }
    }

    // Remove the walls for the exit point of the maze
    public void removeWallsForExitPoint(int mazeWidth, int mazeHeight, Pane pane) {
        if (y == 0) { // Top edge
            wallTop.setVisible(false);
            pane.getChildren().remove(wallTop);
        } else if (y == mazeHeight - 1) { // Bottom edge
            wallBottom.setVisible(false);
            pane.getChildren().remove(wallBottom);
        } else if (x == 0) { // Left edge
            wallLeft.setVisible(false);
            pane.getChildren().remove(wallLeft);
        } else if (x == mazeWidth - 1) { // Right edge
            wallRight.setVisible(false);
            pane.getChildren().remove(wallRight);
        }
    }

    // Adds walls to pane (visually represents walls)
    public void addWallsToPane(Pane pane) {
        pane.getChildren().addAll(wallTop, wallBottom, wallLeft, wallRight);
    }

    // Check if a wall exists in a given direction
    public boolean hasWall(MazeGenerator.Direction direction) {
        return !switch (direction) {
            case UP -> wallTop.isVisible();
            case DOWN -> wallBottom.isVisible();
            case LEFT -> wallLeft.isVisible();
            case RIGHT -> wallRight.isVisible();
        };
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public boolean isVisited() {
        return visited;
    }

}
