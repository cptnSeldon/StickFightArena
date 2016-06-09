import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class PausePane extends JPanel {

     private JLabel label;

     public PausePane() {
         setVisible(true);
         setOpaque(false);
         setBackground(new Color(0, 0, 0, 128));
         setLayout(new GridBagLayout());

         label = new JLabel("Paused");
         label.setHorizontalAlignment(JLabel.CENTER);
         label.setVerticalAlignment(JLabel.CENTER);
         Font font = label.getFont();
         font = font.deriveFont(Font.BOLD, 48f);
         label.setFont(font);
         label.setForeground(Color.WHITE);
         add(label);
     }

     @Override
     protected void paintComponent(Graphics g) {
         super.paintComponent(g);
         g.setColor(getBackground());
         g.fillRect(0, 0, getWidth(), getHeight());
     }

 }
