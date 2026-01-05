import java.util.Random;

public class Plant extends Organism {
    private static final int MAX_AGE = 20;
    private static final double REPRO_PROB = 0.10;
    private static final Random rnd = new Random();

    public Plant(World world, int x, int y) {
        super(world, x, y);
    }

    //ajuda LLM
    @Override
    public void step() {
        age++;
        if (age > MAX_AGE) {
            die();
            return;
        }

        if (rnd.nextDouble() < REPRO_PROB) {
            int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};
            int[] d = dirs[rnd.nextInt(dirs.length)];
            int nx = x + d[0];
            int ny = y + d[1];

            if (world.isInside(nx, ny) && world.isEmpty(nx, ny)) {
                world.addOrganism(new Plant(world, nx, ny));
            }
        }
    }

    @Override
    public String toString() {
        return "*";
    }
}
