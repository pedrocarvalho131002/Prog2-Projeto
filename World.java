import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {

    private int width;
    private int height;
    private Organism[][] grid;
    private List<Organism> organisms;

    public World(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new Organism[height][width];
        this.organisms = new ArrayList<>();
    }

    public boolean isInside(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public boolean isEmpty(int x, int y) {
        return isInside(x, y) && grid[y][x] == null;
    }

    public boolean hasSheep() {
        for (Organism o : organisms) {
            if (o instanceof Sheep) return true;
        }
        return false;
    }

    public boolean hasWolves() {
        for (Organism o : organisms) {
            if (o instanceof Wolf) return true;
        }
        return false;
    }

    public Organism getOrganism(int x, int y) {
        if (!isInside(x, y)) return null;
        return grid[y][x];
    }

    public List<Organism> getOrganisms() {
        return new ArrayList<>(organisms);
    }

    public void addOrganism(Organism o) {
        if (isEmpty(o.getX(), o.getY())) {
            grid[o.getY()][o.getX()] = o;
            organisms.add(o);
        }
    }

    public void removeOrganism(Organism o) {
        grid[o.getY()][o.getX()] = null;
        organisms.remove(o);
    }

    public void moveOrganism(Organism o, int newX, int newY) {
        if (!isInside(newX, newY)) return;

        grid[o.getY()][o.getX()] = null;
        o.setPosition(newX, newY);
        grid[newY][newX] = o;
    }

    public void reproduceSheep() {
        List<Sheep> sheepList = new ArrayList<>();
        for (Organism o : organisms) {
            if (o instanceof Sheep) {
                sheepList.add((Sheep) o);
            }
        }

        Random rand = new Random();

        for (int i = 0; i < sheepList.size(); i++) {
            for (int j = i + 1; j < sheepList.size(); j++) {

                Sheep s1 = sheepList.get(i);
                Sheep s2 = sheepList.get(j);

                if (s1.getX() == s2.getX() && s1.getY() == s2.getY()) {

                    if (s1.getAge() >= Sheep.MIN_REPRO_AGE &&
                        s2.getAge() >= Sheep.MIN_REPRO_AGE &&
                        s1.getEnergy() >= Sheep.MIN_REPRO_ENERGY &&
                        s2.getEnergy() >= Sheep.MIN_REPRO_ENERGY &&
                        rand.nextDouble() < Sheep.REPRO_PROB) {

                        int[] dx = {-1, 1, 0, 0};
                        int[] dy = {0, 0, -1, 1};

                        for (int d = 0; d < 4; d++) {
                            int nx = s1.getX() + dx[d];
                            int ny = s1.getY() + dy[d];

                            if (isInside(nx, ny) && isEmpty(nx, ny)) {
                                addOrganism(new Sheep(this, nx, ny));
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    public void reproduceWolves() {
        List<Wolf> wolfList = new ArrayList<>();
        for (Organism o : organisms) {
            if (o instanceof Wolf) {
                wolfList.add((Wolf) o);
            }
        }

        Random rand = new Random();

        for (int i = 0; i < wolfList.size(); i++) {
            for (int j = i + 1; j < wolfList.size(); j++) {

                Wolf w1 = wolfList.get(i);
                Wolf w2 = wolfList.get(j);

                if (w1.getX() == w2.getX() && w1.getY() == w2.getY()) {

                    if (w1.getAge() >= Wolf.MIN_REPRO_AGE &&
                        w2.getAge() >= Wolf.MIN_REPRO_AGE &&
                        w1.getEnergy() >= Wolf.MIN_REPRO_ENERGY &&
                        w2.getEnergy() >= Wolf.MIN_REPRO_ENERGY &&
                        rand.nextDouble() < Wolf.REPRO_PROB) {

                        int[] dx = {-1, 1, 0, 0};
                        int[] dy = {0, 0, -1, 1};

                        for (int d = 0; d < 4; d++) {
                            int nx = w1.getX() + dx[d];
                            int ny = w1.getY() + dy[d];

                            if (isInside(nx, ny) && isEmpty(nx, ny)) {
                                addOrganism(new Wolf(this, nx, ny));
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    public void initializeRandomly() {
        Random rand = new Random();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                double r = rand.nextDouble();

                if (r < 0.04) {
                    addOrganism(new Wolf(this, x, y));
                } 
                else if (r < 0.16) {
                    addOrganism(new Sheep(this, x, y));
                } 
                else if (r < 0.91) {
                    addOrganism(new Plant(this, x, y));
                }
            }
        }
    }

    public void step() {
        List<Organism> current = new ArrayList<>(organisms);
        for (Organism o : current) {
            if (o.isAlive()) {
                o.act();
            }
        }

        reproduceSheep();
        reproduceWolves();

        List<Organism> toRemove = new ArrayList<>();
        for (Organism o : organisms) {
            if (!o.isAlive()) {
                toRemove.add(o);
            }
        }
        for (Organism o : toRemove) {
            removeOrganism(o);
        }
    }

    public void printWorld() {

        //superior
        System.out.print("+");
        for (int i = 0; i < width * 2 - 1; i++) {
            System.out.print("-");
        }
        System.out.println("+");

        //conteudo
        for (int y = 0; y < height; y++) {
            System.out.print("|");
            for (int x = 0; x < width; x++) {
                if (grid[y][x] == null) {
                    System.out.print(". ");
                } else {
                    System.out.print(grid[y][x].getSymbol() + " ");
                }
            }
            System.out.println("|");
        }

        //inferior
        System.out.print("+");
        for (int i = 0; i < width * 2 - 1; i++) {
            System.out.print("-");
        }
        System.out.println("+\n");
    }
}