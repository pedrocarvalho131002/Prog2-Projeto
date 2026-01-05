import java.util.Random;
import java.util.Scanner;

public class Main {

    private static final int WIDTH = 20;
    private static final int HEIGHT = 20;

    public static void main(String[] args) {

        World world = new World(WIDTH, HEIGHT);
        Random rnd = new Random();
        Scanner sc = new Scanner(System.in);

        //Ajuda da LLM
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                double r = rnd.nextDouble();
                if (r < 0.04) {
                    world.addOrganism(new Wolf(world, x, y));
                } else if (r < 0.16) {
                    world.addOrganism(new Sheep(world, x, y));
                } else if (r < 0.91) {
                    world.addOrganism(new Plant(world, x, y));
                }
            }
        }

        boolean running = true;

        while (running) {
            System.out.println("\n===== SIMULADOR DE ECOSSISTEMA =====");
            System.out.println("1 - Executar 1 passo");
            System.out.println("2 - Executar N passos");
            System.out.println("3 - Executar até extinção de uma espécie");
            System.out.println("0 - Sair");
            System.out.print("Opção: ");

            int option = sc.nextInt();

            switch (option) {
                case 1:
                    world.stepAll();
                    world.print();
                    break;

                case 2:
                    System.out.print("Quantos passos deseja executar? ");
                    int n = sc.nextInt();

                    for (int i = 0; i < n; i++) {
                        world.stepAll();
                        world.print();

                        if (extinctSpecies(world) != null) {
                            System.out.println(
                                "Espécie extinta: " + extinctSpecies(world) +
                                ". Simulação interrompida."
                            );
                            break;
                        }
                    }
                    break;

                case 3:
                    while (extinctSpecies(world) == null) {
                        world.stepAll();
                        world.print();
                    }
                    System.out.println("Espécie extinta: " + extinctSpecies(world));
                    System.out.println("Simulação terminada.");
                    break;

                case 0:
                    running = false;
                    System.out.println("Simulação terminada.");
                    break;

                default:
                    System.out.println("Opção inválida.");
            }
        }

        sc.close();
    }

    private static String extinctSpecies(World world) {
        if (!world.speciesAlive(Plant.class)) return "Plantas";
        if (!world.speciesAlive(Sheep.class)) return "Ovelhas";
        if (!world.speciesAlive(Wolf.class))  return "Lobos";
        return null;
    }

}
