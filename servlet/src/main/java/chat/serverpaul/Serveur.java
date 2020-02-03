package chat.serverpaul;


import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.DefaultListModel;

import com.google.gson.Gson;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import org.apache.commons.lang3.SerializationUtils;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author etse
 */
public class Serveur extends HttpServlet{
    private final int serverPort = 44444;
    private final int NotificationPort = 44444;
    private DefaultListModel<User> connectedUserList=new DefaultListModel<User>();
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
              throws ServletException,
                     IOException
    {        
   String data = new Gson().toJson(connectedUserList);
   PrintWriter out = resp.getWriter();
   resp.setContentType("application/json");
   resp.setCharacterEncoding("UTF-8");
   out.print(data);
   out.flush();
    }
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
                 throws ServletException,
                        IOException{
          BufferedReader reader = req.getReader();
        Gson gson = new Gson();
        User u = gson.fromJson(reader, User.class);
        connectedUserList.removeElement(u);
        Message m = new Message();
        m.setType(MessageType.DisConnect);
        for(int i=0;i<connectedUserList.size();i++)
        {
            m.setSender(u);
            m.setReceiver(connectedUserList.elementAt(i));
            notify(m);
        }
    }
    private void notify(Message m){
        try{
            byte[] buffer = SerializationUtils.serialize(m);
			DatagramSocket socket = new DatagramSocket(serverPort);
			DatagramPacket paquet=new DatagramPacket(buffer,buffer.length,m.getReceiver().getIp(),NotificationPort);
			socket.send(paquet);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    protected void doPost(HttpServletRequest req,
                      HttpServletResponse resp)
               throws ServletException,
                      IOException{
        BufferedReader reader = req.getReader();
        Gson gson =  new Gson();
        User u = gson.fromJson(reader, User.class);
        if(connectedUserList.contains(u))
            connectedUserList.removeElement(u);
        connectedUserList.addElement(u);
        Message m = new Message();
        m.setType(MessageType.ConnectRep);
        for(int i=0;i<connectedUserList.size();i++)
        {
            m.setSender(u);
            m.setReceiver(connectedUserList.elementAt(i));
            notify(m);
        }
    }
    protected void doPut(HttpServletRequest req,
                     HttpServletResponse resp)
              throws ServletException,
                     IOException{
        BufferedReader reader = req.getReader();
        Gson gson = new Gson();
        User u = gson.fromJson(reader, User.class);
        connectedUserList.removeElement(u);
        connectedUserList.addElement(u);
    }
}
    
