public abstract class Organism {
    protected World world;
    protected int x;
    protected int y;

    protected int age;
    protected int maxAge;
    protected boolean alive;

    public Organism(World world, int x, int y, int maxAge) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.maxAge = maxAge;
        this.age = 0;
        this.alive = true;
    }

    // Fase: envelhecimento
    public void ageOneStep() {
        age++;
        if (age > maxAge) {
            die();
        }
    }

    public void die() {
        alive = false;
    }

    public boolean isAlive() {
        return alive;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getAge() {
        return age;
    }

    public abstract char getSymbol();

    // Evita coisas como Class<?> e stream(); facilita contagens/regras
    public abstract int getTypeId();
}
