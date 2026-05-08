import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Background extends JPanel implements ActionListener, KeyListener {
    // We remove the static width/height and use the panel's actual dimensions
    JFrame frame; 
    
    // Variables for Images
    Image Top, mid, birds, bot, ice, golds, bronzes, silvers, platinums;

    // Classes remain mostly the same, but we initialize coordinates relatively
    class Bird {
        int X, Y, bwidth, bheight;
        Image bmge;
        Bird(Image bmge) {
            this.bmge = bmge;
            this.bwidth = 40;
            this.bheight = 30;
        }
    }

    class Pipe {
        int Xp, Yp, Pwidth, Pheight;
        Image pimg;
        boolean succ = false;
        Pipe(Image pimg) {
            this.pimg = pimg;
            this.Pwidth = 100;
            this.Pheight = 472;
        }
    }

    // Game Objects
    public Bird bird;
    private ArrayList<Pipe> pipes;
    private Timer time;
    public Timer pipesTimer;
    
    // Physics
    private int speedP = -9;
    public int speed = 0;
    int opp = 1;
    double score = 0.0;
    public boolean gameOver = false;

    Background() {
        // Use a preferred size for the initial launch, but allow resizing
        setPreferredSize(new Dimension(800, 600));
        setFocusable(true);
        addKeyListener(this);

        // Image Loading (Keep your existing paths)
        Top = new ImageIcon(getClass().getResource("./tube.png")).getImage();
        mid = new ImageIcon(getClass().getResource("./tube2.png")).getImage();
        birds = new ImageIcon(getClass().getResource("./bird.png")).getImage();
        bot = new ImageIcon(getClass().getResource("./iceBackground.jpg")).getImage();
        ice = new ImageIcon(getClass().getResource("./try.png")).getImage();
        golds = new ImageIcon(getClass().getResource("./gold.png")).getImage();
        bronzes = new ImageIcon(getClass().getResource("./bronze.png")).getImage();
        silvers = new ImageIcon(getClass().getResource("./Silver.png")).getImage();
        platinums = new ImageIcon(getClass().getResource("./Platinum.png")).getImage();

        bird = new Bird(birds);
        resetBirdPosition(); // Initial position
        
        pipes = new ArrayList<>();
        time = new Timer(1000/60, this);
        pipesTimer = new Timer(1600, e -> pipeimport());

        pipesTimer.start();
        time.start();
    }

    // Helper to keep bird relative to screen
    private void resetBirdPosition() {
        bird.X = getWidth() > 0 ? getWidth() / 6 : 100;
        bird.Y = getHeight() > 0 ? getHeight() / 2 : 300;
    }

    public void pipeimport() {
        // Use current getHeight() instead of fixed 700
        int h = getHeight() > 0 ? getHeight() : 600;
        int w = getWidth() > 0 ? getWidth() : 800;

        int randPipeY = (int) (0 - 472/4 - Math.random() * (472/2));
        int opening = h / 4; // The gap between pipes scales with screen height

        Pipe topPipe = new Pipe(Top);
        topPipe.Xp = w;
        topPipe.Yp = randPipeY;
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(mid);
        bottomPipe.Xp = w;
        bottomPipe.Yp = topPipe.Yp + 472 + opening;
        pipes.add(bottomPipe);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        // Always draw based on current getWidth() and getHeight()
        g.drawImage(bot, 0, 0, getWidth(), getHeight(), null);
        g.drawImage(bird.bmge, bird.X, bird.Y, bird.bwidth, bird.bheight, null);

        for (Pipe pipe : pipes) {
            g.drawImage(pipe.pimg, pipe.Xp, pipe.Yp, pipe.Pwidth, pipe.Pheight, null);
        }

        g.setColor(Color.darkGray);
        g.setFont(new Font("Arial", Font.BOLD, getWidth() / 40)); // Scale font size!

        if (gameOver) {
            g.drawImage(ice, 0, 0, getWidth(), getHeight(), null);
            g.drawString("Score: " + (int) score, getWidth() / 3, getHeight() / 2);
            g.drawString("PRESS 'S' TO RESTART", getWidth() / 3, getHeight() / 2 + 50);
        } else {
            g.drawString("Score: " + (int) score, 20, 40);
        }
    }

    public void acc() {
        speed += opp;
        bird.Y += speed;
        bird.Y = Math.max(bird.Y, 0);

        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.Xp += speedP;

            if (!pipe.succ && bird.X > pipe.Xp + pipe.Pwidth) {
                pipe.succ = true;
                score += 0.5;
            }

            if (Intersection(bird, pipe)) gameOver = true;
        }

        // Check if bird fell off screen
        if (bird.Y > getHeight()) gameOver = true;
    }

    public boolean Intersection(Bird a, Pipe b) {
        return a.X < b.Xp + b.Pwidth &&
               a.X + a.bwidth > b.Xp &&
               a.Y < b.Yp + b.Pheight &&
               a.Y + a.bheight > b.Yp;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            acc();
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_UP) {
            speed = -9;
        }
        if (code == KeyEvent.VK_S && gameOver) {
            score = 0;
            resetBirdPosition();
            speed = 0;
            pipes.clear();
            gameOver = false;
            time.start();
            pipesTimer.start();
        }
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
}