import java.util.Random;

public class Plant extends Organism {

    private static final int MAX_AGE = 20;
    private static final double REPRODUCTION_PROB = 0.10;

    public Plant(World world, int x, int y) {
        super(world, x, y);
    }

    @Override
    public char getSymbol() {
        return '*';
    }

    @Override
    public void act() {
        increaseAge();

        if (age > MAX_AGE) {
            die();
            return;
        }

        reproduce();
    }

    private void reproduce() {
        Random rand = new Random();

        if (rand.nextDouble() > REPRODUCTION_PROB) return;

        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};

        int dir = rand.nextInt(4);
        int nx = x + dx[dir];
        int ny = y + dy[dir];

        if (world.isInside(nx, ny) && world.isEmpty(nx, ny)) {
            world.addOrganism(new Plant(world, nx, ny));
        }
    }
}
