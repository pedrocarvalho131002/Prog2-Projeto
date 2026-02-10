import java.util.ArrayList;
import java.util.Random;

public class World {
    private int width, height;
    private Organism[][] grid;
    private ArrayList<Organism> organisms;

    private Random rnd;
    private Stats stats;

    private int stepCounter;

    // Encontros (quando >=2 escolhem a mesma celula)
    private ArrayList<SheepMeet> sheepMeets;
    private ArrayList<WolfMeet> wolfMeets;

    // Direcoes N,S,E,O
    private static final int[] DX = {0, 0, 1, -1};
    private static final int[] DY = {1, -1, 0, 0};

    private static class SheepMeet {
        public int x, y;
        public Sheep a;
        public Sheep b;

        public SheepMeet(int x, int y, Sheep a, Sheep b) {
            this.x = x;
            this.y = y;
            this.a = a;
            this.b = b;
        }
    }

    private static class WolfMeet {
        public int x, y;
        public Wolf a;
        public Wolf b;

        public WolfMeet(int x, int y, Wolf a, Wolf b) {
            this.x = x;
            this.y = y;
            this.a = a;
            this.b = b;
        }
    }

    public World(int w, int h) {
        width = w;
        height = h;
        grid = new Organism[w][h];
        organisms = new ArrayList<Organism>();
        rnd = new Random();
        stats = new Stats();
        sheepMeets = new ArrayList<SheepMeet>();
        wolfMeets = new ArrayList<WolfMeet>();
        stepCounter = 0;
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public boolean isInside(int x, int y) {
        return x >= 0 && y >= 0 && x < width && y < height;
    }

    public Organism get(int x, int y) {
        return grid[x][y];
    }

    public boolean isEmpty(int x, int y) {
        return grid[x][y] == null;
    }

    public void addOrganism(Organism o) {
        organisms.add(o);
        grid[o.getX()][o.getY()] = o;
        stats.onCreated(o);
    }

    private void removeOrganism(Organism o) {
        if (isInside(o.getX(), o.getY())) {
            if (grid[o.getX()][o.getY()] == o) {
                grid[o.getX()][o.getY()] = null;
            }
        }
        organisms.remove(o);
        stats.onDead(o);
    }

    public void initialize(double pWolf, double pSheep, double pPlant) {
        organisms.clear();
        grid = new Organism[width][height];
        stats.reset();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double r = rnd.nextDouble();

                if (r < pWolf) {
                    addOrganism(new Wolf(this, x, y));
                } else if (r < pWolf + pSheep) {
                    addOrganism(new Sheep(this, x, y));
                } else if (r < pWolf + pSheep + pPlant) {
                    addOrganism(new Plant(this, x, y));
                } else {
                    // vazio
                }
            }
        }

