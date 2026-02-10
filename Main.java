import java.util.Scanner;

public class Main {

    private static int readInt(Scanner sc, String msg, int min, int max) {
        while (true) {
            System.out.print(msg);
            String line = sc.nextLine().trim();
            try {
                int v = Integer.parseInt(line);
                if (v < min || v > max) {
                    System.out.println("Valor invalido.");
                } else {
                    return v;
                }
            } catch (Exception e) {
                System.out.println("Valor invalido.");
            }
        }
    }

    private static double readDouble01(Scanner sc, String msg) {
        while (true) {
            System.out.print(msg);
            String line = sc.nextLine().trim();
            line = line.replace(',', '.'); // aceita virgula sem “avisos”
            try {
                double v = Double.parseDouble(line);
                if (v < 0.0 || v > 1.0) {
                    System.out.println("Valor invalido.");
                } else {
                    return v;
                }
            } catch (Exception e) {
                System.out.println("Valor invalido.");
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int width = 20;
        int height = 20;

        double pWolf = 0.04;
        double pSheep = 0.12;
        double pPlant = 0.75;

        System.out.println("Simulacao de ecossistema (Plantas, Ovelhas, Lobos)");
        System.out.println("1) Usar valores pre-definidos (20x20, pW=0.04, pO=0.12, pP=0.75)");
        System.out.println("2) Definir manualmente");
        int optInit = readInt(sc, "Opcao: ", 1, 2);

        if (optInit == 2) {
            width = readInt(sc, "Largura da grelha: ", 1, 200);
            height = readInt(sc, "Altura da grelha: ", 1, 200);

            while (true) {
                pWolf = readDouble01(sc, "Probabilidade inicial de Lobos (0..1): ");
                pSheep = readDouble01(sc, "Probabilidade inicial de Ovelhas (0..1): ");
                pPlant = readDouble01(sc, "Probabilidade inicial de Plantas (0..1): ");

                if (pWolf + pSheep + pPlant <= 1.0) {
                    break;
                }
                System.out.println("A soma das probabilidades tem de ser <= 1.0.");
            }
        }

        World world = new World(width, height);
        world.initialize(pWolf, pSheep, pPlant);

        System.out.println();
        world.print();
        world.printCounts();

        int stepCounter = 0;

        while (true) {
            System.out.println();
            System.out.println("Menu:");
            System.out.println("1) Executar 1 passo");
            System.out.println("2) Executar N passos");
            System.out.println("3) Executar ate extincao de uma especie");
            System.out.println("4) Mostrar estatisticas");
            System.out.println("5) Exportar evolucao para CSV");
            System.out.println("0) Sair");

            int op = readInt(sc, "Opcao: ", 0, 5);

            if (op == 0) break;

            if (op == 1) {
                stepCounter++;
                world.step();
                System.out.println("\nPasso " + stepCounter);
                world.print();
                world.printCounts();

            } else if (op == 2) {
                int n = readInt(sc, "N passos: ", 1, 100000);

                for (int i = 0; i < n; i++) {
                    stepCounter++;
                    world.step();
                    System.out.println("\nPasso " + stepCounter);
                    world.print();
                    world.printCounts();

                    if (!world.hasPlants() || !world.hasSheep() || !world.hasWolves()) {
                        System.out.println("Parou por extincao de uma especie.");
                        break;
                    }
                }

            } else if (op == 3) {
                while (world.hasPlants() && world.hasSheep() && world.hasWolves()) {
                    stepCounter++;
                    world.step();
                    System.out.println("\nPasso " + stepCounter);
                    world.print();
                    world.printCounts();
                }
                System.out.println("Parou por extincao de uma especie.");

            } else if (op == 4) {
                world.showStats();

            } else if (op == 5) {
                System.out.print("Nome do ficheiro (ex.: stats.csv): ");
                String name = sc.nextLine().trim();
                if (name.length() == 0) name = "stats.csv";
                world.exportStatsCSV(name);
            }
        }

        System.out.println("Fim.");
        sc.close();
    }
}
