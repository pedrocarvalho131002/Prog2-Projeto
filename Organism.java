public abstract class Organism {
    protected int x;
    protected int y;
    protected int age;
    protected int maxAge;
    protected boolean alive = true;

    public Organism(int x, int y, int maxAge) {
        this.x = x;
        this.y = y;
        this.maxAge = maxAge;
        this.age = 0;
    }

    public void incrementAge() {
        age++;
        if (age > maxAge) {
            alive = false;
        }
    }

    public boolean isAlive() {
        return alive;
    }

    public void die() {
        this.alive = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public abstract char getSymbol();
}
