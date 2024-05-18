package maze;
import javafx.scene.layout.Pane;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Arrays;

public class MazeGenerator implements Serializable{
    @Serial
    private static final long serialVersionUID = 1L;
    private int mazeWidth, mazeHeight;
    private transient MazeCell[][] maze;
    private final transient CellRender[][] cellRender;
    private final Random random = new Random();
    private CellRender endPoint;
    private int maxDepth = 0;
    private final int cellSize = 40;
    public List<Direction> openDirections = new ArrayList<>();
    public List<Direction> openDirections1 = new ArrayList<>();
    public List<Integer> countValue = new ArrayList<>();
    public List<Integer> countValue1 = new ArrayList<>();
    public int countValue2;


    public int getCellSize() {
        return cellSize;
    }

    public MazeGenerator(int width, int height) {
        this.mazeWidth = width;
        this.mazeHeight = height;
        this.maze = new MazeCell[mazeWidth][mazeHeight];
        this.cellRender = new CellRender[mazeWidth][mazeHeight];
    }

    public void generateMaze(Pane pane) {
        initializeMaze(pane);
        carvePath(0, 0, pane, 0);
        if (endPoint != null) {
            endPoint.removeWallsForExitPoint(mazeWidth, mazeHeight, pane); // Set the exit at the farthest edge point
        }
    }

    private void initializeMaze(Pane pane) {

        for (int x = 0; x < mazeWidth; x++) {
            for (int y = 0; y < mazeHeight; y++) {

                maze[x][y] = new MazeCell(x, y, cellSize);
                cellRender[x][y] = new CellRender(x,y,cellSize, pane);
            }
        }
        cellRender[0][0].removeWall(MazeGenerator.Direction.UP, pane);  // Remove top wall at start point
    }

    private void carvePath(int x, int y, Pane pane, int depth) {
        cellRender[x][y].setVisited(true);
        if (isEdgeCell(x, y) && depth > maxDepth) {
            maxDepth = depth;
            endPoint = cellRender[x][y];
            countValue2 = x * mazeWidth + y;
        }
        List<Direction> directions = getRandomDirections();
        for (Direction direction : directions) {
            int newX = x + direction.getDx();
            int newY = y + direction.getDy();

            if (isInBounds(newX, newY) && !cellRender[newX][newY].isVisited()) {
                // Mark the direction as open for both cells

                // Calculate and save the index for use during saving/loading
                int count = x * mazeWidth + y;
                int countOpposite = newX * mazeWidth + newY;
                countValue.add(count);
                countValue1.add(countOpposite);
                openDirections.add(direction);
                openDirections1.add(direction.getOpposite());

                // Remove walls from both cells
                cellRender[x][y].removeWall(direction, pane);
                cellRender[newX][newY].removeWall(direction.getOpposite(), pane);

                // Recursive call to continue carving
                carvePath(newX, newY, pane, depth + 1);
            }
        }
    }


    public void setSize(int width, int height) {
        this.mazeWidth = width;
        this.mazeHeight = height;
        this.maze = new MazeCell[mazeWidth][mazeHeight];  // Reallocate the maze array with new dimensions // Initialize the maze with new dimensions
    }

    private boolean isEdgeCell(int x, int y) {
        return x == 0 || x == mazeWidth - 1 || y == 0 || y == mazeHeight - 1;
    }

    private List<Direction> getRandomDirections() {
        List<Direction> directions = new ArrayList<>(Arrays.asList(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT));
        Collections.shuffle(directions, random);
        return directions;
    }

    private boolean isInBounds(int x, int y) {
        return x >= 0 && x < mazeWidth && y >= 0 && y < mazeHeight;
    }

    public enum Direction {
        UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0);

        private final int dx;
        private final int dy;

        Direction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        public int getDx() {
            return dx;
        }

        public int getDy() {
            return dy;
        }

        public Direction getOpposite() {
            return switch (this) {
                case UP -> DOWN;
                case DOWN -> UP;
                case LEFT -> RIGHT;
                case RIGHT -> LEFT;
            };
        }
    }

    public MazeCell[][] getMaze(){
        return maze;
    }


    public List<MazeGenerator.Direction> getOpenDirections() {
        List<MazeGenerator.Direction> allDirections;
        allDirections = openDirections;
        return allDirections;
    }

    public List<Integer> getCountValue() {
        List<Integer> allValues;
        allValues = countValue;
        return allValues;
    }

    public List<MazeGenerator.Direction> getOpenDirections1() {
        List<MazeGenerator.Direction> allDirections;
        allDirections = openDirections1;
        return allDirections;
    }

    public List<Integer> getCountValue1() {
        List<Integer> allValues;
        allValues = countValue1;
        return allValues;
    }

    public CellRender[][] getMazeCell(){
        return cellRender;
    }

    public int getCountValue2() {
        return countValue2;
    }
}
