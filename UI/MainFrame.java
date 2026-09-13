package UI;

import javax.swing.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        MainPanel mainPanel = new MainPanel();
        add(mainPanel);
        setTitle("Tile Drawer");
        setSize(1100, 700);
        setMinimumSize(new java.awt.Dimension(800, 500));
        setLocationRelativeTo(null);
    }
}