        stepCounter = 0;
        stats.recordStep(stepCounter, countPlants(), countSheep(), countWolves());
    }

    // PASSO: envelhecimento -> movimento -> alimentacao -> reproducao -> remocao mortos
    public void step() {
        sheepMeets.clear();
        wolfMeets.clear();

        // 1) Envelhecimento (e energia nos animais)
        for (int i = 0; i < organisms.size(); i++) {
            Organism o = organisms.get(i);
            if (o.isAlive()) {
                o.ageOneStep();
            }
        }

        // 2) Movimento ovelhas + alimentacao (se cair em planta)
        moveSheepAndFeed();

        // 3) Movimento lobos + alimentacao (se cair em ovelha)
        moveWolvesAndFeed();

        // 4) Reproducao
        reproducePlants();  // 1 tentativa aleatoria
        reproduceSheep();   // encontro por destino comum
        reproduceWolves();  // encontro por destino comum

        // 5) Remover mortos
        cleanupDead();

        stepCounter = stepCounter + 1;
        stats.recordStep(stepCounter, countPlants(), countSheep(), countWolves());
    }

    // Movimento rigoroso: 1 direcao aleatoria.
    // Ovelha pode ir para vazio ou planta.
    private void moveSheepAndFeed() {
        ArrayList<Sheep> sheepList = new ArrayList<Sheep>();
        for (int i = 0; i < organisms.size(); i++) {
            Organism o = organisms.get(i);
            if (o.isAlive() && o.getTypeId() == Sheep.TYPE_ID) {
                sheepList.add((Sheep) o);
            }
        }

        int n = sheepList.size();
        int[] targetX = new int[n];
        int[] targetY = new int[n];
        Organism[] targetOcc = new Organism[n];

        // Planear destinos com base na grelha atual
        for (int i = 0; i < n; i++) {
            Sheep s = sheepList.get(i);

            int cx = s.getX();
            int cy = s.getY();

            int d = rnd.nextInt(4);
            int nx = cx + DX[d];
            int ny = cy + DY[d];

            if (!isInside(nx, ny)) {
                targetX[i] = cx;
                targetY[i] = cy;
                targetOcc[i] = null;
            } else {
                Organism occ = get(nx, ny);
                if (occ == null || occ.getTypeId() == Plant.TYPE_ID) {
                    targetX[i] = nx;
                    targetY[i] = ny;
                    targetOcc[i] = occ; // Plant ou null
                } else {
                    targetX[i] = cx;
                    targetY[i] = cy;
                    targetOcc[i] = null;
                }
            }
        }

        // Contar quantas querem cada destino
        int[][] count = new int[width][height];
        for (int i = 0; i < n; i++) {
            count[targetX[i]][targetY[i]]++;
        }

        boolean[] moveAllowed = new boolean[n];
        for (int i = 0; i < n; i++) {
            moveAllowed[i] = false;
        }

        // Resolver colisões
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (count[x][y] == 1) {
                    for (int i = 0; i < n; i++) {
                        if (targetX[i] == x && targetY[i] == y) {
                            moveAllowed[i] = true;
                            break;
                        }
                    }
                } else if (count[x][y] >= 2) {
                    int[] idx = new int[count[x][y]];
                    int k = 0;
                    for (int i = 0; i < n; i++) {
                        if (targetX[i] == x && targetY[i] == y) {
                            idx[k] = i;
                            k++;
                        }
                    }

                    int winner = idx[rnd.nextInt(idx.length)];
                    moveAllowed[winner] = true;

                    int other = idx[0];
                    if (other == winner && idx.length > 1) other = idx[1];

                    Sheep a = sheepList.get(winner);
                    Sheep b = sheepList.get(other);
                    sheepMeets.add(new SheepMeet(x, y, a, b));
                }
            }
        }

        // Limpar posições antigas
        for (int i = 0; i < n; i++) {
            Sheep s = sheepList.get(i);
            if (grid[s.getX()][s.getY()] == s) {
                grid[s.getX()][s.getY()] = null;
            }
        }

        // Aplicar movimentos e alimentação
        for (int i = 0; i < n; i++) {
            Sheep s = sheepList.get(i);

            if (moveAllowed[i]) {
                s.x = targetX[i];
                s.y = targetY[i];
            }

            if (moveAllowed[i] && targetOcc[i] != null && targetOcc[i].getTypeId() == Plant.TYPE_ID) {
                targetOcc[i].die();
                s.eatPlant();
            }

            grid[s.getX()][s.getY()] = s;
        }
    }

    // Movimento rigoroso: 1 direcao aleatoria.
    // Lobo pode ir para vazio ou ovelha (se ovelha, come).
    // Plantas e lobos bloqueiam.
    private void moveWolvesAndFeed() {
        ArrayList<Wolf> wolfList = new ArrayList<Wolf>();
        for (int i = 0; i < organisms.size(); i++) {
            Organism o = organisms.get(i);
            if (o.isAlive() && o.getTypeId() == Wolf.TYPE_ID) {
                wolfList.add((Wolf) o);
            }
        }

        int n = wolfList.size();
        int[] targetX = new int[n];
        int[] targetY = new int[n];
        Organism[] targetOcc = new Organism[n];

        // Planear destinos com base na grelha após movimento das ovelhas
        for (int i = 0; i < n; i++) {
            Wolf w = wolfList.get(i);

            int cx = w.getX();
            int cy = w.getY();

            int d = rnd.nextInt(4);
            int nx = cx + DX[d];
            int ny = cy + DY[d];

            if (!isInside(nx, ny)) {
                targetX[i] = cx;
                targetY[i] = cy;
                targetOcc[i] = null;
            } else {
                Organism occ = get(nx, ny);

                if (occ == null || occ.getTypeId() == Sheep.TYPE_ID) {
                    targetX[i] = nx;
                    targetY[i] = ny;
                    targetOcc[i] = occ; // Sheep ou null
                } else {
                    targetX[i] = cx;
                    targetY[i] = cy;
                    targetOcc[i] = null;
                }
            }
        }

        int[][] count = new int[width][height];
        for (int i = 0; i < n; i++) {
            count[targetX[i]][targetY[i]]++;
        }

        boolean[] moveAllowed = new boolean[n];
        for (int i = 0; i < n; i++) {
            moveAllowed[i] = false;
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (count[x][y] == 1) {
                    for (int i = 0; i < n; i++) {
                        if (targetX[i] == x && targetY[i] == y) {
                            moveAllowed[i] = true;
                            break;
                        }
                    }
                } else if (count[x][y] >= 2) {
                    int[] idx = new int[count[x][y]];
                    int k = 0;
                    for (int i = 0; i < n; i++) {
                        if (targetX[i] == x && targetY[i] == y) {
                            idx[k] = i;
                            k++;
                        }
                    }

                    int winner = idx[rnd.nextInt(idx.length)];
                    moveAllowed[winner] = true;

                    int other = idx[0];
                    if (other == winner && idx.length > 1) other = idx[1];

                    Wolf a = wolfList.get(winner);
                    Wolf b = wolfList.get(other);
                    wolfMeets.add(new WolfMeet(x, y, a, b));
                }
            }
        }

        // Limpar posições antigas
        for (int i = 0; i < n; i++) {
            Wolf w = wolfList.get(i);
            if (grid[w.getX()][w.getY()] == w) {
                grid[w.getX()][w.getY()] = null;
            }
        }

        // Aplicar movimentos e alimentação
        for (int i = 0; i < n; i++) {
            Wolf w = wolfList.get(i);

            if (moveAllowed[i]) {
                w.x = targetX[i];
                w.y = targetY[i];
            }

            if (moveAllowed[i] && targetOcc[i] != null && targetOcc[i].getTypeId() == Sheep.TYPE_ID) {
                targetOcc[i].die();
                w.eatSheep();
            }

            grid[w.getX()][w.getY()] = w;
        }
    }

    // Plantas: se reproduzirem, escolhem 1 direcao aleatoria e tentam (1 tentativa)
    private void reproducePlants() {
        ArrayList<Organism> copy = new ArrayList<Organism>(organisms);

        for (int i = 0; i < copy.size(); i++) {
            Organism o = copy.get(i);
            if (!o.isAlive()) continue;
            if (o.getTypeId() != Plant.TYPE_ID) continue;

            if (rnd.nextDouble() < Plant.REPRO_PROB) {
                int d = rnd.nextInt(4);
                int nx = o.getX() + DX[d];
                int ny = o.getY() + DY[d];

                if (isInside(nx, ny) && isEmpty(nx, ny)) {
                    addOrganism(new Plant(this, nx, ny));
                }
            }
        }
    }

    // Reproducao por “encontro” (destino comum no movimento)
    private void reproduceSheep() {
        for (int i = 0; i < sheepMeets.size(); i++) {
            SheepMeet m = sheepMeets.get(i);
            Sheep a = m.a;
            Sheep b = m.b;

            if (!a.isAlive() || !b.isAlive()) continue;

            if (a.canReproduce(Sheep.REPRO_MIN_AGE, Sheep.REPRO_MIN_ENERGY) &&
                    b.canReproduce(Sheep.REPRO_MIN_AGE, Sheep.REPRO_MIN_ENERGY)) {

                if (rnd.nextDouble() < Sheep.REPRO_PROB) {
                    spawnSheepNear(m.x, m.y);
                }
            }
        }
    }

    private void spawnSheepNear(int x, int y) {
        int[] candX = new int[4];
        int[] candY = new int[4];
        int ccount = 0;

        for (int d = 0; d < 4; d++) {
            int nx = x + DX[d];
            int ny = y + DY[d];
            if (isInside(nx, ny) && isEmpty(nx, ny)) {
                candX[ccount] = nx;
                candY[ccount] = ny;
                ccount++;
            }
        }

        if (ccount == 0) return;

        int pick = rnd.nextInt(ccount);
        addOrganism(new Sheep(this, candX[pick], candY[pick]));
    }

    private void reproduceWolves() {
        for (int i = 0; i < wolfMeets.size(); i++) {
            WolfMeet m = wolfMeets.get(i);
            Wolf a = m.a;
            Wolf b = m.b;

            if (!a.isAlive() || !b.isAlive()) continue;

            if (a.canReproduce(Wolf.REPRO_MIN_AGE, Wolf.REPRO_MIN_ENERGY) &&
                    b.canReproduce(Wolf.REPRO_MIN_AGE, Wolf.REPRO_MIN_ENERGY)) {

                if (rnd.nextDouble() < Wolf.REPRO_PROB) {
                    spawnWolfNear(m.x, m.y);
                }
            }
        }
    }

    private void spawnWolfNear(int x, int y) {
        int[] candX = new int[4];
        int[] candY = new int[4];
        int ccount = 0;

        for (int d = 0; d < 4; d++) {
            int nx = x + DX[d];
            int ny = y + DY[d];
            if (isInside(nx, ny) && isEmpty(nx, ny)) {
                candX[ccount] = nx;
                candY[ccount] = ny;
                ccount++;
            }
        }

        if (ccount == 0) return;

        int pick = rnd.nextInt(ccount);
        addOrganism(new Wolf(this, candX[pick], candY[pick]));
    }

    private void cleanupDead() {
        int i = 0;
        while (i < organisms.size()) {
            Organism o = organisms.get(i);
            if (!o.isAlive()) {
                removeOrganism(o);
            } else {
                i++;
            }
        }
    }

    public void print() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Organism o = grid[x][y];
                char c = (o == null) ? '.' : o.getSymbol();
                System.out.print(c);
                System.out.print(' ');
            }
            System.out.println();
        }
    }

    public int countEmptyCells() {
        int empty = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (grid[x][y] == null) empty++;
            }
        }
        return empty;
    }

    public int countPlants() {
        int c = 0;
        for (int i = 0; i < organisms.size(); i++) {
            Organism o = organisms.get(i);
            if (o.isAlive() && o.getTypeId() == Plant.TYPE_ID) c++;
        }
        return c;
    }

    public int countSheep() {
        int c = 0;
        for (int i = 0; i < organisms.size(); i++) {
            Organism o = organisms.get(i);
            if (o.isAlive() && o.getTypeId() == Sheep.TYPE_ID) c++;
        }
        return c;
    }

    public int countWolves() {
        int c = 0;
        for (int i = 0; i < organisms.size(); i++) {
            Organism o = organisms.get(i);
            if (o.isAlive() && o.getTypeId() == Wolf.TYPE_ID) c++;
        }
        return c;
    }

    public boolean hasPlants() { return countPlants() > 0; }
    public boolean hasSheep()  { return countSheep() > 0; }
    public boolean hasWolves() { return countWolves() > 0; }

    public void printCounts() {
        System.out.println("Plantas: " + countPlants() + " | Ovelhas: " + countSheep() + " | Lobos: " + countWolves());
    }

    public void showStats() {
        int totalCells = width * height;
        stats.printSummary(stepCounter, countPlants(), countSheep(), countWolves(), countEmptyCells(), totalCells);
    }

    public void exportStatsCSV(String filename) {
        try {
            stats.exportCSV(filename);
            System.out.println("CSV exportado para: " + filename);
        } catch (Exception e) {
            System.out.println("Erro ao exportar CSV: " + e.getMessage());
        }
    }
}
