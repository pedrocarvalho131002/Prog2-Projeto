import java.util.Random;



public class World {
    private int rows;
    private int cols;
    private Organism[][] grid;
    private Organism[][] nextGrid;


    private Random random = new Random();

    public World(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new Organism[rows][cols];
    }

    //Esta parte serve para validar limites, ler as células, saber se a grelha está vazia e imprimir a grelha
    public boolean inside(int x, int y) {
        return x >= 0 && x < rows && y >= 0 && y < cols;
    }

    public Organism getAt(int x, int y) {
        if (!inside(x, y)) return null;
        return grid[x][y];
    }

    public boolean isEmpty(int x, int y) {
        return inside(x, y) && grid[x][y] == null;
    }

    public void setAt(int x, int y, Organism org) {
        if (!inside(x, y)) return;
        grid[x][y] = org;
    }

    public void removeOrganismAt(int x, int y) {
        if (!inside(x, y)) return;
        grid[x][y] = null;
    }

    public void print() {

        System.out.print("+");
        for (int j = 0; j < cols; j++) System.out.print("--");
        System.out.println("+");

        for (int i = 0; i < rows; i++) {
            System.out.print("|");
            for (int j = 0; j < cols; j++) {
                Organism o = grid[i][j];
                char c = (o == null) ? '.' : o.getSymbol();
                System.out.print(c + " ");
            }
            System.out.println("|");
        }


        System.out.print("+");
        for (int j = 0; j < cols; j++) System.out.print("--");
        System.out.println("+");
    }


    public int countPlants() {
        int c = 0;
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                if (grid[i][j] instanceof Plant) c++;
        return c;
    }

    public int countSheep() {
        int c = 0;
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                if (grid[i][j] instanceof Sheep) c++;
        return c;
    }

    public int countWolves() {
        int c = 0;
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                if (grid[i][j] instanceof Wolf) c++;
        return c;
    }

    //Esta função serve para inicializar o mundo, cada célula é preenchida conforme a prob. ou fica vazia
    public void initialize(double pWolf, double pSheep, double pPlant) {

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                double r = random.nextDouble();

                if (r < pWolf) {
                    grid[i][j] = new Wolf(i, j);
                }
                else if (r < pWolf + pSheep) {
                    grid[i][j] = new Sheep(i, j);
                }
                else if (r < pWolf + pSheep + pPlant) {
                    grid[i][j] = new Plant(i, j);
                }
                else {
                    grid[i][j] = null;
                }
            }
        }
    }

    public void step() {

        // Grelha temporária para construir o próximo estado
        nextGrid = new Organism[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                Organism o = grid[i][j];
                if (o == null) continue;

                // envelhecer
                o.incrementAge();
                if (!o.isAlive()) continue;

                // energia (só para animais)
                if (o instanceof Animal) {
                    Animal a = (Animal) o;
                    a.consumeEnergy();
                    if (!a.isAlive()) continue;

                    // movimento + alimentação (cada animal decide)
                    a.move(this);
                } else {
                    // Planta fica no mesmo sítio (por agora)
                    o.setPosition(i, j);
                    placeInNext(i, j, o);
                }
            }
        }

        // Troca do estado
        grid = nextGrid;
        nextGrid = null;
    }



    public int randInt(int bound) {
        return random.nextInt(bound);
    }

    public double randDouble() {
        return random.nextDouble();
    }


    public boolean isNextEmpty(int x, int y) {
        return nextGrid != null && inside(x, y) && nextGrid[x][y] == null;
    }

    public void placeInNext(int x, int y, Organism o) {
        if (nextGrid == null) return;
        if (!inside(x, y)) return;
        nextGrid[x][y] = o;
    }



}
