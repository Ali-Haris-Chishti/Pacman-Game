import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Optional;
import java.util.Random;
import java.util.Vector;

public class Game extends Canvas implements Runnable, KeyListener {

    JFrame frame;
    int score = 0;

    Vector<Food> foodVector = new Vector<>();

    static Game current;

    private Game(){
        backgroundImage = new ImageIcon("images/pac man map.png").getImage();
        foodImage = new ImageIcon("images/food.png").getImage();
        player = new Player(2, 9);
        addFoodToGame();
        initializeEnemies();
        Dimension dimension = new Dimension(510, 680);
        setPreferredSize(dimension);
        addKeyListener(this);  // Add the KeyListener to the Canvas
        setFocusable(true);    // Ensure the Canvas is focusable
        requestFocusInWindow(); // Request focus for the Canvas
        repaint();
    }

    private void render(){
        BufferStrategy bufferStrategy = getBufferStrategy();
        if (bufferStrategy == null){
            createBufferStrategy(3);
            System.out.println("Returning");
            return;
        }
        Graphics graphics = bufferStrategy.getDrawGraphics();
        graphics.drawImage(backgroundImage, 0, 50, 510, 630, null);
        if (alt)
            graphics.drawImage(player.image1, player.posX, player.posY + 50, 30, 30, null);
        else
            graphics.drawImage(player.image2, player.posX, player.posY + 50, 30, 30, null);
        if (foodVector.isEmpty())
            addFoodToGame();
        Iterator<Food> iterator = foodVector.iterator();
        while (iterator.hasNext()) {
            Food food = iterator.next();
            if (food.rectangle.intersects(player.rectangle)) {
                iterator.remove();
                score += 10;
            }
            graphics.drawImage(foodImage, food.posX, food.posY + 50, 30, 30, null);
        }
        for (Enemy enemy: enemies)
            graphics.drawImage(enemy.image, enemy.posX, enemy.posY + 50, 30, 30, null);
        if (bonus != null)
            graphics.drawImage(bonus.image, bonus.posX, bonus.posY + 50, 30, 30, null);

        graphics.setColor(Color.WHITE);
        graphics.fillRect(0,0, 510, 50);
        graphics.setColor(Color.BLACK);
        graphics.drawString("SCORE: " + score, 100, 20);
        graphics.drawString("LIVES: " + lives, 250, 20);
        graphics.drawString(String.format("TIME: %.2f", currentTime), 400, 20);
        graphics.dispose();
        bufferStrategy.show();
    }

    int lives = 5;

    boolean alt = true;
    int counter = 0;
    private boolean isRunning;

    private Thread thread;

    public static void main(String[] args) {
        current = new Game();
        current.frame = new JFrame();
        current.frame.setTitle("PAC MAN");
        current.frame.add(current);
        current.frame.setResizable(false);
        current.frame.pack();
        current.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        current.frame.setVisible(true);
        current.frame.setLocationRelativeTo(null);
        current.start();
        System.out.println("Game Started");
    }

