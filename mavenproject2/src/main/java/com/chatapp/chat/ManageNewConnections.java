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
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

import javax.swing.DefaultListModel;

import org.apache.commons.lang3.SerializationUtils;
/**
 *
 * @author etse
 */
public class ManageNewConnections implements Runnable{
    public InetAddress broadcast;
    public static int broadcastPort=55555;
    DatagramSocket socket;
    private DefaultListModel<User> connectedUsers=new DefaultListModel<User>();
    private  User owner;

    public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public ManageNewConnections(User u)
    {
		owner=u;
        try {
        	try(final DatagramSocket socket = new DatagramSocket()){
      		  socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
      		  owner.setIp(socket.getLocalAddress());
      		  broadcast=getBroadcast();
      		  socket.close();
      		}
        }catch(Exception e) {
        	e.printStackTrace();
        }
        new Thread(this).start();
    }
    
    private InetAddress getBroadcast(InetAddress myIpAddress) {

        NetworkInterface temp;
        InetAddress iAddr = null;
        try {
            temp = NetworkInterface.getByInetAddress(myIpAddress);
            List<InterfaceAddress> addresses = temp.getInterfaceAddresses();

            for (InterfaceAddress inetAddress : addresses) {
                iAddr = inetAddress.getBroadcast();
            }
            return iAddr;

        } catch (SocketException e) {

            e.printStackTrace();
            System.out.println("getBroadcast" + e.getMessage());
        }
        return null;
    }
    private InetAddress getBroadcast() {
    	InetAddress broadcast=null;
    	Enumeration<NetworkInterface> interfaces;
		try {
			interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) 
	    	{
	    	    NetworkInterface networkInterface = interfaces.nextElement();
	    	    if (networkInterface.isLoopback())
	    	        continue;    // Do not want to use the loopback interface.
	    	    for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) 
	    	    {
	    	        broadcast = interfaceAddress.getBroadcast();
	    	        if (broadcast == null)
	    	            continue;
	    	        return broadcast;
	    	       }
	    	}	
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return broadcast;
    }
    	
    public void manageNewMessage() {
    	byte[] buffer=new byte[1024];
		try {
			socket.setBroadcast(true);
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
	    byte[] buffer = SerializationUtils.serialize(m);
	    DatagramPacket paquet=new DatagramPacket(buffer,buffer.length,broadcast,broadcastPort);
		try {
			socket.send(paquet);
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
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public void run() {
    	connect();
    	manageNewMessage();
    }    
}
