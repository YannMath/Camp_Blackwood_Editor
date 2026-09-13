package UI;

import javax.swing.*;

public abstract class Editor<T> extends JPanel {
    protected T object;

    public Editor(T object) {
        this.object = object;
    }
    
    public abstract void load(T object);

    public abstract void export();
}
