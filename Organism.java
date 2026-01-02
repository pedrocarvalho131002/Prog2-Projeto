public abstract class Organism {
    protected int x;
    protected int y;
    protected int age;
    protected boolean alive;
    protected World world;

    public Organism(World world, int x, int y) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.age = 0;
        this.alive = true;
    }

    public void increaseAge() {
        age++;
    }

    public boolean isAlive() {
        return alive;
    }

    public void die() {
        alive = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setPosition(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }

    public abstract void act();
    public abstract char getSymbol();
}