/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chatapp.chat;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author etse
 */
public class NetService {
    public static InetAddress getBroadcast() {
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
    public static InetAddress getIp(){
        
       try {
        	try(final DatagramSocket socket = new DatagramSocket()){
      		  socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
      		  return socket.getLocalAddress();
      		}
        }catch(Exception e) {
        	e.printStackTrace();
        }
        return null;
    }
}
