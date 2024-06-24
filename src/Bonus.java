import javax.swing.*;
import java.awt.*;

public class Bonus {
    Rectangle rectangle;
    Image image;
    int posX, posY;

    public Bonus(int x, int y){
        posX = x;
        posY = y;
        rectangle = new Rectangle(posX, posY, 30, 30);
        image = new ImageIcon("images/bonus.png").getImage();
    }
}
