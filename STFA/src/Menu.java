
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Menu extends JDialog implements ActionListener{
	JButton resume;
	JButton settings;
	JButton exit;
	
	public Menu(JFrame parent){
		super(parent, "Menu", true);
		resume = new JButton("Resume");
		settings = new JButton("Settings");
		exit = new JButton("Exit game");
	    resume.addActionListener(this);
	    JPanel p = new JPanel();
	    Box bv = Box.createVerticalBox();
	    p.add(bv);
	    getContentPane().add(p);
	    bv.add(resume);
	    bv.add(settings); 
	    bv.add(exit); 
	    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	    pack(); 
	    this.move(parent.getX()+parent.getWidth()/2, parent.getY()+ parent.getHeight()/2);
	    setVisible(true);
	  }
	
	  public void actionPerformed(ActionEvent e) {
	    setVisible(false); 
	    dispose(); 
	  }
}
