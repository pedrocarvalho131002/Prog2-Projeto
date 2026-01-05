import java.util.*;

public class World {
    private int width, height;
    private Organism[][] grid;
    private List<Organism> organisms = new ArrayList<>();

    public World(int w, int h) {
        width = w;
        height = h;
        grid = new Organism[w][h];
    }

    public boolean isInside(int x, int y) {
        return x >= 0 && y >= 0 && x < width && y < height;
    }

    public boolean isEmpty(int x, int y) {
        return grid[x][y] == null;
    }

    public Organism get(int x, int y) {
        return grid[x][y];
    }

    public List<Organism> getAt(int x, int y) {
        List<Organism> list = new ArrayList<>();
        if (grid[x][y] != null) list.add(grid[x][y]);
        return list;
    }

    public void addOrganism(Organism o) {
        organisms.add(o);
        grid[o.getX()][o.getY()] = o;
    }

    public void removeOrganism(Organism o) {
        grid[o.getX()][o.getY()] = null;
        organisms.remove(o);
    }

    public void move(Organism o, int nx, int ny) {
        grid[o.getX()][o.getY()] = null;
        o.x = nx;
        o.y = ny;
        grid[nx][ny] = o;
    }

    public void stepAll() {
        List<Organism> copy = new ArrayList<>(organisms);
        for (Organism o : copy) {
            if (o.isAlive()) o.step();
        }
    }

    public void print() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                System.out.print(grid[x][y] == null ? ". " : grid[x][y] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public boolean speciesAlive(Class<?> c) {
        return organisms.stream().anyMatch(o -> c.isInstance(o));
    }
}
