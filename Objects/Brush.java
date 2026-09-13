package Objects;

import java.awt.Color;

public class Brush {
    private Color fg;
    private Color bg;
    private String s = " ";

    public Brush() {

    }

    public void setSymbol(String s) {this.s = s;}
    public void setBg(Color bg) {this.bg = bg;}
    public void setFg(Color fg) {this.fg = fg;}

    public Color fg() {return this.fg;}
    public Color bg() {return this.bg;}
    public String symbol() {return this.s;}
}
