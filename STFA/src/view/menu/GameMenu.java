package view.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class GameMenu extends JDialog implements ActionListener{
	JButton resume;
	JButton settings;
	JButton exit;
	
	public GameMenu(JFrame parent){
		super(parent, "GameMenu", true);
		//
		resume = new JButton("Resume");
		settings = new JButton("Settings");
		exit = new JButton("Exit game");
		//
	    resume.addActionListener(this);
		exit.addActionListener(this);
		//
	    JPanel p = new JPanel();
	    Box bv = Box.createVerticalBox();
		//
	    p.add(bv);
	    getContentPane().add(p);
		//
	    bv.add(resume);
	    bv.add(settings); 
	    bv.add(exit);
		//
	    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	    pack();
		//
	    this.move(parent.getX()+parent.getWidth()/2, parent.getY()+ parent.getHeight()/2);
	    setVisible(true);
	  }
	
	  public void actionPerformed(ActionEvent e) {

		  //RESUME
		  if(e.getSource() == resume){

		  }
		  //EXIT
		  if(e.getSource() == exit){
			  System.exit(0);
		  }
		  //SETTINGS
		  if(e.getSource() == settings){

		  }

		  setVisible(false);
		  dispose();
	  }
}
