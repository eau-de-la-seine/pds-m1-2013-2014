package com.m1big1.presentationlayer.client.swing;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.jnlp.BasicService;
import javax.jnlp.ServiceManager;
import javax.jnlp.UnavailableServiceException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import org.apache.log4j.Logger;

import com.m1big1.common.Transmitter;
import com.m1big1.presentationlayer.client.connection.ImplementationMessageListener;
import com.m1big1.presentationlayer.client.dto.*;
import com.m1big1.presentationlayer.client.nodes.*;

public class WordManagerFrame extends JFrame implements WindowListener {
	
	private String sessionId , lang , viewHistory;

	private Transmitter						transmitter;
	private MyJTree 						myJTree;
	private ImplementationMessageListener	implementationMessageListener;
	private NodeStructureDTO				myNodeStructure;
	
	//Nodes Elements
	private Map<Integer , AbstractNode>		nodeMap;
	private NodeUser 						userRootNode;
	private NodeWord						nodeWord;
		
	//Net elments
	private BasicService 					bs;
	private 			 URL 				urlMail;
	private static final String 			urlValueBegin = "http://localhost/pds/mail.php?username=";
	private static 		 Logger 			logger 		  = Logger.getLogger(LoginFrame.class);
	
	//Graphicals elements
	private Container	containerFrame;
	private JPanel		mainPanel 			= new JPanel();
	private JPanel 		lookupPanel 		= new JPanel();
	private JPanel 		leftPanel 			= new JPanel();
	private JPanel 		welcomePanel 		= new JPanel();
	private JPanel 		wordPanel 			= new JPanel();
	private JPanel 		rightPanel;
	private JLabel 		wordName, welcomeLabel , titleLabel;

	private JScrollPane treePane;
	
