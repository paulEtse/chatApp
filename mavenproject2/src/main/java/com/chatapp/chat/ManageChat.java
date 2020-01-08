package com.chatapp.chat;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultListModel;


public class ManageChat implements Runnable{
	public final int tcpPort=5555;
	private Vector<Message> historique;
	private final String fichier="stockage";
    private DefaultListModel<Message> currentMessages;
    private User currentReceiver;
	public User getCurrentReceiver() {
		return currentReceiver;
	}
	public void setCurrentReceiver(User currentReceiver) {
		this.currentReceiver = currentReceiver;
	}
	public Vector<Message> getHistorique() {
		return historique;
	}
	public ManageChat()
	{
		currentMessages= new DefaultListModel<Message>();
		historique=new Vector<Message>();
		new Thread(this).start();
		FileInputStream fi;
		try {
			File f= new File(fichier);
			fi = new FileInputStream(f);
			if(f.length()!= 0) {
				ObjectInputStream oi = new ObjectInputStream(fi);
				if(f.length()>0)
				{
					historique=(Vector<Message>) oi.readObject();
				}
				oi.close();
				fi.close();
				System.out.println("Historique " + historique);
			}
			
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public DefaultListModel<Message> getCurrentMessages() {
		return currentMessages;
	}	public static void main(String[] args) {
		System.out.println("Hello");
	}
	public void setCurrentMessages(DefaultListModel<Message> currentMessages) {
		this.currentMessages = currentMessages;
	}
	public void listen() {
		ServerSocket server;
		try {
			server = new ServerSocket(tcpPort);
			Socket socket;
			while(true)
			{
				socket = server.accept();
				ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
				Message m=(Message) in.readObject();
                m.setDate(LocalDateTime.now());
				new Thread(){
					public void run() {
						historique.add(m);
						System.out.println("received "+m);
						if(m.getType()==MessageType.File) {
                                                    try {
                                                        Files.write(new File((m.getText())).toPath(), m.getData());
                                                    } catch (IOException ex) {
                                                        Logger.getLogger(ManageChat.class.getName()).log(Level.SEVERE, null, ex);
                                                    }
                                                }
						if(currentReceiver!=null&&m.getSender().equals(currentReceiver)) {
							System.out.println("RecAj");
							currentMessages.addElement(m);
						}
					}
				}.start();	
			}
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void save()
	{
		System.out.println("Historique " + historique);
		try {
			FileOutputStream f = new FileOutputStream(new File(fichier));
			ObjectOutputStream o = new ObjectOutputStream(f);
			o.writeObject(historique);
			o.close();
			f.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void sendMessage(Message m) {
		try {
			Socket s = new Socket(m.getReceiver().getIp(),tcpPort);
			ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
			historique.add(m);
			out.writeObject(m);
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		listen();
	}
	public static byte[] File2Byte(String file_address) {
		try {
			return Files.readAllBytes(Paths.get(file_address));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}