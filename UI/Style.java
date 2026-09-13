package UI;

import javax.swing.*;
import java.awt.*;

public class Style {
    public static final Color BACKGROUND = new Color(40, 40, 40);
    public static final Color FOREGROUND = new Color(230, 230, 230);
    public static final Color ACCENT = new Color(80, 140, 255);
    public static final Font DEFAULT_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 13);

    public static void stylePanel(JPanel panel) {
        panel.setBackground(BACKGROUND);
    }

    public static void styleTextField(JTextField field) {
        field.setBackground(Color.WHITE);
        field.setForeground(Color.BLACK);
        field.setFont(DEFAULT_FONT);
        field.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(ACCENT, 1),
            BorderFactory.createEmptyBorder(4, 6, 4, 6)));
    }

    public static void styleButton(JButton button) {
        button.setBackground(ACCENT);
        button.setForeground(Color.WHITE);
        button.setFont(BUTTON_FONT);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
    }

    public static void styleSpinner(JSpinner spinner) {
        spinner.setFont(DEFAULT_FONT);
        spinner.setForeground(FOREGROUND);
        spinner.setBackground(BACKGROUND);

        spinner.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT, 1),
                BorderFactory.createEmptyBorder(4, 6, 4, 6)
        ));

        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor defaultEditor) {
            JTextField field = defaultEditor.getTextField();
            field.setFont(DEFAULT_FONT);
            field.setForeground(FOREGROUND);
            field.setBackground(BACKGROUND);
            field.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        }

        for (Component c : spinner.getComponents()) {
            if (c instanceof JButton button) {
                button.setBackground(ACCENT);
                button.setForeground(Color.WHITE);
                button.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
                button.setFocusPainted(false);
            }
        }
    }
}
