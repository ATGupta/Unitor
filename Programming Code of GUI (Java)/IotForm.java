import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class IotForm {
	
	private int height;
	private int width;
	private JPanel parent;
	private JFrame f1;
	private JPanel properties,master;
	private JPanel P1;
	private JPanel P2;
	private JButton on1,on2,off1,off2,fan1,fan2;
	private JLabel l1,l2;
	
	public IotForm(){

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		width = (int) screenSize.getWidth();
		height = (int) screenSize.getHeight();
		
		f1=new JFrame();
		width=height=230;
		f1.setSize(width, height);
		f1.setLayout(new FlowLayout());
		
		parent=new JPanel();
		
		fan1=new JButton("FAN1");
		fan2=new JButton("FAN2");
		
		master=new JPanel();
		master.setLayout(new BoxLayout(master,BoxLayout.Y_AXIS));
		
		master.setMaximumSize(new Dimension(width/3, height));
		master.setMinimumSize(new Dimension(width/3, height));
		master.setPreferredSize(new Dimension(width/3, height));
		master.setBackground(new Color(200,200,255) );
		
		master.add(fan1);
		master.add(fan2);
		
		on1=new JButton("ON");
		off1=new JButton("OFF"); 
		
		l1=new JLabel("FAN 1");l1.setVisible(false);
		l2=new JLabel("FAN 2");l2.setVisible(false);
		
		properties=new JPanel();
		properties.setLayout(new BoxLayout(properties, BoxLayout.Y_AXIS));
		properties.setMaximumSize(new Dimension(2*width/3, height));
		properties.setMinimumSize(new Dimension(2*width/3, height));
		properties.setPreferredSize(new Dimension(2*width/3, height));
		
		P1=new JPanel();
		P1.setLayout(new FlowLayout());
		
		P1.add(on1);
		P1.add(off1);
		
		P1.setVisible(false);
		
		on2=new JButton("ON");
		off2=new JButton("OFF");
		
		P2=new JPanel();
		P2.setLayout(new FlowLayout());

		P2.add(on2);
		P2.add(off2);
		
		P2.setVisible(false);
		
		actionlisteners();
		
		parent.add(master);
		parent.add(properties);
		
		properties.add(l1);
		properties.add(l2);
		properties.add(P1);
		properties.add(P2);
		
		f1.add(parent);
		f1.setVisible(true);
		f1.setDefaultCloseOperation(f1.EXIT_ON_CLOSE);
	}
	private void actionlisteners() {
		
		fan1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				l2.setVisible(false);
				l1.setVisible(true);
				P2.setVisible(false);
				P1.setVisible(true);
			}
		});
		
		fan2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				l1.setVisible(false);
				l2.setVisible(true);
				P1.setVisible(false);
				P2.setVisible(true);
			}
		});
		
		on1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				new MainClass("command","on fan1","iot.device23@gmail.com");
			}
		});
		off1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				new MainClass("command","off fan1","iot.device23@gmail.com");
			}
		});
		on2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				new MainClass("command","on fan2","iot.device23@gmail.com");
			}
		});
		off2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				new MainClass("command","off fan2","iot.device23@gmail.com");
			}
		});
	}
	public static void main(String args[]){
		new IotForm();
	}
}
