import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;

public class App extends JPanel {
    JFrame frame1 = new JFrame("FLAPPY BIRD 💕❤️🦜🦜🦜");
    JButton button;
    JPanel pan;
    JLabel lab;
    Container con;
    Font tite = new Font("Times New Roman", Font.BOLD, 60); // Slightly smaller for default view

    App() throws LineUnavailableException {
        // Use BorderLayout instead of null layout for responsiveness
        frame1.setLayout(new BorderLayout()); 
        
        button = new JButton("PLAY");
        button.setPreferredSize(new Dimension(150, 80));
        button.setBackground(Color.LIGHT_GRAY);
        button.setFont(new Font("Arial", Font.BOLD, 20));

        // Center Panel for the button
        JPanel buttonPanel = new JPanel(new GridBagLayout()); // GridBagLayout centers components perfectly
        buttonPanel.setOpaque(false);
        buttonPanel.add(button);

        lab = new JLabel("FLAPPY BIRD", SwingConstants.CENTER);
        lab.setFont(tite);
        
        pan = new JPanel(new BorderLayout());
        pan.setBackground(Color.cyan);
        pan.add(lab, BorderLayout.CENTER);

        con = frame1.getContentPane();
        con.setBackground(Color.cyan);
        con.add(pan, BorderLayout.NORTH);
        con.add(buttonPanel, BorderLayout.CENTER);

        frame1.setSize(800, 600); // Sensible starting size
        frame1.setResizable(true); // ALLOW RESIZING
        frame1.setLocationRelativeTo(null);
        
        Title title = new Title();
        button.addActionListener(title);
        
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        // Ensure UI is created on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                new App();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
        });
    }

    public void Gamescreen() throws LineUnavailableException {
        JFrame frame = new JFrame("FLAPPY BIRD 💕❤️🦜🦜🦜");
        frame1.dispose(); // Close the menu frame instead of just hiding it

        Background background = new Background();
        frame.add(background);  
        
        // This is the magic for responsiveness:
        frame.pack(); // Sizes the frame so contents are at their PreferredSize
        frame.setResizable(true); // ALLOW RESIZING IN GAME
        frame.setLocationRelativeTo(null);
        
        background.requestFocus();
        frame.setVisible(true);

        try {
            Musico.Run("Samplegame music.wav");
        } catch (Exception e) {
            System.out.println("Music file not found.");
        }
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public class Title implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            try {
                Gamescreen();
            } catch (LineUnavailableException ex) {
                ex.printStackTrace();
            }
        }
    }
}