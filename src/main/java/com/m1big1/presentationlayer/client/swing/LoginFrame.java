package com.m1big1.presentationlayer.client.swing;

//PACKAGES
import java.awt.BorderLayout;
//AWT
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



//JMS
import javax.jms.Destination;
import javax.jms.Session;
import javax.swing.JButton;
import javax.swing.JComboBox;
//SWING
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.m1big1.common.Receiver;
import com.m1big1.common.Transmitter;
import com.m1big1.presentationlayer.client.connection.ImplementationMessageListener;
import com.m1big1.presentationlayer.client.dto.ConnectionDTO;
/**
 * </p>Class which we have the login frame for user</p> 
 * @author Oriel SAMAMA
 *
 */
public class LoginFrame extends JFrame {
    //Graphicals components
    private         JPanel                        centerPanel = new JPanel();
    private         JPanel                        southPanel  = new JPanel();
    private         JLabel                        lblLogin    = new JLabel("Login : ");
    private         JLabel                        lblPassword = new JLabel("Password : ");
    private         JLabel                        lblLanguage = new JLabel("Please choose a language : ");
    private         JTextField 					  jFieldLogin = new JTextField(15);
    private         JPasswordField   			  jFieldPassword = new JPasswordField(15);
    private         JComboBox 					  jFieldLanguage = new JComboBox(new String[] { "FRENCH" , "ENGLISH"});
    private         JButton 					  connectButton;
    //JMS
    private         Transmitter 				  transmitter;
    private         ImplementationMessageListener implementationMessageListener;
    private         Session						  session;
	private static  Logger                        logger = Logger.getLogger(LoginFrame.class);

    /**
     * <p>Constructor of WordManager Frame, we have 2 frame .In left, we have JTree and in left we will can see interactions
     * with JTree</p>
     * @param transmitter
     * @param messageListener
     * @param s
     */
    public LoginFrame(final Transmitter transmitter , ImplementationMessageListener messageListener , Session s) {
        super("WordManager - Connection");
        this.transmitter              = transmitter;
        this.session                  = s;
        implementationMessageListener = messageListener;
        setLayout(new BorderLayout());
        
        //North
	        centerPanel.setLayout(new GridLayout(3 , 2));
	        centerPanel.add(lblLogin);
	        JPanel panel1 = new JPanel();
	        panel1.add(jFieldLogin);
	        centerPanel.add(panel1);
	        centerPanel.add(lblPassword);
	        JPanel panel2 = new JPanel();
	        panel2.add(jFieldPassword);
	        centerPanel.add(panel2);
	        centerPanel.add(lblLanguage);
	        JPanel panel3 = new JPanel();
	        panel3.add(jFieldLanguage);
	        centerPanel.add(panel3);
        add(centerPanel , BorderLayout.CENTER);
        
        //South
            connectButton = new JButton("Connect to WordManager");
            connectButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    try {
                        // Create ConnectionDTO object with information of user
                        ConnectionDTO conDto   = new ConnectionDTO();
                        conDto.setSessionId(null);
                        conDto.setRootNode(null);
                        conDto.setLogin(jFieldLogin.getText());
                        conDto.setPassword(new String(jFieldPassword.getPassword()));
                        conDto.setLanguage((String) jFieldLanguage.getSelectedItem());
                        // Initialise queue with login of user
                        Destination receiveDest = (Destination) session.createQueue(jFieldLogin.getText());
                        //Create object to receive XMLs 
                        new Receiver(session , receiveDest , implementationMessageListener);
                        // Send Connection DTO to BuisnessLayer
                        String xml              = Transmitter.objectToXml(conDto);
                        logger.info("\n Message OUT \n" + xml);
                        transmitter.send(xml);
                        //Set language for DTOs reception
                        implementationMessageListener.setLang(conDto.getLanguage());
                    } catch (Exception e1) {
                    	logger.fatal("Problem with send ConnectionDTO" , e1);
                    }
                }
            });
            southPanel.add(connectButton);
            add(southPanel , BorderLayout.SOUTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    /**
     * <p>Method to show windows</p>
     */
    public void showWindows() {
        pack();
        this.setLocationRelativeTo(null);
        setVisible(true);
    }
    /**
     * <p>Return transmitter</p>
     * @return
     */
    public Transmitter getTransmitter() {
        return transmitter;
    }
    /**
     * <p>Return ImplementationMessageListener</p>
     * @return
     */
    public ImplementationMessageListener getImplementationMessageListener() {
        return implementationMessageListener;
    }



}