	private CardLayout 	multiContent 		= new CardLayout();
	private ImageIcon 	searchIcon;
	private JButton 	saveButton;
	final 	JTextArea 	contentArea 		= new JTextArea(30 , 37);
	private JTextField 	fieldSearch  		= new JTextField(10);
	private JMenuBar 	principalBar 		= new JMenuBar();
	private JMenu 		info;
	private JMenuItem 	sendMail ;
	
	
	/**
	 * <p>Constructor of WordManagerFrame</p>
	 * @param mytransmitter
	 * @param myImplementationMessageListener
	 * @param mySessionId
	 * @param nodeStructureDTO
	 * @param mylang
	 * @throws IOException
	 */
	// Frame Constructor
	public WordManagerFrame(Transmitter mytransmitter ,
			ImplementationMessageListener myImplementationMessageListener ,
			String mySessionId , NodeStructureDTO nodeStructureDTO , String mylang)
			throws IOException
			{

				setTitle("WordManager - Application");
				setPreferredSize(new Dimension(750 , 600));
				addWindowListener(this);
		
				// initialise private attributs
				implementationMessageListener = myImplementationMessageListener;
				lang 						  = mylang;
				sessionId 					  = mySessionId;
				myNodeStructure 			  = nodeStructureDTO;
				transmitter 				  = mytransmitter;
				containerFrame 				  = this.getContentPane();
				implementationMessageListener.setWordManagerFrame(this);
				//Initialise URL for sending a mail
				String url = urlValueBegin + nodeStructureDTO.getName() +"&lang="+ mylang;
				URL mail = new URL(url);
				urlMail = mail; 
				AbstractNode.setMyTransmitter(mytransmitter);
				initImages();
				initcomponentForLanguage();
				initComponents();
	}
	/**
	 * <p>Initialise all graphical element of Frame</p>
	 * @throws IOException
	 */
	private void initComponents() throws IOException {
		setLayout(new BorderLayout());
    	mainPanel.setLayout(new BorderLayout());
    		//MenuBar
    		sendMail.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						bs = (BasicService) ServiceManager.lookup("javax.jnlp.BasicService");
						bs.showDocument(urlMail);
						logger.info("Send mail for problem");
					} catch (UnavailableServiceException e1) {
						logger.fatal("Problem to create basic service.", e1);
					}
					
					}
			});
    		info.add(sendMail);
    		principalBar.add(info);
	    	//Define leftPanel
	    	leftPanel.setLayout(new BorderLayout());
			lookupPanel.setLayout(new BorderLayout());
				//Define elements of left panel which have lookup panel and JTreePanel
				JButton searchButton = new JButton(searchIcon);
				searchButton.setPreferredSize(new Dimension(20 , 20));
				searchButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						SearchWordDTO searchWordDTO = new SearchWordDTO();
						searchWordDTO.setSessionId(sessionId);
						searchWordDTO.setSearchName(fieldSearch.getText());
	                        try {
	                            String xml = Transmitter.objectToXml(searchWordDTO);
	                            logger.info("Message OUT \n" + xml);
	                            transmitter.send(xml);
	                            } catch (Exception e1) {
	                            	logger.fatal("Problem to send ReadWordDTO" , e1);
	                            } 
						
						
					}
				});
				lookupPanel.add(fieldSearch , BorderLayout.CENTER);
				lookupPanel.add(searchButton , BorderLayout.EAST);
			leftPanel.add(lookupPanel , BorderLayout.NORTH);
					//Create JTree
		    		myJTree = createJTree(myNodeStructure);
		    		//Add JTree to the Pane
		    		treePane = new JScrollPane(myJTree);
		    		treePane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		    		treePane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		    leftPanel.add(treePane , BorderLayout.CENTER);
		    		//Define button log history
		    		JButton viewHistoryButton = new JButton (viewHistory);
		    		viewHistoryButton.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							 HistoryDTO historyDTO = new HistoryDTO();
							 historyDTO.setSessionId(sessionId);
		                        try {
		                            String xml = Transmitter.objectToXml(historyDTO);
		                            logger.info("Message OUT \n" + xml);
		                            transmitter.send(xml);
		                            } catch (Exception e1) {
		                            	logger.fatal("Problem to send ReadWordDTO" , e1);
		                            } 
							
						}
					});
		    leftPanel.add(viewHistoryButton , BorderLayout.SOUTH); 
		    //End of left Panel
	   mainPanel.add(leftPanel , BorderLayout.WEST);
		   	//Define right JPanel
		    rightPanel = new JPanel(multiContent);
		    	//Define WelcomePanel and show when wordmanagerframe start
			    	welcomePanel.setLayout(new FlowLayout());
			    	welcomePanel.add(welcomeLabel);
			rightPanel.add(welcomePanel , "welcomepanel");
					//Show this Panel when WordManagerFrame is started first time
			    	makeWelcomePanel();
			    	
		    	//Define ContentWord Panel
			    	wordPanel.setPreferredSize(new Dimension(350 , 600));
		            wordPanel.setLayout(new BorderLayout());
		            //Header Panel
		                JPanel titlePanel = new JPanel();
		                titlePanel.setLayout(new BorderLayout());
		                titlePanel.add(wordName , BorderLayout.WEST);
		                titleLabel = new JLabel();
		                titlePanel.add(titleLabel , BorderLayout.CENTER);
		            wordPanel.add(titlePanel , BorderLayout.NORTH);
		            //Body Panel
		                JPanel contentPanel 	= new JPanel();
		                JScrollPane jScrollPane = new JScrollPane(contentArea);
		                jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		                jScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		                contentPanel.add(jScrollPane);
		            wordPanel.add(contentPanel , BorderLayout.CENTER);
		            //Footer
			            JPanel buttonPanel = new JPanel();
			            buttonPanel.setLayout(new FlowLayout());
			            if (saveButton.getActionListeners().length == 0) {
			                saveButton.addActionListener(new ActionListener() {
			                    @Override
			                    public void actionPerformed(ActionEvent e) {
			                        if(nodeWord != null) {
			                            UpdateWordDTO uwd = new UpdateWordDTO();
			                            uwd.setSessionId(sessionId);
			                            uwd.setNodeId(nodeWord.getId());
			                            String content = contentArea.getText().replace("\r", "\n");
			                            uwd.setContent(content);
			                            try {
			                                String xml = Transmitter.objectToXml(uwd);
			                                logger.info("Message OUT \n" + xml);
			                                transmitter.send(xml);
			                                } catch (Exception e1) {
			                                	logger.fatal("Problem to send UpdateWordDTO" , e1);
			                            }  
			                        }
			                      }
			                    });
			            }
			            buttonPanel.add(saveButton);
		            wordPanel.add(buttonPanel , BorderLayout.SOUTH);
		     rightPanel.add(wordPanel  , "mainContentPanel");
	          //Define search Panel
	    mainPanel.add(rightPanel , BorderLayout.CENTER);
	    mainPanel.add(principalBar , BorderLayout.NORTH);
	  containerFrame.add(mainPanel , BorderLayout.CENTER);
	 }		

	public void showWindows() {
		 pack();
	     setResizable(false);
	     setLocationRelativeTo(null);
	     setVisible(true);
		
	}
	/**
	 * <p>Void with which we see the welcome panel
	 */
	public void makeWelcomePanel(){
		
		multiContent.show(rightPanel , "welcomepanel");
		
	}
	/**
	 * <p>Fonction for create JTree</p>
	 * @param nodeStructure
	 * @return
	 * @throws IOException
	 */
    // Definition of JTree
    private MyJTree createJTree(NodeStructureDTO nodeStructure) throws IOException {
        AbstractNode.initImages();
        nodeMap = new HashMap<Integer , AbstractNode>();
        
        //Initiliaze the User Node
        	userRootNode = new NodeUser(myNodeStructure.getId() , myNodeStructure.getParentId() , myNodeStructure.getName() , sessionId , lang);
        	nodeMap.put(userRootNode.getId() , userRootNode);
        	
        //Initiliaze object with first next node in object 
        	NodeStructureDTO currentNodeDTO = myNodeStructure.getNextNode();
	        while (currentNodeDTO != null) {
	            //initialise a abstractnode with nextode
		            AbstractNode currentNode = (currentNodeDTO.getType() == 1) ?
	                    new NodeFolder(currentNodeDTO.getId() , currentNodeDTO.getParentId() , currentNodeDTO.getName() , sessionId , lang):
	                    new NodeWord  (currentNodeDTO.getId() , currentNodeDTO.getParentId() , currentNodeDTO.getName() , currentNodeDTO.getContent() , sessionId , lang);
	            
	            //Add node in Map
                    nodeMap.put(currentNodeDTO.getId() , currentNode);
	            //Add node in parent node
                    nodeMap.get(currentNodeDTO.getParentId()).add(currentNode);
	            //Initialise object currentDTO with next node
                    currentNodeDTO = currentNodeDTO.getNextNode();
	        }
        
        //Create JTree with options and listeners
	        myJTree = new MyJTree(userRootNode , this , nodeMap , sessionId);
        //Apply renderer to node
	        myJTree.setCellRenderer(new NodesRenderer());
        return myJTree;
    }
    
    /**
     * <p>Fonction which apply Word Panel to see content of Word</p>
     * @param myid
     * @param myContent
     */
    public void setNodeWord(int myid , String myContent) {
        nodeWord = (NodeWord) nodeMap.get(myid);
        nodeWord.setContent(myContent);
   	 	titleLabel.setText(nodeWord.getName());
   	 	contentArea.setText(nodeWord.getContent());
   	 	multiContent.show(rightPanel , "mainContentPanel");
       
    }
    /**
     * <p>Init Images</p>
     */
	private void initImages(){
		 try {
	            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	            
	            Properties propertiesImage 		= new Properties();
	            InputStream propertiesImageFile = classLoader.getResourceAsStream("images/imageLocation.properties");
	            propertiesImage.load(propertiesImageFile);
	            String searchIconLocation 		= propertiesImage.getProperty("SEARCH_ICON_LOCATION");
	            searchIcon 						= new ImageIcon(NodeWord.class.getResource(searchIconLocation));
	            propertiesImageFile.close();
	        } catch (IOException e) {
	        	logger.fatal("Problem to load languages" , e);
	        }
		
	}
	/**
	 * <p>Init languages component</p>
	 */
	 private final void initcomponentForLanguage(){
	       
	        try {
	            Properties properties = new Properties();
	            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	            InputStream propertiesFile = classLoader.getResourceAsStream("languages/language_"+ lang +".properties");
	            properties.load(propertiesFile);
	            // First Label of right panel
	            saveButton   = new JButton(properties.getProperty("saveButton"));
	            wordName     = new JLabel(properties.getProperty("fileName") + ": ");
	            welcomeLabel = new JLabel(properties.getProperty("first_label_of_right_panel"));
	            viewHistory  = properties.getProperty("view_history");
	            info         = new JMenu (properties.getProperty ("info"));
	            sendMail     = new JMenuItem (properties.getProperty ("signalProblem"));
	            propertiesFile.close();
	        } catch (IOException e) {
	        	logger.fatal("Problem to load languages" , e);
	        }

	    }
	 public String getLang() {
			return lang;
		}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) {
		LogoutDTO dec = new LogoutDTO();
		dec.setSessionId(sessionId);
		try {
			this.dispose();
			String xml = Transmitter.objectToXml(dec);
			logger.info("Message OUT \n" + xml);
			transmitter.send(xml);
			logger.info("User disconnect");
			System.exit(0);
		} catch (Exception e1) {
			logger.fatal("Problem to disconnect", e1);
		}

	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}
	

    public MyJTree getMyJTree() {
        return myJTree;
    }
    public void setMyJTree(MyJTree myJTree) {
        this.myJTree = myJTree;
    }

	public Transmitter getTransmitter() {
		// TODO Auto-generated method stub
		return transmitter;
	}

}





