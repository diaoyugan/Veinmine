package top.diaoyugan.veinmine.client.configScreen;

public class VerticalLayout {

    private final int x;
    private int y;
    private final int gap;

    public VerticalLayout(int x, int startY, int gap) {
        this.x = x;
        this.y = startY;
        this.gap = gap;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public void next(int height) {
        y += height + gap;
    }
}

