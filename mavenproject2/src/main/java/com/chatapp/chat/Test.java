package com.chatapp.chat;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class Test {
public static void main(String[] args) throws SocketException {
	Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
	while (interfaces.hasMoreElements()) 
	{
	    NetworkInterface networkInterface = interfaces.nextElement();
	    if (networkInterface.isLoopback())
	        continue;    // Do not want to use the loopback interface.
	    for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) 
	    {
	        InetAddress broadcast = interfaceAddress.getBroadcast();
	        if (broadcast == null)
	            continue;
	        System.out.println("Broadcast : "+broadcast.getHostName());
	        // Do something with the address.
	    }
	}
}
}
