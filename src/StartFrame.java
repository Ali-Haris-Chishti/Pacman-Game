import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StartFrame extends JFrame {

    public StartFrame() {
        initUI();
    }

    private void initUI() {
        // Set the layout manager to BoxLayout along the Y_AXIS for vertical alignment
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // Create buttons
        JButton startGameButton = new JButton("Start Game");
        JButton highScoresButton = new JButton("High Scores");
        JButton exitGameButton = new JButton("Exit Game");

        // Align buttons to the center
        startGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        highScoresButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add action listeners to buttons
        startGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onStartGame();
            }
        });

        highScoresButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onShowHighScores();
            }
        });

        exitGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onExitGame();
            }
        });

        // Add buttons to the frame
        add(startGameButton);
        add(Box.createRigidArea(new Dimension(0, 10))); // 10 pixels of space
        add(highScoresButton);
        add(Box.createRigidArea(new Dimension(0, 10))); // 10 pixels of space
        add(exitGameButton);

        // Set the frame properties
        setTitle("Game Menu");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void onStartGame() {
        Game.main(null);
    }

    private void onShowHighScores() {
        showHighScores();
    }

    private void onExitGame() {
        JOptionPane.showMessageDialog(this, "Exiting Game ...");
        System.exit(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StartFrame ex = new StartFrame();
            ex.setVisible(true);
        });
    }

    public void showHighScores() {
        JFrame frame = new JFrame("High Scores");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Read high scores from the file
        List<ScoreEntry> scores = readHighScores();

        // Sort the scores in descending order
        scores.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));

        // Create a JList with a custom model
        DefaultListModel<ScoreEntry> listModel = new DefaultListModel<>();
        for (ScoreEntry entry : scores) {
            listModel.addElement(entry);
        }
        JList<ScoreEntry> scoreList = new JList<>(listModel);

        // Set a custom cell renderer to display each score entry
        scoreList.setCellRenderer(new ListCellRenderer<ScoreEntry>() {
            @Override
            public Component getListCellRendererComponent(JList<? extends ScoreEntry> list, ScoreEntry value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = new JLabel(value.getName() + ": " + value.getScore());
                label.setOpaque(true);

                // Customize the appearance based on selection state
                if (isSelected) {
                    label.setBackground(list.getSelectionBackground());
                    label.setForeground(list.getSelectionForeground());
                } else {
                    label.setBackground(list.getBackground());
                    label.setForeground(list.getForeground());
                }

                // Set padding and border
                label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                return label;
            }
        });

        // Add the JList to a scroll pane
        JScrollPane scrollPane = new JScrollPane(scoreList);
        frame.add(scrollPane);

        // Make the frame visible
        frame.setVisible(true);
    }

    private List<ScoreEntry> readHighScores() {
        List<ScoreEntry> scores = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("high score.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String name = line;
                int score = Integer.parseInt(br.readLine());
                scores.add(new ScoreEntry(name, score));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scores;
    }

    class ScoreEntry {
        private String name;
        private int score;

        public ScoreEntry(String name, int score) {
            this.name = name;
            this.score = score;
        }

        public String getName() {
            return name;
        }

        public int getScore() {
            return score;
        }

        @Override
        public String toString() {
            return name + ": " + score;
        }

    }


}