package com.m1big1.accesslayer.server;

import java.sql.SQLException;
import java.util.List;

import com.m1big1.accesslayer.server.dao.DAOFactory;
import com.m1big1.accesslayer.server.model.Node;

public class Test {
    public static void main(String[] args) {
        try {
            List<Node> list = DAOFactory.getNodeDAO().find(2, "Test");
            for(Node node : list) {
                System.out.println(DAOFactory.getNodeDAO().getFullPath(node.getNodeID()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
}
