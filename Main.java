public class Main {

    public static void main(String[] args) {

        int width = 20;
        int height = 20;

        double pWolf = 0.04;
        double pSheep = 0.12;
        double pPlant = 0.75;

        System.out.println("Simulação iniciada.");
        System.out.println(
                "World criado: " + width + "x" + height +
                        " | pWolf=" + pWolf +
                        " pSheep=" + pSheep +
                        " pPlant=" + pPlant
        );

        World world = new World(width, height);


        world.initialize(pWolf, pSheep, pPlant);
        world.print();
        for (int t = 1; t <= 3; t++) {
            world.step();
            System.out.println("Depois de " + t + " step:");
            world.print();
        }


    }

}
