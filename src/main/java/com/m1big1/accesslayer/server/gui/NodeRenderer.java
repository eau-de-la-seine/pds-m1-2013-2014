package com.m1big1.accesslayer.server.gui;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.m1big1.accesslayer.server.model.Node;

public class NodeRenderer extends DefaultTreeCellRenderer {
    private final String WORD_ICON_LOCATION = "/com/m1big1/accesslayer/gui/images/word_icon.png";
    private final String FOLDER_ICON_LOCATION = "/com/m1big1/accesslayer/gui/images/folder_icon.png";
    
    private final ImageIcon wordIcon = new ImageIcon(NodeRenderer.class.getResource(WORD_ICON_LOCATION));
    private final ImageIcon folderIcon = new ImageIcon(NodeRenderer.class.getResource(FOLDER_ICON_LOCATION));
     
    public Component getTreeCellRendererComponent(JTree tree, Object value,  boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        JLabel label = new JLabel();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        label.setText(((Node) node).getNodeName());
        switch(((Node) node).getNodeTypeID()) {
            case 1:
                label.setIcon(folderIcon);  break;
            case 2:
                label.setIcon(wordIcon);    break;
            default:                        break;
        }

        return label;
    }
}
