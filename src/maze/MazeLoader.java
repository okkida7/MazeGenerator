package maze;
import javafx.scene.layout.Pane;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class MazeLoader implements Serializable{
    @Serial
    private static final long serialVersionUID = 1L;
    private int mazeWidth, mazeHeight;
    private final List<MazeGenerator.Direction> directions;
    private final List<Integer> countValue;
    private final List<MazeGenerator.Direction> directions1;
    private final List<Integer> countValue1;
    private final int countValue2;
    //private Set<MazeGenerator.Direction> oppositeDirections;
    private MazeCell[][] maze;
    private CellRender[][] drawMaze;
    public CellRender endPoint;
    public int getX;
    public int getY;
    public int matchEndPoint;
    private final int cellSize = 40;

    public int getEndPoint() {
        return matchEndPoint;
    }

    public int getCellSize() {
        return cellSize;
    }

    public MazeLoader(int width, int height, List<MazeGenerator.Direction> newDirections, List<MazeGenerator.Direction> newDirections1, List<Integer> countValue, List<Integer> countValue1, int countValue2) {
        this.mazeWidth = width;
        this.mazeHeight = height;
        this.maze = new MazeCell[mazeWidth][mazeHeight];
        this.directions = newDirections;
        this.countValue = countValue;
        this.directions1 = newDirections1;
        this.countValue1 = countValue1;
        this.countValue2 = countValue2;

        //this.oppositeDirections = oppositeDirections;
    }

    public void generateMaze(Pane pane) {
        int width = mazeWidth;
        int height = mazeHeight;
        initializeMaze(width, height, pane);
        //System.out.println(countValue);
    }

    private void initializeMaze(int width, int height, Pane pane) {
        int count = 0;
        this.drawMaze = new CellRender[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                maze[x][y] = new MazeCell(x, y, cellSize);
                drawMaze[x][y] = new CellRender(x, y, cellSize, pane);
                drawCell(x, y, width, height, pane, count);
                drawOpposite(x, y, width, height, pane, count);
                count++;
            }
        }
        drawMaze[0][0].removeWall(MazeGenerator.Direction.UP, pane);  // Remove top wall at start point
    }

    private void drawCell(int x, int y, int width, int height, Pane pane, int count) {
        if (this.drawMaze[x][y] == null) {
            System.err.println("CellRender at position (" + x + ", " + y + ") is not initialized.");
            return;
        }
        if(countValue2 == count && isEdgeCell(x,y)) {
            CellRender endpoint = drawMaze[x][y];
            endpoint.removeWallsForExitPoint(mazeWidth, mazeHeight, pane);
            getX = maze[x][y].getX();// Set the exit at the farthest edge point
            getY = maze[x][y].getY();
            matchEndPoint = getX * width + getY;
        }
        for (int i = 0; i < countValue.size(); i++) {
            if (count == countValue.get(i)) {  // Process only if first hasn't been found yet
                MazeGenerator.Direction dir = directions.get(i);
                drawMaze[x][y].removeWall(dir, pane);
            }
        }
    }



    private void drawOpposite(int x, int y, int width, int height, Pane pane, int count) {
        if (this.drawMaze[x][y] == null) {
            System.err.println("CellRender at position (" + x + ", " + y + ") is not initialized.");
            return;
        }
        if(countValue2 == count && isEdgeCell(x,y)) {
            CellRender endpoint = drawMaze[x][y];
            endpoint.removeWallsForExitPoint(mazeWidth, mazeHeight, pane);
            getX = maze[x][y].getX();// Set the exit at the farthest edge point
            getY = maze[x][y].getY();
            matchEndPoint = getX * width + getY;
        }
        for (int j = 0; j < countValue1.size(); j++) {
            if (count == countValue1.get(j)) {  // Process only if first has been found
                MazeGenerator.Direction dir = directions1.get(j);
                drawMaze[x][y].removeWall(dir, pane);
            }
        }
    }

    private boolean isEdgeCell(int x, int y) {
        return x == 0 || x == mazeWidth - 1 || y == 0 || y == mazeHeight - 1;
    }

    public MazeCell[][] getMaze(){
        return maze;
    }

    public CellRender[][] getMazeCell(){
        return drawMaze;
    }

    public void setSize(int width, int height) {
        this.mazeWidth = width;
        this.mazeHeight = height;
        this.maze = new MazeCell[mazeWidth][mazeHeight];// Reallocate the maze array with new dimensions // Initialize the maze with new dimensions
    }

    public List<MazeGenerator.Direction> getOpenDirections() {
        List<MazeGenerator.Direction> allDirections;
        allDirections = directions;
        return allDirections;
    }

    public List<Integer> getCountValue() {
        List<Integer> allValues;
        allValues = countValue;
        return allValues;
    }

    public List<MazeGenerator.Direction> getOpenDirections1() {
        List<MazeGenerator.Direction> allDirections;
        allDirections = directions1;
        return allDirections;
    }

    public List<Integer> getCountValue1() {
        List<Integer> allValues;
        allValues = countValue1;
        return allValues;
    }

    public int getCountValue2() {
        return countValue2;
    }
}
