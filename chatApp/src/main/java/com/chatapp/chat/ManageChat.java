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
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultListModel;


public class ManageChat implements Runnable{
	public int tcpPort=5555;
	private Vector<Message> historique;
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
	}
	public void setHistorique(Vector<Message> historique) {
		this.historique = historique;
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
	public void sendMessage(Message m) {
		try {
			Socket s = new Socket(m.getReceiver().getIp(),tcpPort);
			ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
			historique.add(m);
			try {
				DbServices.putMessage(m);
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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