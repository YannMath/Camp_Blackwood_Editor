package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import Objects.Coordinate;
import Objects.Tile;
import Objects.TileGrid;
import Objects.Tilemap;

public class InterfacePreviewPanel extends JPanel {
    private final InterfaceEditorPanel parentPanel;
    private static final String CELL_FONT_NAME = Font.MONOSPACED;
    Set<Coordinate> highlighted = new HashSet<>();

    private TileGrid board;
    private int rows;
    private int cols;

    public InterfacePreviewPanel(InterfaceEditorPanel parentPanel) {
        this.parentPanel = parentPanel;
        setPreferredSize(new Dimension(600, 600));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = e.getX();
                int row = e.getY();

                Tile t = getTileAt(col, row);
                if (t != null) parentPanel.receive(t.getX(), t.getY());
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int col = e.getX();
                int row = e.getY();

                Tile t = getTileAt(col, row);
                if (t != null) parentPanel.receive(t.getX(), t.getY());
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Important! Else there will be Rendering-artifacts
        Set<Coordinate> pendingSelection = parentPanel.getPendingSelection();
        if (board == null || cols == 0 || rows == 0) return;

        int cellH = Math.max(1, getHeight() / rows);
        Font cellFont = new Font(CELL_FONT_NAME, Font.PLAIN, Math.max(1, Math.round(cellH * 0.8f)));
        g.setFont(cellFont);
        FontMetrics fm = g.getFontMetrics();
        int cellW = Math.max(1, Math.round(cellH * fm.charWidth('M') / (float) fm.getHeight()));

        Tile[][] tiles = board.getTiles();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < tiles[r].length; c++) {
                Tile t = tiles[r][c];
                int x = c * cellW;
                int y = r * getHeight() / rows;
                int nextY = (r + 1) * getHeight() / rows;
                int actualCellH = nextY - y;

                // Background
                Color bg = t.getBG() != null ? t.getBG() : Color.LIGHT_GRAY;
                if (highlighted.contains(new Coordinate(c, r))) {
                    bg = blend(bg, Color.YELLOW, 0.5f); 
                }
                else if (pendingSelection.contains(new Coordinate(c, r))) {
                    bg = blend(bg, Color.GREEN, 0.5f);
                }
                g.setColor(bg);
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

    private Color blend(Color base, Color overlay, float ratio) {
        ratio = Math.max(0f, Math.min(1f, ratio));

        int r = (int) (base.getRed()   * (1 - ratio) + overlay.getRed()   * ratio);
        int g = (int) (base.getGreen() * (1 - ratio) + overlay.getGreen() * ratio);
        int b = (int) (base.getBlue()  * (1 - ratio) + overlay.getBlue()  * ratio);

        return new Color(r, g, b);
    }

    public void addSelection(List<Coordinate> infoFieldCoordinates) {
        for (Coordinate c : infoFieldCoordinates) {
            highlighted.add(new Coordinate(c.getX(), c.getY()));
        }
        repaint();
    }

    public void removeHighlight(Set<Coordinate> coordinates) {
        for (Coordinate c : coordinates) {
            highlighted.remove(c);
        }
        repaint();
    }

    public void importToBoard(Tilemap tm) {
        TileGrid newBoard = new TileGrid(0, 0);
        String[][] sprites = tm.getSprite();
        Color[][] bg = tm.getBackground();
        Color[][] fg = tm.getForeground();

        if (sprites.length == 0) {
            newBoard.setTiles(new Tile[0][0]);
        } else {
            Tile[][] tiles = new Tile[sprites.length][];

            for (int y = 0; y < sprites.length; y++) {
                tiles[y] = new Tile[sprites[y].length];
                for (int x = 0; x < sprites[y].length; x++) {
                    Tile t = new Tile();
                    tiles[y][x] = t;
                    t.setSymbol(sprites[y][x]);
                    if (y < bg.length && x < bg[y].length && bg[y][x] != null)
                        t.setBG(bg[y][x]);
                    if (y < fg.length && x < fg[y].length && fg[y][x] != null)
                        t.setFG(fg[y][x]);
                }
            }

            newBoard.setTiles(tiles);
        }

        board = newBoard;
        refreshBoard();
    }

    private Tile getTileAt(int mouseX, int mouseY) {
        if (board == null || cols == 0 || rows == 0) return null;
        int cellH = Math.max(1, getHeight() / rows);
        FontMetrics fm = getFontMetrics(new Font(CELL_FONT_NAME, Font.PLAIN, Math.max(1, Math.round(cellH * 0.8f))));
        int cellW = Math.max(1, Math.round(cellH * fm.charWidth('M') / (float) fm.getHeight()));
        int col = mouseX / cellW;
        int row = mouseY * rows / Math.max(1, getHeight());
        if (row < 0 || row >= rows || col < 0 || col >= cols) return null;
        Tile t = board.getTiles()[row][col];
        t.setX(col);
        t.setY(row);
        return t;
    }

    public void refreshBoard() {
        Tile[][] tiles = board == null ? null : board.getTiles();
        rows = tiles == null ? 0 : tiles.length;
        cols = 0;
        if (tiles != null) {
            for (Tile[] row : tiles) {
                if (row != null) {
                    cols = Math.max(cols, row.length);
                }
            }
        }
        repaint();
    }
}