    public void start(){
        if (isRunning)
            return;
        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    public void stop(){
        if (isRunning)
            isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    double currentTime;
    int bonusCounter = 0;
    Bonus bonus = null;
    @Override
    public void run() {
        long b = System.currentTimeMillis();
        while (true){
            while (isRunning){
                long a = System.currentTimeMillis();
                if (a - b < 10) // Movement after every 8 millisecond, can be changed to change game speed
                    continue;
                else {
                    b = System.currentTimeMillis();
                    currentTime += 0.01;
                }
                bonusCounter = (bonusCounter + 1)%500;
                if (bonusCounter == 499 && bonus == null){
                    Random random = new Random();
                    int r = random.nextInt(1, 5);
                    if (r == 1){
                        System.out.println("Producing bonus");
                        int i = random.nextInt(0, 4);
                        bonus = new Bonus(enemies[i].posX, enemies[i].posY);
                    }
                }
                player.move();
                for (Enemy enemy: enemies)
                    enemy.move();
                render();

                // for player animation
                counter = (counter + 1) % 30;
                if (counter == 0)
                    alt = !alt;
            }
        }
    }

    Image backgroundImage;
    Image foodImage;

    Player player;

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_LEFT:
                player.changeDirection(Direction.LEFT);
                break;
            case KeyEvent.VK_RIGHT:
                player.changeDirection(Direction.RIGHT);
                break;
            case KeyEvent.VK_UP:
                player.changeDirection(Direction.UP);
                break;
            case KeyEvent.VK_DOWN:
                player.changeDirection(Direction.DOWN);
                break;
            case KeyEvent.VK_SPACE:
                isRunning = !isRunning;
                break;
                case KeyEvent.VK_ESCAPE:
                    isRunning = false;
                    frame.setVisible(false);
                    StartFrame.main(null);
                    break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    Enemy [] enemies = new Enemy[4];
    private void initializeEnemies(){
        for (int i = 0; i < enemies.length; i++){
            int n = i + 1;
            enemies[i] = new Enemy("images/enemy " + n + ".png", player);
        }
    }

    void enemyKilled(){
        score += 50;
    }

    void gameOver(){
        isRunning = false;
        frame.setVisible(false);
        enterName();
    }

    public void enterName() {
        JFrame frame = new JFrame("Enter High Score");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new GridLayout(3, 2));

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    writeScoreToFile(nameField.getText(), score);
                    frame.setVisible(false);
                    System.exit(0);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        // Add components to the frame
        frame.add(nameLabel);
        frame.add(nameField);
        frame.add(submitButton);
        frame.setVisible(true);
    }

    private void writeScoreToFile(String name, int score) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("high score.txt", true))) {
            writer.write(name);
            writer.newLine();
            writer.write(String.valueOf(score));
            writer.newLine();
        }
    }

    private void addFoodToGame(){
        for (int i = 1; i < 8; i++)
            foodVector.add(new Food(i, 1));
        for (int i = 9; i < 16; i++)
            foodVector.add(new Food(i, 1));

        foodVector.add(new Food(1, 2));
        foodVector.add(new Food(4, 2));
        foodVector.add(new Food(7, 2));
        foodVector.add(new Food(9, 2));
        foodVector.add(new Food(12, 2));
        foodVector.add(new Food(15, 2));

        for (int i = 1; i < 16; i++)
            foodVector.add(new Food(i, 3));

        foodVector.add(new Food(1, 4));
        foodVector.add(new Food(4, 4));
        foodVector.add(new Food(6, 4));
        foodVector.add(new Food(10, 4));
        foodVector.add(new Food(12, 4));
        foodVector.add(new Food(15, 4));

        foodVector.add(new Food(1, 5));
        foodVector.add(new Food(2, 5));
        foodVector.add(new Food(3, 5));
        foodVector.add(new Food(4, 5));
        foodVector.add(new Food(6, 5));
        foodVector.add(new Food(7, 5));
        foodVector.add(new Food(9, 5));
        foodVector.add(new Food(10, 5));
        foodVector.add(new Food(12, 5));
        foodVector.add(new Food(13, 5));
        foodVector.add(new Food(14, 5));
        foodVector.add(new Food(15, 5));

        foodVector.add(new Food(4, 6));
        foodVector.add(new Food(7, 6));
        foodVector.add(new Food(9, 6));
        foodVector.add(new Food(12, 6));

        foodVector.add(new Food(4, 7));
        foodVector.add(new Food(12, 7));

        foodVector.add(new Food(4, 8));
        foodVector.add(new Food(12, 8));

        foodVector.add(new Food(4, 9));
        foodVector.add(new Food(5, 9));
        foodVector.add(new Food(11, 9));
        foodVector.add(new Food(12, 9));

        foodVector.add(new Food(4, 10));
        foodVector.add(new Food(12, 10));

        foodVector.add(new Food(4, 11));
        foodVector.add(new Food(12, 11));

        foodVector.add(new Food(4, 12));
        foodVector.add(new Food(6, 12));
        foodVector.add(new Food(10, 12));
        foodVector.add(new Food(12, 12));

        for (int i = 1; i < 8; i++)
            foodVector.add(new Food(i, 13));
        for (int i = 9; i < 16; i++)
            foodVector.add(new Food(i, 13));

        foodVector.add(new Food(1, 14));
        foodVector.add(new Food(4, 14));
        foodVector.add(new Food(7, 14));
        foodVector.add(new Food(9, 14));
        foodVector.add(new Food(12, 14));
        foodVector.add(new Food(15, 14));

        foodVector.add(new Food(1, 15));
        foodVector.add(new Food(2, 15));
        for (int i = 4; i < 13; i++)
            foodVector.add(new Food(i, 15));
        foodVector.add(new Food(14, 15));
        foodVector.add(new Food(15, 15));

        foodVector.add(new Food(2, 16));
        foodVector.add(new Food(4, 16));
        foodVector.add(new Food(6, 16));
        foodVector.add(new Food(10, 16));
        foodVector.add(new Food(12, 16));
        foodVector.add(new Food(14, 16));

        foodVector.add(new Food(1, 17));
        foodVector.add(new Food(2, 17));
        foodVector.add(new Food(3, 17));
        foodVector.add(new Food(4, 17));
        foodVector.add(new Food(6, 17));
        foodVector.add(new Food(7, 17));
        foodVector.add(new Food(9, 17));
        foodVector.add(new Food(10, 17));
        foodVector.add(new Food(12, 17));
        foodVector.add(new Food(13, 17));
        foodVector.add(new Food(14, 17));
        foodVector.add(new Food(15, 17));

        foodVector.add(new Food(1, 18));
        foodVector.add(new Food(7, 18));
        foodVector.add(new Food(9, 18));
        foodVector.add(new Food(15, 18));

        for (int i = 1; i < 16; i++)
            foodVector.add(new Food(i, 19));
    }
}
