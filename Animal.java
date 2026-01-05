public abstract class Animal extends Organism {
    protected int energy;
    protected int maxAge;
    protected int energyPerStep;

    public Animal(World world, int x, int y, int energy, int maxAge, int energyPerStep) {
        super(world, x, y);
        this.energy = energy;
        this.maxAge = maxAge;
        this.energyPerStep = energyPerStep;
    }

    protected void ageAndConsume() {
        age++;
        energy += energyPerStep;
        if (age > maxAge || energy <= 0) {
            die();
        }
    }

    protected boolean canReproduce(int minAge, int minEnergy) {
        return age >= minAge && energy >= minEnergy;
    }
}
