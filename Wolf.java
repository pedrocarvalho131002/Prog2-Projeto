public class Wolf extends Animal {
    public static final int TYPE_ID = 3;

    public static final int INIT_ENERGY = 15;
    public static final int MAX_AGE = 40;

    public static final int ENERGY_GAIN_SHEEP = 10;

    public static final int REPRO_MIN_AGE = 5;
    public static final int REPRO_MIN_ENERGY = 20;
    public static final double REPRO_PROB = 0.25;

    public Wolf(World world, int x, int y) {
        super(world, x, y, INIT_ENERGY, MAX_AGE, -1);
    }

    public void eatSheep() {
        energy += ENERGY_GAIN_SHEEP;
    }

    @Override
    public char getSymbol() {
        return 'W';
    }

    @Override
    public int getTypeId() {
        return TYPE_ID;
    }
}
