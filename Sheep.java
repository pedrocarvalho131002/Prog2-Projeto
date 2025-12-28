
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

        int cx = this.x;
        int cy = this.y;

        // 4 direções (N, S, W, E) embaralhadas
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};

        // baralhar índices 0..3
        int[] idx = {0, 1, 2, 3};
        for (int k = 3; k > 0; k--) {
            int r = world.randInt(k + 1);
            int tmp = idx[k];
            idx[k] = idx[r];
            idx[r] = tmp;
        }

        // 1) tentar comer planta primeiro
        for (int t = 0; t < 4; t++) {
            int k = idx[t];
            int nx = cx + dx[k];
            int ny = cy + dy[k];

            if (!world.inside(nx, ny)) continue;
            if (!world.isNextEmpty(nx, ny)) continue;

            Organism target = world.getAt(nx, ny);
            if (target instanceof Plant) {
                // come a planta
                this.addEnergy(ENERGY_FROM_PLANT);
                this.setPosition(nx, ny);
                world.placeInNext(nx, ny, this);
                return;
            }
        }

        // 2) senão, tenta mover para célula vazia
        for (int t = 0; t < 4; t++) {
            int k = idx[t];
            int nx = cx + dx[k];
            int ny = cy + dy[k];

            if (!world.inside(nx, ny)) continue;
            if (!world.isNextEmpty(nx, ny)) continue;

            Organism target = world.getAt(nx, ny);
            if (target == null) {
                this.setPosition(nx, ny);
                world.placeInNext(nx, ny, this);
                return;
            }
        }

        // 3) se não conseguiu, fica onde está (se ainda estiver livre)
        if (world.isNextEmpty(cx, cy)) {
            this.setPosition(cx, cy);
            world.placeInNext(cx, cy, this);
        }
    }


    @Override
    public void tryReproduce(World world) {
        
    }
}