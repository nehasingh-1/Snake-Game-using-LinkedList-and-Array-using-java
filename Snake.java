import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

class GameFrame extends JFrame {
    GameFrame() {
        this.add(new GamePanel());
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}

class GamePanel extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH = 750;
    static final int SCREEN_HEIGHT = 450;
    static final int UNIT_SIZE = 50;
    static int DELAY;
    final int[] head = new int[2]; // [x, y]
    LinkedList<int[]> body = new LinkedList<>(); // stores the rest of the snake's body
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());

        // Select difficulty
        String[] options = {"Easy", "Medium", "Hard"};
        int choice = JOptionPane.showOptionDialog(
            this,
            "Select Difficulty Level",
            "Difficulty",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );

        switch (choice) {
            case 0:
                DELAY = 160; // Easy
                break;
            case 1:
                DELAY = 100; // Medium
                break;
            case 2:
                DELAY = 40;  // Hard
                break;
            default:
                DELAY = 100; // Default to medium
                break;
        }

        startGame();
    }

    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
        // Initialize snake's body
        head[0] = 0;
        head[1] = 0;
        for (int i = 1; i <= 6; i++) {
            body.add(new int[]{head[0] - i * UNIT_SIZE, head[1]});
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            // Draw head
            g.setColor(Color.green);
            g.fillRect(head[0], head[1], UNIT_SIZE, UNIT_SIZE);

            // Draw body
            for (int[] part : body) {
                g.setColor(new Color(45, 180, 0));
                g.fillRect(part[0], part[1], UNIT_SIZE, UNIT_SIZE);
            }

            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());
        } else {
            gameOver(g);
        }
    }
    public void newApple() {
        appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }
    public void move() {
        // Add current head to body
        body.addFirst(new int[]{head[0], head[1]});
        // Update head position
        switch (direction) {
            case 'U':
                head[1] -= UNIT_SIZE;
                break;
            case 'D':
                head[1] += UNIT_SIZE;
                break;
            case 'L':
                head[0] -= UNIT_SIZE;
                break;
            case 'R':
                head[0] += UNIT_SIZE;
                break;
        }
        // Remove last part of the body if not eating apple
        if (applesEaten != body.size()) {
            body.removeLast();
        }
    }
    public void checkApple() {
        if ((head[0] == appleX) && (head[1] == appleY)) {
            applesEaten++;
            newApple();
        }
    }
    public void checkCollisions() {
        // Check if head collides with body
        for (int[] part : body) {
            if (head[0] == part[0] && head[1] == part[1]) {
                running = false;
                break;
            }
        }
        // Check if head touches left border
        if (head[0] < 0) {
            running = false;
        }
        // Check if head touches right border
        if (head[0] >= SCREEN_WIDTH) {
            running = false;
        }
        // Check if head touches top border
        if (head[1] < 0) {
            running = false;
        }
        // Check if head touches bottom border
        if (head[1] >= SCREEN_HEIGHT) {
            running = false;
        }
        if (!running) {
            timer.stop();
        }
    }
    public void gameOver(Graphics g) {
        // Score
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());
        // Game Over
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }
    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
public class Snake {
        public static void main(String[] args) {
        new GameFrame();
        }
        }    