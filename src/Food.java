import java.awt.Rectangle;

public class Food {
    int posX, posY;

    Rectangle rectangle;

    public Food(int x, int y){
        posX = x * 30;
        posY = y * 30;

        rectangle = new Rectangle(posX, posY, 30, 30);
    }
}
