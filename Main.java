import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        World world = new World(20, 20);
        world.initializeRandomly();

        boolean running = true;
        int step = 0;

        while (running) {
            System.out.println("\n===== SIMULACAO ECOSSISTEMA =====");
            System.out.println("1 - Executar passo a passo");
            System.out.println("2 - Executar N passos");
            System.out.println("3 - Executar ate extincao de uma especie");
            System.out.println("0 - Sair");
            System.out.print("Opcao: ");

            int option = scanner.nextInt();

            switch (option) {

                case 1:
                    System.out.println("\nModo passo a passo (ENTER para continuar, 0 para sair)");
                    scanner.nextLine();

                    while (true) {
                        System.out.println("\nSTEP " + step);
                        world.printWorld();
                        world.step();
                        step++;

                        String input = scanner.nextLine();
                        if (input.equals("0")) break;
                    }
                    break;

                case 2:
                    System.out.print("Quantos passos deseja executar? ");
                    int n = scanner.nextInt();

                    for (int i = 0; i < n; i++) {
                        System.out.println("\nSTEP " + step);
                        world.printWorld();
                        world.step();
                        step++;
                    }
                    break;

                case 3:
                    System.out.println("\nA executar ate extincao de uma especie...");

                    while (world.hasSheep() && world.hasWolves()) {
                        System.out.println("\nSTEP " + step);
                        world.printWorld();
                        world.step();
                        step++;
                    }

                    System.out.println("\n--- SIMULACAO TERMINADA ---");
                    if (!world.hasSheep()) {
                        System.out.println("As ovelhas foram extintas.");
                    } else if (!world.hasWolves()) {
                        System.out.println("Os lobos foram extintos.");
                    }
                    break;

                case 0:
                    running = false;
                    break;

                default:
                    System.out.println("Opcao invalida.");
            }
        }

        scanner.close();
        System.out.println("Programa terminado.");
    }
}
