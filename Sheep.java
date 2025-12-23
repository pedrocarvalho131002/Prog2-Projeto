
public class Sheep extends Animal {

    private static final int MAX_AGE = 30;
    private static final int INITIAL_ENERGY = 10;
    private static final int ENERGY_PER_STEP = -1;
    private static final int ENERGY_FROM_PLANT = 5;

    private static final int MIN_REPRO_AGE = 5;
    private static final int MIN_REPRO_ENERGY = 20;
    private static final double REPRO_PROB = 0.30;

    public Sheep(int x, int y) {
        super(x, y, MAX_AGE, INITIAL_ENERGY, ENERGY_PER_STEP);
    }

    @Override
    public char getSymbol() {
        return 'O';
    }

    @Override
    public void move(World world) {
        
    }

    @Override
    public void tryReproduce(World world) {
        
    }
}