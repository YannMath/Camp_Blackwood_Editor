package UI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.IOException;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import Logic.Import;
import Logic.Export;
import Objects.UI;
import Objects.Coordinate;
import Objects.Tilemap;

public class InterfaceEditorPanel extends Editor<UI> {
    private InterfacePreviewPanel previewPanel = new InterfacePreviewPanel();
    private JPanel infoFieldSelectionPanel = new JPanel(new GridLayout(0, 1));
    private JPanel editorPanel = new JPanel(new GridLayout(0, 1));
    private JTextField pathField = new JTextField("F:/Terminal/First_Game_Test/game_test/src/main/resources/tilemaps");
    private JTextField nameField = new JTextField("");
    private JSpinner x_offsetSpinner = new JSpinner();
    private JSpinner y_offsetSpinner = new JSpinner();

    private  Path tilemapDirectory = Paths.get(pathField.getText().trim());
    private enum EditMode { VIEWING, EDITING }
    private EditMode mode = EditMode.VIEWING;
    private String editingAreaName = null;
    private Set<Coordinate> pendingSelection = new HashSet<>();
    private UI gameInterface;

    public InterfaceEditorPanel(UI object) {
        super(object);

        setLayout(new BorderLayout());

        nameField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                updatePreview();
            }
        
            public void removeUpdate(DocumentEvent e) {
                updatePreview();
            }
        
            public void changedUpdate(DocumentEvent e) {
                updatePreview();
            }
        });
        pathField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                updatePath();
            }
        
            public void removeUpdate(DocumentEvent e) {
                updatePath();
            }
        
            public void changedUpdate(DocumentEvent e) {
                updatePath();
            }
        });

        editorPanel.setPreferredSize(new Dimension(100, this.getHeight()));

        Style.stylePanel(previewPanel);
        Style.stylePanel(infoFieldSelectionPanel);
        Style.stylePanel(editorPanel);
        Style.styleTextField(pathField);
        Style.styleTextField(nameField);
        Style.styleSpinner(x_offsetSpinner);
        Style.styleSpinner(y_offsetSpinner);

        editorPanel.add(pathField);
        editorPanel.add(nameField);
        editorPanel.add(x_offsetSpinner);
        editorPanel.add(y_offsetSpinner);
        add(previewPanel, BorderLayout.CENTER);
        add(infoFieldSelectionPanel, BorderLayout.WEST);
        add(editorPanel, BorderLayout.EAST);
    }

    private boolean tilemapExists() {
        String name = nameField.getText().trim();

        if (name.isEmpty()) return false;
        if (!name.endsWith(".txt")) name += ".txt";

        Path tilemapPath = tilemapDirectory.resolve(name).normalize();

        return tilemapPath.getParent().equals(tilemapDirectory.normalize())
            && Files.isRegularFile(tilemapPath);
    }

    private void updatePreview() {
        if (!tilemapExists()) {
            return;
        }

        String name = nameField.getText().trim();
        if (!name.endsWith(".txt")) {
            name += ".txt";
        }

        Path tilemapPath = tilemapDirectory.resolve(name).normalize();
        try {
            Tilemap tilemap = Import.importSpriteFromFilename(tilemapPath.toString());
            previewPanel.importToBoard(tilemap);
            gameInterface.setTmName(nameField.getText().trim());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void updatePath() {
        tilemapDirectory = Paths.get(pathField.getText().trim());
        updatePreview();
    }
 
    @Override
    public void load(UI object) {
        super.object = object;
        try {
            this.gameInterface = Import.loadInterface();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load interface", e);
        }
    }

    @Override 
    public void export() {
        Export.exportInterface(gameInterface, this);
    }
}