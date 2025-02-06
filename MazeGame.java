import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class MazeGame extends JPanel implements KeyListener {
    private static final int TILE_SIZE = 20;
    private static final int ROWS = 20;
    private static final int COLS = 20;
    private static final int PLAYER_SIZE = 10;
    private int[][] maze;
    private int playerX, playerY;
    private int goalX, goalY;
    private boolean levelComplete = false;

    public MazeGame() {
        setPreferredSize(new Dimension(COLS * TILE_SIZE, ROWS * TILE_SIZE));
        setFocusable(true);
        addKeyListener(this);
        generateMaze();
    }

    private void generateMaze() {
        Random rand = new Random();
        maze = new int[ROWS][COLS];
        
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                maze[r][c] = rand.nextInt(3) == 0 ? 1 : 0; // 1 = wall, 0 = path
            }
        }
        
        playerX = 1;
        playerY = 1;
        maze[playerY][playerX] = 0;
        
        goalX = COLS - 2;
        goalY = ROWS - 2;
        maze[goalY][goalX] = 0;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (maze[r][c] == 1) {
                    g.setColor(Color.BLACK);
                    g.fillRect(c * TILE_SIZE, r * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }
        }
        
        g.setColor(Color.RED);
        g.fillRect(playerX * TILE_SIZE + (TILE_SIZE - PLAYER_SIZE) / 2, 
                   playerY * TILE_SIZE + (TILE_SIZE - PLAYER_SIZE) / 2, 
                   PLAYER_SIZE, PLAYER_SIZE);
        
        g.setColor(Color.GREEN);
        g.fillRect(goalX * TILE_SIZE + 5, goalY * TILE_SIZE + 5, 10, 10);
        
        if (levelComplete) {
            g.setColor(Color.BLUE);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString("Level Complete!", COLS * TILE_SIZE / 3, ROWS * TILE_SIZE / 2);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (levelComplete) {
            levelComplete = false;
            generateMaze();
            repaint();
            return;
        }
        
        int newX = playerX;
        int newY = playerY;
        
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP: newY--; break;
            case KeyEvent.VK_DOWN: newY++; break;
            case KeyEvent.VK_LEFT: newX--; break;
            case KeyEvent.VK_RIGHT: newX++; break;
        }
        
        if (newX >= 0 && newX < COLS && newY >= 0 && newY < ROWS) {
            if (maze[newY][newX] == 0) {
                playerX = newX;
                playerY = newY;
                if (playerX == goalX && playerY == goalY) {
                    levelComplete = true;
                }
            } else {
                generateMaze(); // Reset level if player hits a wall
            }
        }
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Maze Game");
        MazeGame game = new MazeGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
