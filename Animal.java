public abstract class Animal extends Organism {
    protected int energy;
    protected int energyCostPerStep;

    public Animal(World world, int x, int y, int initEnergy, int maxAge, int energyCostPerStep) {
        super(world, x, y, maxAge);
        this.energy = initEnergy;
        this.energyCostPerStep = energyCostPerStep;
    }

    @Override
    public void ageOneStep() {
        super.ageOneStep();
        if (!alive) return;

        energy += energyCostPerStep; // tipicamente -1 por passo
        if (energy <= 0) {
            die();
        }
    }

    public int getEnergy() {
        return energy;
    }

    public boolean canReproduce(int minAge, int minEnergy) {
        return alive && age >= minAge && energy >= minEnergy;
    }
}
