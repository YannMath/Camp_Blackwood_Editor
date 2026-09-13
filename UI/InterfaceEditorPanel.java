package UI;

import java.io.IOException;
import javax.swing.*;
import Logic.Import;
import Objects.UI;

public class InterfaceEditorPanel extends Editor<UI> {
    private JTextField nameField;
    private JSpinner x_offsetSpinner;
    private JSpinner y_offsetSpinner;

    private UI gameInterface;

    public InterfaceEditorPanel(UI object) {
        super(object);

        // Build UI...
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

    }
}
