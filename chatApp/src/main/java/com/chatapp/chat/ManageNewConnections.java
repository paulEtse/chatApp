/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chatapp.chat;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultListModel;

import org.apache.commons.lang3.SerializationUtils;
/**
 *
 * @author etse
 */
public class ManageNewConnections implements Runnable{
    public InetAddress broadcast;
    public static final int broadcastPort=Global.BroadcastPort;
    public static final  int NotifyPort=Global.NotifyPort;
    DatagramSocket socket;
    private DefaultListModel<User> connectedUsers=new DefaultListModel<User>();
    public boolean subcription=false;
    private  User owner;

    public ManageNewConnections(User u)
    {
	owner=u;
        owner.setIp(NetService.getIp());
        broadcast=NetService.getBroadcast();
        connect();
        if(subcription)
            subscribe();
        new Thread(this).start();
    }
    
      public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}
    
    public void manageLocalMessage(){
        	byte[] buffer=new byte[1024];
		try {
	    	while(true) {
	    		DatagramPacket paquet =new DatagramPacket(buffer,buffer.length);
					socket.receive(paquet);
					Message m = SerializationUtils.deserialize(paquet.getData());
					new Thread(){
						public void run() {
							manage(m);
						}
					}.start();	
	    	}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    	
    public void ManageNotifications() {
        	byte[] buffer=new byte[1024];
		try {
                    DatagramSocket s = new DatagramSocket(broadcastPort);
	    	while(true) {
	    		DatagramPacket paquet =new DatagramPacket(buffer,buffer.length);
					s.receive(paquet);
					Message m = SerializationUtils.deserialize(paquet.getData());
					new Thread(){
						public void run() {
							manage(m);
						}
					}.start();	
	    	}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public void manage(Message m )
    {
  		if(m.getSender().equals(owner)==false)
		{
  			Message rep= new Message();
  	    	rep.setReceiver(m.getSender());
  	    	rep.setSender(owner);
  	    	switch(m.getType()) {
  	    	case Connect : 
  	    		rep.setType(MessageType.ConnectRep);
  	    		byte[] buffer = SerializationUtils.serialize(rep);
  	    		DatagramPacket paquet=new DatagramPacket(buffer,buffer.length,rep.getReceiver().getIp(),broadcastPort);
  				try {
  					socket.send(paquet);
  				} catch (IOException e) {
  					// TODO Auto-generated catch block
  					e.printStackTrace();
  				}
  	    		break;
  	    	case ConnectRep :
                        if(connectedUsers.contains(m.getSender()))
                            connectedUsers.removeElement(m.getSender());
  	    		connectedUsers.add(0, m.getSender());
                        System.out.println("Connected "+getConnectedUsers());
  	    		break;
  	    	case DisConnect :
  	    		connectedUsers.removeElement(m.getSender());
  	    		break;
  	    	default:
  	    	}
		}
    }
    public DefaultListModel<User> getConnectedUsers() {
		return connectedUsers;
	}

	public void setConnectedUsers(DefaultListModel<User> connectedUsers) {
		this.connectedUsers = connectedUsers;
	}
public void chooseAndSendPseudo(String pseudo) {
		Message m= new Message();
		owner.setPseudo(pseudo);
		m.setSender(owner);
		m.setType(MessageType.ConnectRep);
		try {
			DbServices.putUser(owner);
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    byte[] buffer = SerializationUtils.serialize(m);
	    DatagramPacket paquet=new DatagramPacket(buffer,buffer.length,broadcast,broadcastPort);
		try {
			socket.send(paquet);
                        if(subcription)
                        ServerServices.sendPostRequest(owner);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void connect()
    {
    	try {
    		Message m= new Message();
    		m.setType(MessageType.Connect);
    		m.setSender(owner);
    		byte[] buffer = SerializationUtils.serialize(m);
			socket = new DatagramSocket(broadcastPort);
			DatagramPacket paquet=new DatagramPacket(buffer,buffer.length,broadcast,broadcastPort);
			socket.send(paquet);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public void disconnect() {
    	try {
    		Message m= new Message();
    		m.setType(MessageType.DisConnect);
    		m.setSender(owner);
    		byte[] buffer = SerializationUtils.serialize(m);
			DatagramPacket paquet=new DatagramPacket(buffer,buffer.length,broadcast,broadcastPort);
			socket.send(paquet);
                        if(subcription)
                        ServerServices.sendDeleteRequest(owner);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public void subscribe(){
        /*try {
            connectedUsers=ServerReq.sendGetRequest();
        } catch (IOException ex) {
            Logger.getLogger(ManageNewConnections.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ManageNewConnections.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }
    public void run() {
            manageLocalMessage();
        if(subcription)
            ManageNotifications();
    }    
}
