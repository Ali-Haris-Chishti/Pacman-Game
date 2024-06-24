import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class Player{
    Map<Direction, String> imageMap = new HashMap<>();
    private int speed = 2;

    Image image1;
    Image image2;

    int posX, posY;

    Direction nextDir = null;
    int counter = -1;
    public void changeDirection(Direction dir){
        if (dir == Direction.UP || dir == Direction.DOWN)
            counter = posX % 30;
        else
            counter = posY % 30;
        if (counter == 0)
            direction = dir;
        else
            nextDir = dir;
        image1 = new ImageIcon("images/pac 1 " + imageMap.get(dir) + ".png").getImage();
        image2 = new ImageIcon("images/pac 2 " + imageMap.get(dir) + ".png").getImage();
    }

    public void move(){
        switch (direction){
            case UP -> {
                if (isSafe(new Rectangle(posX, posY - speed, 30, 30)))
                    posY -= speed;
                counter = posY % 30;
            }
            case RIGHT -> {
                if (isSafe(new Rectangle(posX + speed, posY, 30, 30)))
                    posX += speed;
                counter = posX % 30;
            }
            case DOWN -> {
                if (isSafe(new Rectangle(posX, posY + speed, 30, 30)))
                    posY += speed;
                counter = posY % 30;
            }
            case LEFT -> {
                if (isSafe(new Rectangle(posX - speed, posY, 30, 30)))
                    posX -= speed;
                counter = posX % 30;
            }
        }
        if (counter == 0 && nextDir != null) {
            direction = nextDir;
            nextDir = null;
        }
        if (posX <= -30)
            posX = 30*19;
        if (posX >= 30*20)
            posX = 0;
        rectangle = new Rectangle(posX, posY, 30, 30);

        if (Game.current.bonus != null)
            if (rectangle.intersects(Game.current.bonus.rectangle)) {
                bonusEaten = true;
                Game.current.bonus = null;
                Game.current.score += 100;
                bonusCounter = 0;
                timeOver = false;
        }
        bonusCounter++;
        if (!timeOver && bonusCounter > 500){
            if (posX % 30 == 0 && posY % 30 == 0){
                System.out.println("Bonus Time Over");
                speed = 2;
                bonusEaten = false;
                timeOver = true;
            }
        }
        if (bonusEaten)
            if (posX % 30 == 0 && posY % 30 == 0){
                System.out.println("Bonus Time Started");
                speed = 3;
                bonusEaten = false;
            }
    }
    boolean bonusEaten = false;
    int bonusCounter = 0;

    boolean timeOver = true;

    public boolean isSafe(Rectangle rectangle){
        for (Rectangle rect: obstacleVector){
            if (rect.intersects(rectangle))
                return false;
        }
        return true;
    }

    Direction direction = Direction.RIGHT;

    public Player(int x, int y){
        posX = x*30;
        posY = y*30;
        rectangle = new Rectangle(posX, posY, 30, 30);
        ImageIcon imageIcon = new ImageIcon("images/pac 1 right.png");
        image1 = imageIcon.getImage();
        imageIcon = new ImageIcon("images/pac 2 right.png");
        image2 = imageIcon.getImage();

        imageMap.put(Direction.UP, "up");
        imageMap.put(Direction.RIGHT, "right");
        imageMap.put(Direction.DOWN, "down");
        imageMap.put(Direction.LEFT, "left");


        initializeObstacleVector();
    }

    // move to starting position
    void lifeLost(){
        posX = 2*30;
        posY = 9*30;
        direction = Direction.RIGHT;
        rectangle = new Rectangle(posX, posY, 30, 30);
    }

    Rectangle rectangle;


    // All the blocks in which player or enemy can not go, darkblue blocks
    void initializeObstacleVector(){
        obstacleVector = new Vector<>();

        for (int i = 0; i < 16; i++)
            obstacleVector.add(new Rectangle(i*30, 0, 30, 30));

        for (int i = 0; i < 16; i++)
            obstacleVector.add(new Rectangle(i*30, 20*30, 30, 30));

        for (int i = 0; i < 7; i++)
            obstacleVector.add(new Rectangle(0, i*30, 30, 30));

        for (int i = 0; i < 7; i++)
            obstacleVector.add(new Rectangle(16*30, i*30, 30, 30));

        for (int i = 12; i < 21; i++)
            obstacleVector.add(new Rectangle(0, i*30, 30, 30));

        for (int i = 12; i < 21; i++)
            obstacleVector.add(new Rectangle(16*30, i*30, 30, 30));

        obstacleVector.add(new Rectangle(8*30, 30, 30, 30));

        obstacleVector.add(new Rectangle(2*30, 2*30, 30, 30));
        obstacleVector.add(new Rectangle(3*30, 2*30, 30, 30));
        obstacleVector.add(new Rectangle(5*30, 2*30, 30, 30));
        obstacleVector.add(new Rectangle(6*30, 2*30, 30, 30));
        obstacleVector.add(new Rectangle(8*30, 2*30, 30, 30));
        obstacleVector.add(new Rectangle(10*30, 2*30, 30, 30));
        obstacleVector.add(new Rectangle(11*30, 2*30, 30, 30));
        obstacleVector.add(new Rectangle(13*30, 2*30, 30, 30));
        obstacleVector.add(new Rectangle(14*30, 2*30, 30, 30));

        obstacleVector.add(new Rectangle(2*30, 4*30, 30, 30));
        obstacleVector.add(new Rectangle(3*30, 4*30, 30, 30));
        obstacleVector.add(new Rectangle(5*30, 4*30, 30, 30));
        obstacleVector.add(new Rectangle(7*30, 4*30, 30, 30));
        obstacleVector.add(new Rectangle(8*30, 4*30, 30, 30));
        obstacleVector.add(new Rectangle(9*30, 4*30, 30, 30));
        obstacleVector.add(new Rectangle(11*30, 4*30, 30, 30));
        obstacleVector.add(new Rectangle(13*30, 4*30, 30, 30));
        obstacleVector.add(new Rectangle(14*30, 4*30, 30, 30));

        obstacleVector.add(new Rectangle(5*30, 5*30, 30, 30));
        obstacleVector.add(new Rectangle(8*30, 5*30, 30, 30));
        obstacleVector.add(new Rectangle(11*30, 5*30, 30, 30));

        obstacleVector.add(new Rectangle(30, 6*30, 30, 30));
        obstacleVector.add(new Rectangle(2*30, 6*30, 30, 30));
        obstacleVector.add(new Rectangle(3*30, 6*30, 30, 30));
        obstacleVector.add(new Rectangle(5*30, 6*30, 30, 30));
        obstacleVector.add(new Rectangle(6*30, 6*30, 30, 30));
        obstacleVector.add(new Rectangle(8*30, 6*30, 30, 30));
        obstacleVector.add(new Rectangle(10*30, 6*30, 30, 30));
        obstacleVector.add(new Rectangle(11*30, 6*30, 30, 30));
        obstacleVector.add(new Rectangle(13*30, 6*30, 30, 30));
        obstacleVector.add(new Rectangle(14*30, 6*30, 30, 30));
        obstacleVector.add(new Rectangle(15*30, 6*30, 30, 30));

        obstacleVector.add(new Rectangle(3*30, 7*30, 30, 30));
        obstacleVector.add(new Rectangle(5*30, 7*30, 30, 30));
        obstacleVector.add(new Rectangle(11*30, 7*30, 30, 30));
        obstacleVector.add(new Rectangle(13*30, 7*30, 30, 30));

        obstacleVector.add(new Rectangle(30, 8*30, 30, 30));
        obstacleVector.add(new Rectangle(2*30, 8*30, 30, 30));
        obstacleVector.add(new Rectangle(3*30, 8*30, 30, 30));
        obstacleVector.add(new Rectangle(5*30, 8*30, 30, 30));
        obstacleVector.add(new Rectangle(7*30, 8*30, 30, 30));
        obstacleVector.add(new Rectangle(9*30, 8*30, 30, 30));
        obstacleVector.add(new Rectangle(11*30, 8*30, 30, 30));
        obstacleVector.add(new Rectangle(13*30, 8*30, 30, 30));
        obstacleVector.add(new Rectangle(14*30, 8*30, 30, 30));
        obstacleVector.add(new Rectangle(15*30, 8*30, 30, 30));

        obstacleVector.add(new Rectangle(7*30, 9*30, 30, 30));
        obstacleVector.add(new Rectangle(9*30, 9*30, 30, 30));

        obstacleVector.add(new Rectangle(30, 10*30, 30, 30));
        obstacleVector.add(new Rectangle(2*30, 10*30, 30, 30));
        obstacleVector.add(new Rectangle(3*30, 10*30, 30, 30));
        obstacleVector.add(new Rectangle(5*30, 10*30, 30, 30));
        obstacleVector.add(new Rectangle(7*30, 10*30, 30, 30));
        obstacleVector.add(new Rectangle(8*30, 10*30, 30, 30));
        obstacleVector.add(new Rectangle(9*30, 10*30, 30, 30));
        obstacleVector.add(new Rectangle(11*30, 10*30, 30, 30));
        obstacleVector.add(new Rectangle(13*30, 10*30, 30, 30));
        obstacleVector.add(new Rectangle(14*30, 10*30, 30, 30));
        obstacleVector.add(new Rectangle(15*30, 10*30, 30, 30));

        obstacleVector.add(new Rectangle(3*30, 11*30, 30, 30));
        obstacleVector.add(new Rectangle(5*30, 11*30, 30, 30));
        obstacleVector.add(new Rectangle(11*30, 11*30, 30, 30));
        obstacleVector.add(new Rectangle(13*30, 11*30, 30, 30));

        obstacleVector.add(new Rectangle(30, 12*30, 30, 30));
        obstacleVector.add(new Rectangle(2*30, 12*30, 30, 30));
        obstacleVector.add(new Rectangle(3*30, 12*30, 30, 30));
        obstacleVector.add(new Rectangle(5*30, 12*30, 30, 30));
        obstacleVector.add(new Rectangle(7*30, 12*30, 30, 30));
        obstacleVector.add(new Rectangle(8*30, 12*30, 30, 30));
        obstacleVector.add(new Rectangle(9*30, 12*30, 30, 30));
        obstacleVector.add(new Rectangle(11*30, 12*30, 30, 30));
        obstacleVector.add(new Rectangle(13*30, 12*30, 30, 30));
        obstacleVector.add(new Rectangle(14*30, 12*30, 30, 30));
        obstacleVector.add(new Rectangle(15*30, 12*30, 30, 30));

        obstacleVector.add(new Rectangle(8*30, 13*30, 30, 30));

        obstacleVector.add(new Rectangle(2*30, 14*30, 30, 30));
        obstacleVector.add(new Rectangle(3*30, 14*30, 30, 30));
        obstacleVector.add(new Rectangle(5*30, 14*30, 30, 30));
        obstacleVector.add(new Rectangle(6*30, 14*30, 30, 30));
        obstacleVector.add(new Rectangle(8*30, 14*30, 30, 30));
        obstacleVector.add(new Rectangle(10*30, 14*30, 30, 30));
        obstacleVector.add(new Rectangle(11*30, 14*30, 30, 30));
        obstacleVector.add(new Rectangle(13*30, 14*30, 30, 30));
        obstacleVector.add(new Rectangle(14*30, 14*30, 30, 30));

        obstacleVector.add(new Rectangle(3*30, 15*30, 30, 30));
        obstacleVector.add(new Rectangle(13*30, 15*30, 30, 30));

        obstacleVector.add(new Rectangle(30, 16*30, 30, 30));
        obstacleVector.add(new Rectangle(3*30, 16*30, 30, 30));
        obstacleVector.add(new Rectangle(5*30, 16*30, 30, 30));
        obstacleVector.add(new Rectangle(7*30, 16*30, 30, 30));
        obstacleVector.add(new Rectangle(8*30, 16*30, 30, 30));
        obstacleVector.add(new Rectangle(9*30, 16*30, 30, 30));
        obstacleVector.add(new Rectangle(11*30, 16*30, 30, 30));
        obstacleVector.add(new Rectangle(13*30, 16*30, 30, 30));
        obstacleVector.add(new Rectangle(15*30, 16*30, 30, 30));

        obstacleVector.add(new Rectangle(5*30, 17*30, 30, 30));
        obstacleVector.add(new Rectangle(8*30, 17*30, 30, 30));
        obstacleVector.add(new Rectangle(11*30, 17*30, 30, 30));

        obstacleVector.add(new Rectangle(2*30, 18*30, 30, 30));
        obstacleVector.add(new Rectangle(3*30, 18*30, 30, 30));
        obstacleVector.add(new Rectangle(4*30, 18*30, 30, 30));
        obstacleVector.add(new Rectangle(5*30, 18*30, 30, 30));
        obstacleVector.add(new Rectangle(6*30, 18*30, 30, 30));
        obstacleVector.add(new Rectangle(8*30, 18*30, 30, 30));
        obstacleVector.add(new Rectangle(10*30, 18*30, 30, 30));
        obstacleVector.add(new Rectangle(11*30, 18*30, 30, 30));
        obstacleVector.add(new Rectangle(12*30, 18*30, 30, 30));
        obstacleVector.add(new Rectangle(13*30, 18*30, 30, 30));
        obstacleVector.add(new Rectangle(14*30, 18*30, 30, 30));
    }

    static Vector<Rectangle> obstacleVector;

}
