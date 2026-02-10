import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Stats {
    private int createdPlants, createdSheep, createdWolves;
    private int deadPlants, deadSheep, deadWolves;

    private ArrayList<Integer> plantsHistory = new ArrayList<Integer>();
    private ArrayList<Integer> sheepHistory  = new ArrayList<Integer>();
    private ArrayList<Integer> wolvesHistory = new ArrayList<Integer>();

    private int maxPlants, stepMaxPlants;
    private int maxSheep, stepMaxSheep;
    private int maxWolves, stepMaxWolves;

    private int extinctStep;
    private String extinctName;

    public void reset() {
        createdPlants = 0;
        createdSheep = 0;
        createdWolves = 0;

        deadPlants = 0;
        deadSheep = 0;
        deadWolves = 0;

        plantsHistory.clear();
        sheepHistory.clear();
        wolvesHistory.clear();

        maxPlants = -1; stepMaxPlants = 0;
        maxSheep = -1;  stepMaxSheep  = 0;
        maxWolves = -1; stepMaxWolves = 0;

        extinctStep = -1;
        extinctName = null;
    }

    public void onCreated(Organism o) {
        if (o.getTypeId() == Plant.TYPE_ID) createdPlants++;
        else if (o.getTypeId() == Sheep.TYPE_ID) createdSheep++;
        else if (o.getTypeId() == Wolf.TYPE_ID) createdWolves++;
    }

    public void onDead(Organism o) {
        if (o.getTypeId() == Plant.TYPE_ID) deadPlants++;
        else if (o.getTypeId() == Sheep.TYPE_ID) deadSheep++;
        else if (o.getTypeId() == Wolf.TYPE_ID) deadWolves++;
    }

    // step=0 no estado inicial, depois 1..N
    public void recordStep(int step, int plants, int sheep, int wolves) {
        plantsHistory.add(plants);
        sheepHistory.add(sheep);
        wolvesHistory.add(wolves);

        if (plants > maxPlants) { maxPlants = plants; stepMaxPlants = step; }
        if (sheep > maxSheep)   { maxSheep = sheep;  stepMaxSheep  = step; }
        if (wolves > maxWolves) { maxWolves = wolves; stepMaxWolves = step; }

        if (extinctStep == -1) {
            if (plants == 0) { extinctStep = step; extinctName = "Plantas"; }
            else if (sheep == 0) { extinctStep = step; extinctName = "Ovelhas"; }
            else if (wolves == 0) { extinctStep = step; extinctName = "Lobos"; }
        }
    }

    public void printSummary(int currentStep, int plantsAlive, int sheepAlive, int wolvesAlive,
                             int emptyCells, int totalCells) {

        System.out.println("=== Estatisticas ===");
        System.out.println("Passo atual: " + currentStep);

        System.out.println("Vivos agora: Plantas=" + plantsAlive + " Ovelhas=" + sheepAlive + " Lobos=" + wolvesAlive);
        System.out.println("Vazios: " + emptyCells + " / " + totalCells);

        System.out.println("Criados: Plantas=" + createdPlants + " Ovelhas=" + createdSheep + " Lobos=" + createdWolves);
        System.out.println("Mortos:  Plantas=" + deadPlants + " Ovelhas=" + deadSheep + " Lobos=" + deadWolves);

        System.out.println("Maximos:");
        System.out.println("  Plantas=" + maxPlants + " (passo " + stepMaxPlants + ")");
        System.out.println("  Ovelhas=" + maxSheep + " (passo " + stepMaxSheep + ")");
        System.out.println("  Lobos=" + maxWolves + " (passo " + stepMaxWolves + ")");

        if (extinctStep != -1) {
            System.out.println("Extincao: " + extinctName + " (passo " + extinctStep + ")");
        } else {
            System.out.println("Extincao: nao ocorreu");
        }

        System.out.println("Ultimos registos:");
        int n = plantsHistory.size();
        int start = n - 5;
        if (start < 0) start = 0;

        for (int i = start; i < n; i++) {
            System.out.println("  passo " + i + " -> P:" + plantsHistory.get(i)
                    + " O:" + sheepHistory.get(i)
                    + " L:" + wolvesHistory.get(i));
        }

        System.out.println("====================");
    }

    public void exportCSV(String filename) throws IOException {
        PrintWriter out = new PrintWriter(new FileWriter(filename));
        out.println("step,plants,sheep,wolves,total");

        int steps = plantsHistory.size();
        for (int i = 0; i < steps; i++) {
            int p = plantsHistory.get(i);
            int s = sheepHistory.get(i);
            int w = wolvesHistory.get(i);
            int total = p + s + w;
            out.println(i + "," + p + "," + s + "," + w + "," + total);
        }

        out.close();
    }
}
