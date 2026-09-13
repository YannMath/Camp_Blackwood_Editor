package UI;

import javax.swing.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        //MainPanel mainPanel = new MainPanel();
        //add(mainPanel);
        TilemapEditorPanel mainPanel = new TilemapEditorPanel();
        add(mainPanel);
    }
}
