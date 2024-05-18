package maze;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import javafx.scene.layout.Pane;

public class MazeCell implements Serializable{
    @Serial
    private static final long serialVersionUID = 1L;
    public int x, y, size;
    public Set<MazeGenerator.Direction> openDirections = new LinkedHashSet<>();
    public Set<MazeGenerator.Direction> oppositeDirections = new HashSet<>();

    transient CellRender cellRenderer;

    // Constructor and getters/setters
    public MazeCell(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
    }

    public CellRender renderCell(Pane pane) {
        if (cellRenderer == null) {
            cellRenderer = new CellRender(this.x, this.y, this.size, pane);
        }
        return cellRenderer;
    }


    public void addOpenDirection(MazeGenerator.Direction direction) {
        openDirections.add(direction);
    }


    public Set<MazeGenerator.Direction> getOpenDirections() {
        return new LinkedHashSet<>(openDirections);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
