public abstract class Animal extends Organism {

    protected int energy;
    protected int maxAge;
    protected int energyPerStep;

    public Animal(World world, int x, int y, int maxAge, int initialEnergy, int energyPerStep) {
        super(world, x, y);
        this.maxAge = maxAge;
        this.energy = initialEnergy;
        this.energyPerStep = energyPerStep;
    }

    public void loseEnergy() {
        energy += energyPerStep;
        if (energy <= 0) {
            die();
        }
    }

    public boolean isTooOld() {
        return age > maxAge;
    }

    public int getAge() {
        return age;
    }

    public int getEnergy() {
        return energy;
    }
}