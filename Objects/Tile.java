package Objects;

import java.awt.Color;

public class Tile {
    String symbol = " ";
    Color getBG = null;
    Color getFG = null;
    int x = 0;
    int y = 0;

    public Tile() {

    }

    public void setSymbol(String symbol) {this.symbol = symbol;}
    public void setBG(Color bg) {this.getBG = bg;}
    public void setFG(Color fg) {this.getFG = fg;}
    public void setX(int x) {this.x = x;}
    public void setY(int y) {this.y = y;}

    public String getSymbol() {return symbol;}
    public Color getBG() {return getBG;}
    public Color getFG() {return getFG;}
    public int getX() {return x;}
    public int getY() {return y;}
}
