package Objects;

import java.awt.Color;

public class Tilemap {
    private String[][] sprite;
    private Color[][] background;
    private Color[][] foreground;

    public Tilemap(String[][] sprite, Color[][] background, Color[][] foreground) {
        this.sprite = sprite;
        this.background = background;
        this.foreground = foreground;
    }

    public String[][] getSprite() {return sprite;}
    public Color[][] getBackground() {return background;}
    public Color[][] getForeground() {return foreground;}
} 
