import UI.MainFrame;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        System.out.println("Hello World");

        MainFrame mainFrame = new MainFrame();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
    }
}