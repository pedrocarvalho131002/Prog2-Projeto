import java.util.Random;

public class Sheep extends Animal {

    private static final int MAX_AGE = 30;
    private static final int INITIAL_ENERGY = 10;
    private static final int ENERGY_PER_STEP = -1;
    private static final int ENERGY_FROM_PLANT = 5;
    public static final int MIN_REPRO_AGE = 5;
    public static final int MIN_REPRO_ENERGY = 20;
    public static final double REPRO_PROB = 0.30;


    public Sheep(World world, int x, int y) {
        super(world, x, y, MAX_AGE, INITIAL_ENERGY, ENERGY_PER_STEP);
    }

    @Override
    public char getSymbol() {
        return 'O';
    }

    @Override
    public void act() {
        increaseAge();
        loseEnergy();

        if (!isAlive() || isTooOld()) {
            die();
            return;
        }

        move();
        eat();
    }

    private void move() {
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};

        Random rand = new Random();

        for (int i = 0; i < 4; i++) {
            int dir = rand.nextInt(4);
            int nx = x + dx[dir];
            int ny = y + dy[dir];

            if (!world.isInside(nx, ny)) continue;

            Organism target = world.getOrganism(nx, ny);

            if (target == null || target instanceof Plant) {
                world.moveOrganism(this, nx, ny);
                return;
            }
        }
    }

    private void eat() {
        Organism current = world.getOrganism(x, y);

        if (current instanceof Plant) {
            world.removeOrganism(current);
            energy += ENERGY_FROM_PLANT;
        }
    }
}