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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

import Logic.Import;
import Logic.Export;
import Objects.UI;
import Objects.Coordinate;
import Objects.Tilemap;

public class InterfaceEditorPanel extends Editor<UI> {
    private InterfacePreviewPanel previewPanel = new InterfacePreviewPanel(this);
    private JScrollPane infoFieldOverviewContainer = new JScrollPane();
    private JPanel infoFieldPanel = new JPanel(new GridLayout(0, 1));
    private JPanel infoFieldNamingPanel = new JPanel(new GridLayout(1, 0));
    private JPanel infoFieldOverviewPanel = new JPanel(new GridLayout(0, 2));
    private JPanel editorPanel = new JPanel(new GridLayout(0, 1));
    private JButton saveInfoAreaButton = new JButton("Save information area");
    private JButton pathButton = new JButton("Change directory");
    private JButton importButton = new JButton("Import a .ui file");
    private JButton exportButton = new JButton("Export as .ui");
    private JTextField infoAreaNameField = new JTextField("");
    private JTextField nameField = new JTextField("");
    private JSpinner x_offsetSpinner = new JSpinner();
    private JSpinner y_offsetSpinner = new JSpinner();

    private String path = "F:/Terminal/First_Game_Test/game_test/src/main/resources/tilemaps";
    private  Path tilemapDirectory = Paths.get(path);
    private enum EditMode { VIEWING, EDITING }
    private EditMode mode = EditMode.VIEWING;
    private String editingAreaName = null;
    private Set<Coordinate> pendingSelection = new HashSet<>();
    private List<String> names = new ArrayList<>();
    private UI gameInterface = new UI("", 0, 0);

    public InterfaceEditorPanel(UI object) {
        super(object);

        setLayout(new BorderLayout());

        infoAreaNameField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                updateInfoArea();
            }
        
            public void removeUpdate(DocumentEvent e) {
                updateInfoArea();
            }
        
            public void changedUpdate(DocumentEvent e) {
                updateInfoArea();
            }
        });
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
        saveInfoAreaButton.addActionListener(e -> {
            if (names.contains(editingAreaName)) {JOptionPane.showMessageDialog(this, "Name already exists"); return;}
            Set<Coordinate> savedSelection = new HashSet<>(pendingSelection);
            addInfoSection(editingAreaName, savedSelection);
            gameInterface.addInfoArea(editingAreaName, new ArrayList<>(savedSelection));
            previewPanel.addSelection(new ArrayList<>(savedSelection));
            pendingSelection.clear();
            infoAreaNameField.setText("");

            revalidate();
            repaint();
        });
        pathButton.addActionListener(e -> {
            path = JOptionPane.showInputDialog(
                this,
                "Enter a valid path:",
                "Path",
                JOptionPane.PLAIN_MESSAGE
            );
            updatePath();
        });
        importButton.addActionListener(e -> {load(object);});
        exportButton.addActionListener(e -> {
            gameInterface.setTmName(nameField.getText().trim());
            gameInterface.setX_offset((int) x_offsetSpinner.getValue());
            gameInterface.setY_offset((int) y_offsetSpinner.getValue());

            export();
        });

        editorPanel.setPreferredSize(new Dimension(150, this.getHeight()));
        infoFieldNamingPanel.setPreferredSize((new Dimension(250, 100)));

        Style.stylePanel(previewPanel);
        Style.stylePanel(infoFieldPanel);
        Style.stylePanel(infoFieldNamingPanel);
        Style.stylePanel(infoFieldOverviewPanel);
        Style.stylePanel(editorPanel);
        Style.styleButton(saveInfoAreaButton);
        Style.styleButton(pathButton);
        Style.styleButton(importButton);
        Style.styleButton(exportButton);
        Style.styleTextField(infoAreaNameField);
        Style.styleTextField(nameField);
        Style.styleSpinner(x_offsetSpinner);
        Style.styleSpinner(y_offsetSpinner);

        infoFieldNamingPanel.add(infoAreaNameField);
        infoFieldNamingPanel.add(saveInfoAreaButton);
        infoFieldOverviewContainer.setViewportView(infoFieldOverviewPanel);
        infoFieldPanel.add(infoFieldNamingPanel);
        infoFieldPanel.add(infoFieldOverviewContainer);
        editorPanel.add(pathButton);
        editorPanel.add(nameField);
        editorPanel.add(x_offsetSpinner);
        editorPanel.add(y_offsetSpinner);
        editorPanel.add(importButton);
        editorPanel.add(exportButton);
        add(previewPanel, BorderLayout.CENTER);
        add(infoFieldPanel, BorderLayout.WEST);
        add(editorPanel, BorderLayout.EAST);
    }

    private void addInfoSection(String name, Set<Coordinate> infoAreaCoordinates) {
        JPanel row = new JPanel();
        JTextField textField = new JTextField(name);
        JButton removeButton = new JButton("X");

        names.add(name);

        removeButton.addActionListener(e -> {
            gameInterface.removeInfoArea(name);
            previewPanel.removeHighlight(infoAreaCoordinates);
            previewPanel.revalidate();
            previewPanel.repaint();
            names.remove(names.indexOf(name));

            infoFieldOverviewPanel.remove(row);
            infoFieldOverviewPanel.revalidate();
            infoFieldOverviewPanel.repaint();
        });

        Style.stylePanel(row);
        Style.styleTextField(textField);
        Style.styleButton(removeButton);

        row.add(textField);
        row.add(removeButton);
        infoFieldOverviewPanel.add(row);
    }

    private void updateInfoArea() {
        editingAreaName = infoAreaNameField.getText().trim();
        if (editingAreaName.equals("")) {mode = EditMode.VIEWING; return;}
        mode = EditMode.EDITING;
    }

    public void receive(int x, int y) {
        if (mode == EditMode.EDITING) {
            pendingSelection.add(new Coordinate(x, y));
        }
        previewPanel.revalidate();
        previewPanel.repaint();
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
        tilemapDirectory = Paths.get(path);
        updatePreview();
    }
 
    @Override
    public void load(UI object) {
        super.object = object;
        try {
            this.gameInterface = Import.loadInterface();
            if (gameInterface == null) {
                return;
            }

            this.nameField.setText(gameInterface.getTmName());
            updatePreview();
            for (String infoArea: gameInterface.getInfoAreas()) {
                addInfoSection(infoArea, new HashSet<>(gameInterface.getCoordinates(infoArea)));
                previewPanel.addSelection(new ArrayList<>(gameInterface.getCoordinates(infoArea)));
            }
            revalidate();
            repaint();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load interface", e);
        }
    }

    @Override 
    public void export() {
        Export.exportInterface(gameInterface, this);
    }

    public Set<Coordinate> getPendingSelection() {return pendingSelection;}
}