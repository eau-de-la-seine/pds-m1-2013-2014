package com.m1big1.accesslayer.server.gui;

import com.m1big1.accesslayer.server.dao.DAOFactory;
import com.m1big1.accesslayer.server.dao.NodeDAO;

public class ServerInterfaceLauncher {
    public static void main(String[] args) {
        try {
            System.gc();
            NodeDAO nodeDAO = DAOFactory.getNodeDAO();
            DatabaseInterface gui = new DatabaseInterface(nodeDAO , nodeDAO.findAllByUserID(3));
            gui.showWindows();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
