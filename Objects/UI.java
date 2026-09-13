package Objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UI {
    private HashMap<String, List<Coordinate>> infoMap = new HashMap<>();
    private List<String> currentInfoAreas = new ArrayList<>();
    private String tmName;
    private int x_offset;
    private int y_offset;

    public UI(String tmName, int x_offset, int y_offset) {
        this.tmName = tmName;
        this.x_offset = x_offset;
        this.y_offset = y_offset;
    }

    public void addInfoArea(String name, Coordinate coordinate) {
        addInfoArea(name, List.of(coordinate));
    }

    public void addInfoArea(String name, List<Coordinate> coordinates) {
        infoMap.put(name, new ArrayList<>(coordinates));
        if (!currentInfoAreas.contains(name)) {
            currentInfoAreas.add(name);
        }
    }

    public void removeInfoArea(String name) {
        infoMap.remove(name);
        currentInfoAreas.remove(name);
    }

    public void setTmName(String tmName) {this.tmName = tmName;}
    public void setX_offset(int x_offset) {this.x_offset = x_offset;}
    public void setY_offset(int y_offset) {this.y_offset = y_offset;}

    public String getTmName() {return tmName;}
    public List<String> getInfoAreas() {return currentInfoAreas;}
    public List<Coordinate> getCoordinates(String name) {return infoMap.getOrDefault(name, List.of());}
    public int getX_offset() {return x_offset;}
    public int getY_offset() {return y_offset;}
}