package UI;

import javax.swing.*;

import Logic.Export;
import Logic.Import;

import java.awt.*;
import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;

import Objects.*;

public class TilemapEditorPanel extends JPanel {
    private DrawingPanel editorPanel = new DrawingPanel();
    private JPanel settingsPanel = new JPanel();
    private JPanel createBoardPanel = new JPanel();
    private JPanel editBrushPanel = new JPanel();
    private JTextField boardSizeXField = new JTextField();
    private JTextField boardSizeYField = new JTextField();
    private JTextField symbolField = new JTextField(1);
    private JButton createBoardButton = new JButton("Create board");
    private JButton importTileMapButton = new JButton("Import tile map");
    private JButton fgButton = new JButton("Choose foreground");
    private JButton bgButton = new JButton("Choose background");
    private JButton standartStyleButton = new JButton("Reset to standart colors");
    private JButton exportButton = new JButton("Export as .txt");

    private TileGrid board = new TileGrid(0, 0);
    private Brush brush = new Brush();
    private Color currentFg = null;
    private Color currentBg = null;

    public TilemapEditorPanel() {
        setLayout(new BorderLayout());
        settingsPanel.setLayout(new GridLayout(0, 1));
        createBoardPanel.setLayout(new GridLayout(2, 2));
        editBrushPanel.setLayout(new GridLayout(2, 2));

        createBoardButton.addActionListener(e -> {createBoard();});
        exportButton.addActionListener(e -> {exportBoard();});

        editorPanel.setBoard(board);
        editorPanel.setBrush(brush);

        importTileMapButton.addActionListener(e -> {
            Tilemap tm = null;
            try {
                tm = Import.importTileMap();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Fehler beim Importieren: " + ex.getMessage());
            }
            if (tm != null) {
                Import.importToBoard(board, tm, editorPanel);
                editorPanel.refreshBoard();
            }
        });

        fgButton.addActionListener(e -> {
            Color chosen = JColorChooser.showDialog(this, "Vordergrundfarbe wählen", Color.WHITE);
            if (chosen != null) currentFg = chosen;
            updateBrush();
        });

        bgButton.addActionListener(e -> {
            Color chosen = JColorChooser.showDialog(this, "Hintergrundfarbe wählen", Color.WHITE);
            if (chosen != null) currentBg = chosen;
            updateBrush();
        });

        standartStyleButton.addActionListener(e -> {
            currentFg = null;
            currentBg = null;
            symbolField.setText(" ");
            updateBrush();
        });

        symbolField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateBrush(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateBrush(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateBrush(); }
        });

        Style.stylePanel(settingsPanel);
        Style.stylePanel(createBoardPanel);
        Style.stylePanel(editBrushPanel);
        Style.styleTextField(boardSizeXField);
        Style.styleTextField(boardSizeYField);
        Style.styleTextField(symbolField);
        Style.styleButton(createBoardButton);
        Style.styleButton(importTileMapButton);
        Style.styleButton(fgButton);
        Style.styleButton(bgButton);
        Style.styleButton(standartStyleButton);
        Style.styleButton(exportButton);

        createBoardPanel.add(boardSizeXField);
        createBoardPanel.add(boardSizeYField);
        createBoardPanel.add(createBoardButton);
        createBoardPanel.add(importTileMapButton);
        editBrushPanel.add(symbolField);
        editBrushPanel.add(standartStyleButton);
        editBrushPanel.add(bgButton);
        editBrushPanel.add(fgButton);
        settingsPanel.add(createBoardPanel);
        settingsPanel.add(editBrushPanel);
        settingsPanel.add(exportButton);
        add(editorPanel, BorderLayout.CENTER);
        add(settingsPanel, BorderLayout.EAST);
    }

    private void createBoard() {
        int x = stringToInt(boardSizeXField.getText());
        int y = stringToInt(boardSizeYField.getText());
        if (x != -1 && y != -1)
            editorPanel.createBoard(x, y);
    }

    private void exportBoard() {
        Export.exportTilemap(board, this); 
    }

    private void updateBrush() {
        String text = symbolField.getText();
        String symbol = text.isEmpty()
            ? " "
            : new String(Character.toChars(text.codePointAt(0)));
        brush.setSymbol(symbol);
        brush.setBg(currentBg);
        brush.setFg(currentFg);
    }

    private int stringToInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return -1; // invalid integer value
        }
    }
}