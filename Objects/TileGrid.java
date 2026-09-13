package Objects;

public class TileGrid {
    Tile[][] tiles;
    int width, height;

    public TileGrid(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setTiles(Tile[][] tiles) {
        this.tiles = tiles;
        this.height = tiles.length;
        this.width = tiles.length == 0 ? 0 : tiles[0].length;
    }

    public Tile[][] getTiles() {return tiles;}
    public int height() {return this.height;}
    public int width() {return this.width;}
}
