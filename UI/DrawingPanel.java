package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import Objects.*;

public class DrawingPanel extends JPanel {
    private static final String CELL_FONT_NAME = Font.MONOSPACED;
    private TileGrid board;
    private Brush brush;

    private int rows;
    private int cols;

    public DrawingPanel() {
        setPreferredSize(new Dimension(600, 600));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = e.getX();
                int row = e.getY();

                Tile t = getTileAt(col, row);
                if (t != null) applyBrush(t);
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int col = e.getX();
                int row = e.getY();
                Tile t = getTileAt(col, row);
                if (t != null) applyBrush(t);
            }
        });
    }

    public void createBoard(int cols, int rows) {
        Tile[][] tiles = new Tile[rows][cols];
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                tiles[r][c] = new Tile();
        board.setTiles(tiles);
        this.rows = board.getTiles().length;
        this.cols = board.getTiles()[0].length;
        repaint();
    }

    private void applyBrush(Tile t) {
        if (board == null || brush == null) return;
        t.setSymbol(brush.symbol());
        t.setFG(brush.fg());
        t.setBG(brush.bg());
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Important! Else there will be Rendering-artifacts
        if (board == null || cols == 0 || rows == 0) return;

        int cellH = Math.max(1, getHeight() / rows);
        Font cellFont = new Font(CELL_FONT_NAME, Font.PLAIN, Math.max(1, Math.round(cellH * 0.8f)));
        g.setFont(cellFont);
        FontMetrics fm = g.getFontMetrics();
        int cellW = Math.max(1, Math.round(cellH * fm.charWidth('M') / (float) fm.getHeight()));

        for (int r = 0; r < rows; r++) {   
            for (int c = 0; c < cols; c++) {
                Tile t = board.getTiles()[r][c];
                int x = c * cellW;
                int y = r * getHeight() / rows;
                int nextY = (r + 1) * getHeight() / rows;
                int actualCellH = nextY - y;

                // Background
                g.setColor(t.getBG() != null ? t.getBG() : Color.LIGHT_GRAY); // Fallback for the Editor-Grid
                g.fillRect(x, y, cellW, actualCellH);

                g.setColor(Color.GRAY);
                g.drawRect(x, y, cellW, actualCellH); // Grid lines

                // Center symbol
                g.setColor(t.getFG() != null ? t.getFG() : Color.BLACK);
                String s = t.getSymbol();
                int textX = x + (cellW - fm.stringWidth(s)) / 2;
                int textY = y + (actualCellH - fm.getHeight()) / 2 + fm.getAscent();
                g.drawString(s, textX, textY);
            }
        }
    }

    private Tile getTileAt(int mouseX, int mouseY) {
        if (board == null || cols == 0 || rows == 0) return null;
        int cellH = Math.max(1, getHeight() / rows);
        FontMetrics fm = getFontMetrics(new Font(CELL_FONT_NAME, Font.PLAIN, Math.max(1, Math.round(cellH * 0.8f))));
        int cellW = Math.max(1, Math.round(cellH * fm.charWidth('M') / (float) fm.getHeight()));
        int col = mouseX / cellW;
        int row = mouseY * rows / Math.max(1, getHeight());
        if (row < 0 || row >= rows || col < 0 || col >= cols) return null;
        return board.getTiles()[row][col];
    }

    public void setBoard(TileGrid board) {this.board = board;}
    public void setBrush(Brush brush) {this.brush = brush;}

    public void refreshBoard() {
        rows = board.getTiles() == null ? 0 : board.getTiles().length;
        cols = rows == 0 ? 0 : board.getTiles()[0].length;
        repaint();
    }

    public Tile[][] getTiles() { return board.getTiles(); }
}