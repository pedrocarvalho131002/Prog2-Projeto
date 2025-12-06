
public class Plant extends Organism {

    private static final int DEFAULT_MAX_AGE = 20;
    private static final double REPRODUCTION_PROB = 0.10;

    public Plant(int x, int y) {
        super(x, y, DEFAULT_MAX_AGE);
    }

    @Override
    public char getSymbol() {
        return 'P';
    }
}
