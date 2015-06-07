package com.m1big1.accesslayer.server.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import com.m1big1.accesslayer.server.dao.NodeDAO;
import com.m1big1.accesslayer.server.model.Node;

public class DatabaseInterface extends JFrame implements WindowListener {
    private final CardLayout    cardLayout          = new CardLayout();
    
    private final JPanel        treePanel           = new JPanel();
    private final JPanel        nodePanel           = new JPanel();

    private final JPanel        nodeTitlePanel      = new JPanel();
    private final JPanel        welcomePanel        = new JPanel();
    private final JPanel        nodeContentPanel    = new JPanel(cardLayout);
    
    private final JTextField    titleField          = new JTextField(30);
    private final JTextArea     contentTextArea     = new JTextArea(35,35);
    
    private       AccessJTree   accessTree          = null;
    
    private List<Node> nodeList;
    private Map<Integer, Node> nodeMap;
    private final NodeDAO nodeDAO;
    
    // Frame Constructor
    public DatabaseInterface(NodeDAO nodeDAO, List<Node> nodeList) {
        setTitle("Access Layer Interface");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        this.nodeList = nodeList; 
        this.nodeDAO = nodeDAO;
        initComponent();
    }
    
    private void initComponent() {
        createJTree();
        setLayout(new BorderLayout());
        
        JScrollPane jScrollTree = new JScrollPane(accessTree);
        jScrollTree.setPreferredSize(new Dimension(150,600));
        treePanel.add(jScrollTree);
        
        welcomePanel.add(new JLabel("Welcome"));
        nodeTitlePanel.add(titleField);
        
        nodeContentPanel.add(welcomePanel, "welcome");
        nodeContentPanel.add(contentTextArea, "content");
        
        cardLayout.show(nodeContentPanel, "welcome");
        
        nodePanel.setLayout(new BorderLayout());
        nodePanel.add(nodeTitlePanel, BorderLayout.NORTH);
        nodePanel.add(nodeContentPanel, BorderLayout.CENTER);

        add(treePanel, BorderLayout.WEST);
        add(nodePanel, BorderLayout.CENTER);
    }

    private void createJTree() {
        nodeMap = new HashMap<Integer, Node>();
        Node rootNode = nodeList.get(0);
        nodeMap.put(rootNode.getNodeID(), rootNode);
        
        for(int i = 1; i< nodeList.size(); i++) {
            Node actualNode = nodeList.get(i);

            nodeMap.put(actualNode.getNodeID(), actualNode);
            nodeMap.get(actualNode.getNodeParentID()).add(actualNode);
        }
        accessTree = new AccessJTree(this, nodeDAO, nodeMap, rootNode);
    }

    public void showWindows() {
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    public void setNodeContent(String title, String content) {
        titleField.setText(title);
        contentTextArea.setText(content);
        cardLayout.show(nodeContentPanel, "content");
    }

    public String getWordNewContent()
    {
        System.out.println(contentTextArea.getText());
        return contentTextArea.getText();
    }
    
    @Override
    public void windowClosed(WindowEvent e) { }
    
    @Override
    public void windowActivated(WindowEvent e) { }
    
    @Override
    public void windowClosing(WindowEvent e) {
        System.exit(0);
    }

    @Override
    public void windowOpened(WindowEvent e) { }

    @Override
    public void windowIconified(WindowEvent e) { }

    @Override
    public void windowDeiconified(WindowEvent e) { }

    @Override
    public void windowDeactivated(WindowEvent e) { }
}
