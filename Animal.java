public abstract class Animal extends Organism {

    protected int energy;
    protected int energyPerStep;

    public Animal(int x, int y, int maxAge, int initialEnergy, int energyPerStep) {
        super(x, y, maxAge);
        this.energy = initialEnergy;
        this.energyPerStep = energyPerStep;
    }

    public void consumeEnergy() {
        energy += energyPerStep;
        if (energy <= 0) {
            alive = false;
        }
    }

    public int getEnergy() {
        return energy;
    }

    public void addEnergy(int amount) {
        energy += amount;
    }

    public abstract void move(World world);

    public abstract void tryReproduce(World world);
}
