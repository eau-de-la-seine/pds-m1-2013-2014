package com.m1big1.businesslayer.server.servlet;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;


//import java.lang.reflect.Method;
//import com.m1big1.businesslayer.action.Responses;
//import java.net.URLDecoder;
import javax.jms.Session;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.m1big1.businesslayer.client.ClientFactory;
import com.m1big1.businesslayer.server.action.Action;
import com.m1big1.businesslayer.server.dto.access.DTO;
import com.m1big1.common.Receiver;
import com.m1big1.common.Transmitter;


/**
 *<p>This servlet treats responses from the Access Layer</p>
 * @author Gokan EKINCI
 */
@WebServlet("/a2b")
public class A2BServlet extends AbstractServlet {
    private static Logger logger = Logger.getLogger(A2BServlet.class);
    // private Responses responses;    
    
    /**
     * <p>Initialize A2BServlet</p>
     * @param config
     * @see http://docs.oracle.com/javaee/6/api/javax/servlet/GenericServlet.html#init%28javax.servlet.ServletConfig%29
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            dtoPackageName = (String) envContext.lookup("dto_package_access");
            actionPackageName = (String) envContext.lookup("action_package_access");
        } catch (NamingException e) {
            logger.fatal("An error happened when using lookup in A2BServlet", e);
        }
        // responses = new Responses();
    }
    
    
    /**
     * <p>Treats received HTTP request from A2B Consumer</p>
     * @param request
     * @param response
     */
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response){
        String xml = request.getParameter("xmlmessage"); // Implicit url decode

        if(xml != null) {
            try {

                DTO receivedDto = Receiver.xmlToObject(dtoPackageName, xml);
                Constructor<?> constructor = Class.forName(actionPackageName + "." + receivedDto.getClass().getSimpleName() + "Action")
                                                  .getConstructor(ClientFactory.class, Transmitter.class, Session.class, receivedDto.getClass());
                Action action = (Action) constructor.newInstance(CLIENT_FACTORY, transmitterToAccess, session, receivedDto);
                action.execute();
            } catch (UnsupportedEncodingException uee) {
                logger.warn("Problem when decoding xmlmessage", uee);
            } catch (Exception e) {
                logger.warn("An error happened during XML to object transformation, wrong DTO structure could have been sent from Access Layer !", e);
            }
        }      
    }
}
