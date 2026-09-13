package Logic;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.Color;

import Objects.*;
import UI.DrawingPanel;

public class Import {
    public static Tilemap importTileMap() throws IOException {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);
        File file = null;
        
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
            System.out.println("Ausgewählt: " + file.getAbsolutePath());
        }

        if (returnValue != JFileChooser.APPROVE_OPTION) {
            return null;
        }

        List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);

        List<String> spriteLines = new ArrayList<>();
        List<String> bgLines = new ArrayList<>();
        List<String> fgLines = new ArrayList<>();

        String currentSection = "";

        for (String line : lines) {

            if (line.equals("[SPRITE]")) {
                currentSection = "SPRITE";
                continue;
            }

            if (line.equals("[BG]")) {
                currentSection = "BG";
                continue;
            }

            if (line.equals("[FG]")) {
                currentSection = "FG";
                continue;
            }

            if (line.isBlank() && !"SPRITE".equals(currentSection)) {
                continue;
            }

            switch (currentSection) {

                case "SPRITE" -> spriteLines.add(line);

                case "BG" -> bgLines.add(line);

                case "FG" -> fgLines.add(line);
            }
        }

        String[][] sprite = new String[spriteLines.size()][];

        for (int y = 0; y < spriteLines.size(); y++) {
            sprite[y] = spriteLines.get(y).codePoints()
                    .mapToObj(codePoint -> new String(Character.toChars(codePoint)))
                    .toArray(String[]::new);
        }

        int[] expectedWidths = new int[sprite.length];
        for (int y = 0; y < sprite.length; y++) {
            expectedWidths[y] = sprite[y].length;
        }

        Color[][] background = parseColorMap(bgLines, expectedWidths);
        Color[][] foreground = parseColorMap(fgLines, expectedWidths);

        return new Tilemap(sprite, background, foreground);
    }


    private static Color[][] parseColorMap(List<String> lines, int[] expectedWidths) {
        Color[][] result = new Color[lines.size()][];

        for (int y = 0; y < lines.size(); y++) {
            List<Color> row = tokenizeColorRow(lines.get(y));
            int expectedWidth = y < expectedWidths.length ? expectedWidths[y] : 0;

            if (expectedWidth > 0 && row.size() != expectedWidth) {
                row = expandToWidth(row, expectedWidth);
            }

            result[y] = row.toArray(new Color[0]);
        }

        return result;
    }

    public static void importToBoard(TileGrid board, Tilemap tm, DrawingPanel panel) {
        String[][] sprites = tm.getSprite();
        Color[][] bg = tm.getBackground();
        Color[][] fg = tm.getForeground();

        if (sprites.length == 0) {
            board.setTiles(new Tile[0][0]);
            return;
        }

        Tile[][] tiles = new Tile[sprites.length][];

        for (int y = 0; y < sprites.length; y++) {
            tiles[y] = new Tile[sprites[y].length];
            for (int x = 0; x < sprites[y].length; x++) {
                Tile t = tiles[y][x];
                t = new Tile();
                tiles[y][x] = t;
                t.setSymbol(sprites[y][x]);
                if (y < bg.length && x < bg[y].length && bg[y][x] != null)
                    t.setBG(bg[y][x]);
                if (y < fg.length && x < fg[y].length && fg[y][x] != null)
                    t.setFG(fg[y][x]);
            }
        }

        board.setTiles(tiles);
        
        panel.setBoard(board);
    }

    private static List<Color> tokenizeColorRow(String row) {
        List<Color> colors = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inRgb = false;

        for (int i = 0; i < row.length(); i++) {
            char ch = row.charAt(i);

            if (ch == '(') {
                inRgb = true;
                current.append(ch);
                continue;
            }

            if (ch == ')') {
                inRgb = false;
                current.append(ch);
                colors.add(parseColor(current.toString()));
                current.setLength(0);
                continue;
            }

            if (Character.isWhitespace(ch) && !inRgb) {
                if (current.length() > 0) {
                    colors.add(parseColor(current.toString()));
                    current.setLength(0);
                }
                continue;
            }

            current.append(ch);
        }

        if (current.length() > 0) {
            colors.add(parseColor(current.toString()));
        }

        return colors;
    }

    private static List<Color> expandToWidth(List<Color> colors, int expectedWidth) {
        if (colors.isEmpty()) {
            return new ArrayList<>();
        }

        List<Color> expanded = new ArrayList<>(expectedWidth);
        int repeatFactor = (int) Math.ceil((double) expectedWidth / colors.size());

        for (Color color : colors) {
            for (int i = 0; i < repeatFactor && expanded.size() < expectedWidth; i++) {
                expanded.add(color);
            }
        }

        return expanded.subList(0, expectedWidth);
    }


    private static Color parseColor(String color) {
        if (color.startsWith("(") && color.endsWith(")")) {
            String values = color.substring(1, color.length() - 1);
            String[] rgb = values.trim().replace(',', ' ').split("\\s+");

            if (rgb.length != 3) {
                throw new IllegalArgumentException("Invalid RGB color: " + color);
            }

            int r = Integer.parseInt(rgb[0]);
            int g = Integer.parseInt(rgb[1]);
            int b = Integer.parseInt(rgb[2]);

            return new Color(r, g, b);
        }

        String upper = color.trim();
        if (upper.isEmpty()) {
            throw new IllegalArgumentException("Empty color token");
        }

        try {
            return ConvertLanternaToAwt.convertColor(upper);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unsupported color token: " + color, e);
        }
    }

    public static UI loadInterface() throws IOException {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);
        File file = null;

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
            System.out.println("Ausgewählt: " + file.getAbsolutePath());
        }

        if (returnValue != JFileChooser.APPROVE_OPTION) {
            return null;
        }

        final Pattern INFORMATION_PATTERN =
            Pattern.compile("^\\s*\\(([^;]+);(.*)\\)\\s*$");
        final Pattern COORDINATE_PATTERN =
                Pattern.compile("\\((-?\\d+)\\s*,\\s*(-?\\d+)\\)");

        List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);

        String spriteLine = null;
        List<String> infoAreaLines = new ArrayList<>();
        String offsetLine = null;

        String currentSection = "";

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.equals("[SPRITE]")) {
                currentSection = "SPRITE";
                continue;
            }

            if (line.equals("[INFORMATION]")) {
                currentSection = "INFORMATION";
                continue;
            }

            if (line.equals("[OFFSET]")) {
                currentSection = "OFFSET";
                continue;
            }

            if (line.isBlank()) {
                continue;
            }

            switch (currentSection) {
                case "SPRITE" -> spriteLine = line;
                case "INFORMATION" -> infoAreaLines.add(line);
                case "OFFSET" -> offsetLine = line;
            }
        }

        if (spriteLine == null || spriteLine.isBlank()) {
            throw new IOException("UI file does not define a sprite link for chosen UI");
        }

        int[] offsets = parseOffset(offsetLine);
        int x_offset = offsets[0];
        int y_offset = offsets[1];

        UI newInterface = new UI(spriteLine.trim(), x_offset, y_offset);

        Map<String, List<Coordinate>> informationAreas = new LinkedHashMap<>();
        for (String infoAreaLine : infoAreaLines) {
            Matcher informationMatcher = INFORMATION_PATTERN.matcher(infoAreaLine);
            if (!informationMatcher.matches()) {
                throw new IllegalArgumentException("Invalid information area: " + infoAreaLine);
            }

            String name = informationMatcher.group(1).trim();
            Matcher coordinateMatcher = COORDINATE_PATTERN.matcher(informationMatcher.group(2));
            List<Coordinate> coordinates = informationAreas.computeIfAbsent(name, ignored -> new ArrayList<>());
            while (coordinateMatcher.find()) {
                int x = Integer.parseInt(coordinateMatcher.group(1));
                int y = Integer.parseInt(coordinateMatcher.group(2));
                coordinates.add(new Coordinate(x, y));
            }

            if (coordinates.isEmpty()) {
                throw new IllegalArgumentException("Information area has no coordinates: " + name);
            }
        }

        for (Map.Entry<String, List<Coordinate>> informationArea : informationAreas.entrySet()) {
            newInterface.addInfoArea(informationArea.getKey(), informationArea.getValue());
        }

        return newInterface;
    }

    private static int[] parseOffset(String offsetLine) {
        if (offsetLine == null || offsetLine.isBlank()) {
            return new int[]{0, 0};
        }

        String[] values = offsetLine.split("\\s*,\\s*");
        if (values.length != 2) {
            throw new IllegalArgumentException("Invalid UI offset: " + offsetLine);
        }

        return new int[]{Integer.parseInt(values[0].trim()), Integer.parseInt(values[1].trim())};
    }
}