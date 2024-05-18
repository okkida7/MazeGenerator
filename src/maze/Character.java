package maze;

import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

// Character class represents a character in the maze
public class Character {
    private final Rectangle graphic;
    private int x, y; // Current positions
    private final int size; // Size of each cell, assuming square cells
    private final MazeCell[][] maze;
    private final CellRender[][] mazeCell;
    private final MazeLoader mazeLoader;
    private final MazeGenerator mazeGenerator;
    private final int width;

    public Character(int width, MazeLoader mazeLoader, MazeGenerator mazeGenerator, int x, int y, int size, MazeCell[][] maze, CellRender[][] mazeCell) {
        this.width = width;
        this.mazeLoader = mazeLoader;
        this.mazeGenerator = mazeGenerator;
        this.x = x;
        this.y = y;
        this.size = size;
        graphic = new Rectangle(x * size, y * size, size, size);
        graphic.setFill(javafx.scene.paint.Color.BLUE);
        this.maze = maze;
        this.mazeCell = mazeCell;
    }

    // Checks and updates the win condition
    public void winCondition(Text text) {
        if(mazeGenerator != null) {
            if((x * width + y) == mazeGenerator.getCountValue2()) {
                text.setText("You Win!");
                text.setVisible(true);
            }
        }
        if(mazeLoader != null) {
            if((x * width + y) == mazeLoader.getEndPoint()) {
                text.setText("You Win!");
                text.setVisible(true);
            }
        }
    }

    public void moveUp() {
        if (y > 0 && mazeCell[x][y - 1].hasWall(MazeGenerator.Direction.DOWN)) {
            y--;
            graphic.setY(y * size);
        }
    }

    public void moveDown() {
        if (y < maze[0].length - 1 && mazeCell[x][y].hasWall(MazeGenerator.Direction.DOWN)) {
            y++;
            graphic.setY(y * size);
        }
    }

    public void moveLeft() {
        if (x > 0 && mazeCell[x - 1][y].hasWall(MazeGenerator.Direction.RIGHT)) {
            x--;
            graphic.setX(x * size);
        }
    }

    public void moveRight() {
        if (x < maze.length - 1 && mazeCell[x][y].hasWall(MazeGenerator.Direction.RIGHT)) {
            x++;
            graphic.setX(x * size);
        }
    }

    public Rectangle getGraphic() {
        return graphic;
    }
}
