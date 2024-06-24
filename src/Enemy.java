import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Enemy {
    int speed = 1;
    Image image;
    Rectangle rectangle;
    int posX, posY;

    Player player;

    public Enemy(String path, Player player){
        this.player = player;
        posX = 30 * 8;
        posY = 30 * 9;

        image = new ImageIcon(path).getImage();
        rectangle = new Rectangle(posX, posY, 30, 30);

        directions[0] = Direction.UP;
        directions[1] = Direction.RIGHT;
        directions[2] = Direction.DOWN;
        directions[3] = Direction.LEFT;
    }
    Direction direction = Direction.UP;

    public void move(){
        Rectangle next = rectangle;
        switch (direction){
            case UP -> {
                next = new Rectangle(posX, posY - speed, 30, 30);
            }
            case RIGHT -> {
                next = new Rectangle(posX + speed, posY, 30, 30);
            }
            case DOWN -> {
                next = new Rectangle(posX, posY + speed, 30, 30);
            }
            case LEFT -> {
                next = new Rectangle(posX - speed, posY, 30, 30);
            }
        }
        if (isSafe(next)) {
            rectangle = next;
            posX = rectangle.x;
            posY = rectangle.y;
            // if intersects, player or enemy, one loses life
            if (rectangle.intersects(player.rectangle)){
                if (player.direction == direction){
                    Game.current.enemyKilled();
                    posX = 30 * 8;
                    posY = 30 * 9;
                    rectangle = new Rectangle(posX, posY, 30, 30);
                }
                else {
                    player.lifeLost();
                    Game.current.lives--;
                    if (Game.current.lives == 0)
                        Game.current.gameOver();
                }
            }
        }
        else{
            direction = getRandomDirection();
        }
    }

    public boolean isSafe(Rectangle next){
        for (Rectangle rect: Player.obstacleVector){
            if (rect.intersects(next))
                return false;
        }
        return true;
    }

    Direction [] directions = new Direction[4];

    Direction getRandomDirection(){
        Random random = new Random();
        return directions[random.nextInt(0, 4)];
    }
}
