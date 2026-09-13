package UI;

import javax.swing.*;
import java.awt.*;
import Objects.UI;

public class MainPanel extends JPanel {
    private static final String HOME_CARD = "home";
    private static final String TILEMAP_CARD = "tilemap";
    private static final String INTERFACE_CARD = "interface";

    private final CardLayout cardLayout = new CardLayout();

    public MainPanel() {
        setLayout(cardLayout);

        add(createHomePanel(), HOME_CARD);
        add(createEditorCard(new TilemapEditorPanel(), "Tilemap editor"), TILEMAP_CARD);
        add(createEditorCard(
            new InterfaceEditorPanel(new UI("New interface", 0, 0)),
            "Interface editor"), INTERFACE_CARD);
        cardLayout.show(this, HOME_CARD);
    }

    private JPanel createHomePanel() {
        JPanel homePanel = new JPanel(new BorderLayout(0, 24));
        homePanel.setBorder(BorderFactory.createEmptyBorder(48, 64, 48, 64));
        Style.stylePanel(homePanel);

        JPanel headingPanel = new JPanel();
        headingPanel.setOpaque(false);
        headingPanel.setLayout(new BoxLayout(headingPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Editor");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(Style.FOREGROUND);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));

        JLabel subtitleLabel = new JLabel("Choose what you want to edit");
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setForeground(new Color(175, 175, 175));
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        headingPanel.add(titleLabel);
        headingPanel.add(Box.createVerticalStrut(8));
        headingPanel.add(subtitleLabel);

        JPanel choicesPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        choicesPanel.setOpaque(false);
        choicesPanel.add(createChoiceCard(
            "Tilemap", "Draw and export tile-based maps", TILEMAP_CARD));
        choicesPanel.add(createChoiceCard(
            "Interface", "Create and edit game interfaces", INTERFACE_CARD));

        homePanel.add(headingPanel, BorderLayout.NORTH);
        homePanel.add(choicesPanel, BorderLayout.CENTER);
        return homePanel;
    }

    private JPanel createChoiceCard(String title, String description, String cardName) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 70, 70)),
            BorderFactory.createEmptyBorder(28, 28, 28, 28)));
        Style.stylePanel(card);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(Style.FOREGROUND);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));

        JLabel descriptionLabel = new JLabel(description);
        descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        descriptionLabel.setForeground(new Color(175, 175, 175));
        descriptionLabel.setFont(Style.DEFAULT_FONT);

        JButton openButton = new JButton("Open " + title);
        openButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        Style.styleButton(openButton);
        openButton.addActionListener(e -> cardLayout.show(this, cardName));

        card.add(Box.createVerticalGlue());
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(descriptionLabel);
        card.add(Box.createVerticalStrut(24));
        card.add(openButton);
        card.add(Box.createVerticalGlue());
        return card;
    }

    private JPanel createEditorCard(JPanel editorPanel, String title) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        Style.stylePanel(card);
        card.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel navigationPanel = new JPanel(new BorderLayout());
        navigationPanel.setOpaque(false);
        JButton backButton = new JButton("Back to editor selection");
        Style.styleButton(backButton);
        backButton.addActionListener(e -> cardLayout.show(this, HOME_CARD));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(Style.FOREGROUND);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 0));

        navigationPanel.add(backButton, BorderLayout.WEST);
        navigationPanel.add(titleLabel, BorderLayout.CENTER);
        card.add(navigationPanel, BorderLayout.NORTH);
        card.add(editorPanel, BorderLayout.CENTER);
        return card;
    }
}
