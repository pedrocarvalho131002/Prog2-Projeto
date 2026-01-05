import java.util.Random;

public class Wolf extends Animal {
    private static final int INIT_ENERGY = 15;
    private static final int MAX_AGE = 40;
    private static final int ENERGY_GAIN = 10;
    private static final Random rnd = new Random();

    public Wolf(World world, int x, int y) {
        super(world, x, y, INIT_ENERGY, MAX_AGE, -1);
    }

    //Ajuda do LLM m sheep.java adaptada para Wolf.java
    @Override
    public void step() {
        ageAndConsume();
        if (!alive) return;

        int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};
        int[] d = dirs[rnd.nextInt(dirs.length)];
        int nx = x + d[0];
        int ny = y + d[1];

        if (world.isInside(nx, ny)) {
            Organism o = world.get(nx, ny);
            if (o == null || o instanceof Sheep || o instanceof Plant) {
                if (o instanceof Sheep) {
                    o.die();
                    energy += ENERGY_GAIN;
                }
                if (!(o instanceof Wolf)) {
                    world.move(this, nx, ny);
                }
            }
        }

        // Reprodução
        for (Organism o : world.getAt(x, y)) {
            if (o instanceof Wolf && o != this) {
                Wolf other = (Wolf) o;
                if (canReproduce(5, 20) && other.canReproduce(5, 20)
                        && rnd.nextDouble() < 0.25) {

                    int[] rd = dirs[rnd.nextInt(dirs.length)];
                    int rx = x + rd[0];
                    int ry = y + rd[1];

                    if (world.isInside(rx, ry) && world.isEmpty(rx, ry)) {
                        world.addOrganism(new Wolf(world, rx, ry));
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        return "W";
    }
}
