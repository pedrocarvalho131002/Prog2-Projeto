
public class World {

    private int width;
    private int height;
    private Organism[][] grid;

    public World(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new Organism[height][width];
    }

    public void setAt(int x, int y, Organism o) {
        grid[y][x] = o;
    }

    public void removeOrganismAt(int x, int y) {
        grid[y][x] = null;
    }

}
