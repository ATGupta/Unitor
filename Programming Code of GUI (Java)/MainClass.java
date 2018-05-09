
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;

import sun.misc.FloatingDecimal.BinaryToASCIIConverter;

import com.sun.javafx.fxml.expression.BinaryExpression;

public class MainClass {
	
	private static String userName="iot.commander23@gmail.com";
	private static String password="internetofthings";
	private JOptionPane op;

	public MainClass(String subject, String messageText, String sendTo) {
		
		Thread th=new Thread(new Runnable(){
			public void run() {
				op=new JOptionPane();
				op.showOptionDialog(null, "Success","Wait...", JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE, null, new Object[]{}, null);
			}
		});
		th.start();
		
		Properties prop=new Properties();
		prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable", "true");
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", "587");
		
		Session session = Session.getInstance(prop, new javax.mail.Authenticator(){
			protected javax.mail.PasswordAuthentication getPasswordAuthentication()
			{
				return new javax.mail.PasswordAuthentication(userName, password);
			}
		});
		
			
		try {
			Message message=new MimeMessage(session);
			message.setFrom(new InternetAddress(userName));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(sendTo));
			message.setSubject(subject);
			
			messageText=encryptEdon80(messageText);
			String st2="";
			for(int i=0;i<messageText.length();i++){
				String a=Integer.toBinaryString((int)(messageText.charAt(i)));
				for(int j=a.length();j<8;j++)
					a='0'+a;
				st2+=a;
			}messageText=st2;
			
			message.setText(messageText);
			
			Transport.send(message);
			
			op.getRootFrame().dispose();
			th.stop();
			JOptionPane.showMessageDialog(
					null,
					"Command Sent.",
					"Success",
					JOptionPane.OK_OPTION);
		} catch (MessagingException e) {
			
			op.getRootFrame().dispose();
			th.stop();
			JOptionPane.showMessageDialog(
					null,
					"Command not sent. Try again",
					"Error",
					JOptionPane.OK_OPTION);
			e.printStackTrace();
		}
	}

	private String encryptEdon80(String messageText) {
		String st="";
		int posAtST = 0;
		boolean repeat;
		
		do{
			repeat=false;
			try {
				int character=0;
				BufferedReader br=new BufferedReader(new FileReader("Keystream.txt"));
				for(;;){
					if(posAtST>=messageText.length()*8)break;
					
					int x=br.read();
					
					if(x==-1){
						repeat = true;
						break;
					}
					
					x=x-48;
					
					char c = messageText.charAt(posAtST/8);
					int i = (int)c;
					int pos = 7 - (posAtST%8);
					
					int count=0;
					int r=0;
					while(count<=pos){
						r=i%2;
						i=i/2;
						count++;
					}
					
					if((r==1&&x==0)||(r==0&&x==1))r=1;
					else r=0;
					
					character=character*2+r;
					
					posAtST++;
					
					if(posAtST%8==0){
						st=st+(char)character;
						character=0;
					}
				}
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(null, "There was an error encrypting the message.\n" +
						"Try again.", "Error", JOptionPane.ERROR_MESSAGE);
				break;
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "There was an error encrypting the message.\n" +
						"Try again.", "Error", JOptionPane.ERROR_MESSAGE);
				break;
			}
		}while(repeat);
		
		return st;
	}
}
