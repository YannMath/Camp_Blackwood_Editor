package UI;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.IOException;
import javax.swing.*;

import Logic.Import;
import Logic.Export;
import Objects.UI;

public class InterfaceEditorPanel extends Editor<UI> {
    private JPanel editorPanel = new JPanel(new GridLayout(0, 1));
    private JTextField nameField = new JTextField("");
    private JSpinner x_offsetSpinner = new JSpinner();
    private JSpinner y_offsetSpinner = new JSpinner();

    private UI gameInterface;

    public InterfaceEditorPanel(UI object) {
        super(object);

        setLayout(new BorderLayout());

        Style.styleTextField(nameField);
        Style.styleSpinner(x_offsetSpinner);
        Style.styleSpinner(y_offsetSpinner);

        add(nameField);
        editorPanel.add(nameField);
        editorPanel.add(x_offsetSpinner);
        editorPanel.add(y_offsetSpinner);
        add(editorPanel, BorderLayout.EAST);
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
