public class Sheep extends Animal {
    public static final int TYPE_ID = 2;

    public static final int INIT_ENERGY = 10;
    public static final int MAX_AGE = 30;

    public static final int ENERGY_GAIN_PLANT = 5;

    public static final int REPRO_MIN_AGE = 5;
    public static final int REPRO_MIN_ENERGY = 20;
    public static final double REPRO_PROB = 0.30;

    public Sheep(World world, int x, int y) {
        super(world, x, y, INIT_ENERGY, MAX_AGE, -1);
    }

    public void eatPlant() {
        energy += ENERGY_GAIN_PLANT;
    }

    @Override
    public char getSymbol() {
        return 'O';
    }

    @Override
    public int getTypeId() {
        return TYPE_ID;
    }
}
