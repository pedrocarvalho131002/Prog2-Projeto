import java.util.Random;

public class Sheep extends Animal {
    private static final int INIT_ENERGY = 10;
    private static final int MAX_AGE = 30;
    private static final int ENERGY_GAIN = 5;
    private static final Random rnd = new Random();

    public Sheep(World world, int x, int y) {
        super(world, x, y, INIT_ENERGY, MAX_AGE, -1);
    }

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
            if (o == null || o instanceof Plant) {
                if (o instanceof Plant) {
                    o.die();
                    energy += ENERGY_GAIN;
                }
                world.move(this, nx, ny);
            }
        }

        // Reprodução
        for (Organism o : world.getAt(x, y)) {
            if (o instanceof Sheep && o != this) {
                Sheep other = (Sheep) o;
                if (canReproduce(5, 20) && other.canReproduce(5, 20)
                        && rnd.nextDouble() < 0.30) {

                    int[] rd = dirs[rnd.nextInt(dirs.length)];
                    int rx = x + rd[0];
                    int ry = y + rd[1];

                    if (world.isInside(rx, ry) && world.isEmpty(rx, ry)) {
                        world.addOrganism(new Sheep(world, rx, ry));
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        return "O";
    }
}
