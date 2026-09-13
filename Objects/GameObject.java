package Objects;

public abstract class GameObject {
    private String sprite;
    private String board;
    private int x;
    private int y;

    public GameObject(String sprite, String board, int x, int y) {
        this.sprite = sprite;
        this.board = board;
        this.x = x;
        this.y = y;
    }

    public String getSpriteName() {return sprite;}
    public String boardName() {return board;}
    public int getx() {return x;}
    public int getY() {return y;}
}
