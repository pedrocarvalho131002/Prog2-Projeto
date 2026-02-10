public class Plant extends Organism {
    public static final int TYPE_ID = 1;

    public static final int MAX_AGE = 20;
    public static final double REPRO_PROB = 0.10;

    public Plant(World world, int x, int y) {
        super(world, x, y, MAX_AGE);
    }

    @Override
    public char getSymbol() {
        return '*';
    }

    @Override
    public int getTypeId() {
        return TYPE_ID;
    }
}
