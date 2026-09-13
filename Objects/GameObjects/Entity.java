package Objects.GameObjects;

import Objects.GameObject;

public class Entity extends GameObject {
    private String name;
    private int maxHealth;

    public Entity(String sprite, String board, int x, int y, String name, int maxHealth) {
        super(sprite, board, x, y);
        this.name = name;
        this.maxHealth = maxHealth;
    }

    public String getName() {return name;}
    public int getMaxHealth() {return maxHealth;}
}
