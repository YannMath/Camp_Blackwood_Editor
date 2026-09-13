package Logic;

import java.awt.Color;

public class ConvertLanternaToAwt {
    public static Color convertColor(String lanternaColor) {
        switch (lanternaColor) {
            case "BLACK":
                return new Color(0, 0, 0);
            case "RED":
                return new Color(170, 0, 0);
            case "GREEN":
                return new Color(0, 170, 0);
            case "YELLOW":
                return new Color(170, 170, 0);
            case "BLUE":
                return new Color(0, 0, 170);
            case "MAGENTA":
                return new Color(170, 0, 170);
            case "CYAN":
                return new Color(0, 170, 170);
            case "WHITE":
                return new Color(170, 170, 170);
            case "BLACK_BRIGHT":
                return new Color(85, 85, 85);
            case "RED_BRIGHT":
                return new Color(255, 85, 85);
            case "GREEN_BRIGHT":
                return new Color(85, 255, 85);
            case "YELLOW_BRIGHT":
                return new Color(255, 255, 85);
            case "BLUE_BRIGHT":
                return new Color(85, 85, 255);
            case "MAGENTA_BRIGHT":
                return new Color(255, 85, 255);
            case "CYAN_BRIGHT":
                return new Color(85, 255, 255);
            case "WHITE_BRIGHT":
                return new Color(255, 255, 255);
        }
        return null;
    }
}
