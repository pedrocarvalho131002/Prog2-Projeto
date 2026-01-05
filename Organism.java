public abstract class Organism {
    protected World world;
    protected int x, y;
    protected int age;
    protected boolean alive = true;

    public Organism(World world, int x, int y) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.age = 0;
    }

    public abstract void step();

    public void die() {
        alive = false;
        world.removeOrganism(this);
    }

    public boolean isAlive() {
        return alive;
    }

    public int getX() { return x; }
    public int getY() { return y; }
}
