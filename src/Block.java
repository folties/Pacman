
import java.awt.*;

public class Block {
    public int x, y, width, height;
    public Image image;

    public Block(int x, int y, int width, int height, Image image) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = image;
    }
}
