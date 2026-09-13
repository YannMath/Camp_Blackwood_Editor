package Objects;

import java.awt.Color;

public class Tile {
    String symbol = " ";
    Color getBG = null;
    Color getFG = null;

    public Tile() {

    }

    public void setSymbol(String symbol) {this.symbol = symbol;}
    public void setBG(Color bg) {this.getBG = bg;}
    public void setFG(Color fg) {this.getFG = fg;}

    public String getSymbol() {return symbol;}
    public Color getBG() {return getBG;}
    public Color getFG() {return getFG;}
}
