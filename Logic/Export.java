package Logic;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.awt.Color;

import Objects.Coordinate;
import Objects.Tile;
import Objects.TileGrid;
import Objects.UI;
import UI.InterfaceEditorPanel;
import UI.TilemapEditorPanel;

public class Export {
    public static void exportTilemap(TileGrid board, TilemapEditorPanel editor) {
        Tile[][] tiles = board.getTiles();
        if (tiles == null) return;
        StringBuilder sb = new StringBuilder();
        appendBlock(sb, "[SPRITE]", tiles, t -> String.valueOf(t.getSymbol()));
        appendBlock(sb, "[BG]", tiles, t -> formatColor(t.getBG()));
        appendBlock(sb, "[FG]", tiles, t -> formatColor(t.getFG()));
        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(editor) == JFileChooser.APPROVE_OPTION) {
            try {
                Files.writeString(chooser.getSelectedFile().toPath(), sb.toString(), StandardCharsets.UTF_8);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(editor, "Fehler beim Speichern: " + ex.getMessage());
            }
        } 
    }  

    private static void appendBlock(StringBuilder sb, String header, Tile[][] tiles, java.util.function.Function<Tile, String> extractor) {
        sb.append(header).append("\n");
        for (Tile[] row : tiles) {
            for (Tile t : row) {
                sb.append(extractor.apply(t)).append("");
            }
            sb.append("\n");
        }
    }

    private static String formatColor(Color c) {
        if (c == null) return "DEFAULT ";
        return String.format("(%03d %03d %03d)", c.getRed(), c.getGreen(), c.getBlue());
    }

    public static void exportInterface(UI gameInterface, InterfaceEditorPanel editor) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("[SPRITE]\n");
        sb.append(gameInterface.getTmName() + "\n");
        sb.append("[INFORMATION]\n");
        for (String info : gameInterface.getInfoAreas()) {
            List<Coordinate> coords = gameInterface.getCoordinates(info);
            String[] coordinateStrings = new String[coords.size()];
            for (int i = 0; i < coords.size(); i++) {
                if (i != 0)
                    coordinateStrings[i] = ", (" + coords.get(i).getX() + ", " + coords.get(i).getY() + ")";
                else
                    coordinateStrings[i] = "(" + coords.get(i).getX() + ", " + coords.get(i).getY() + ")";
            }
            sb.append("(" + info + "; ");
            for (String s : coordinateStrings) {
                sb.append(s);
            }
            sb.append(")\n");
        }
        sb.append("[OFFSET]\n");
        sb.append(gameInterface.getX_offset() + ", " + gameInterface.getY_offset() + "\n");

        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(editor) == JFileChooser.APPROVE_OPTION) {
            try {
                Files.writeString(chooser.getSelectedFile().toPath(), sb.toString(), StandardCharsets.UTF_8);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(editor, "Fehler beim Speichern: " + ex.getMessage());
            }
        } 
        
        System.out.println("Exportet interface with:");
        System.out.println("[SPRITE] " + gameInterface.getTmName());
        System.out.println("[Infos] " + gameInterface.getInfoAreas());
        System.out.println("[OFFSET] (" + gameInterface.getX_offset() + "/" + gameInterface.getY_offset() + ")");
    }
}