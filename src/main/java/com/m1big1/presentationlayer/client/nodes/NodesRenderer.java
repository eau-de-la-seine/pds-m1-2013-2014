package com.m1big1.presentationlayer.client.nodes;


import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * <p>Class Define the renderer of node in JTree</p>
 * @author Oriel SAMAMA
 *
 */
public class NodesRenderer extends DefaultTreeCellRenderer {
    
    public Component getTreeCellRendererComponent(JTree tree , Object value , boolean selected , boolean expanded , boolean leaf , int row ,
            boolean hasFocus) {
    	
        JLabel label      = new JLabel();
        AbstractNode node = (AbstractNode) value;
        label.setIcon(node.getIcon());
        label.setText(node.getName());
        return label;
    }

   
}
