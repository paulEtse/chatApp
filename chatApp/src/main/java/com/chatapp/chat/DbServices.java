package com.chatapp.chat;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Vector;

public class DbServices {
	 public static String  url =Global.Dburl;
	    public static String login =Global.Dblogin;
	    public static String password =  Global.Dbpassword;
	    public static void connect() throws ClassNotFoundException, SQLException{
	        Class.forName("com.mysql.cj.jdbc.Driver") ;
	        Connection con = DriverManager.getConnection(url, login, password) ;
	        
	}
	    public static Vector<Message> getMessages(User u) throws ClassNotFoundException, SQLException, UnknownHostException, ParseException{
	             Class.forName("com.mysql.cj.jdbc.Driver") ;
	        Connection con = DriverManager.getConnection(url, login, password) ;
	            Statement statment = con.createStatement();
	            String query = "select * from message where sender = '"+u.getIp().getHostAddress()+"' or receiver = '"+u.getIp().getHostAddress()+"'";
	            ResultSet rs = statment.executeQuery(query);
	            Vector<Message> messages= new Vector<Message>();
	            while(rs.next())
	            {
	            	Message m = new Message();
	            	User sender=new User();
	            	sender.setIp(InetAddress.getByName(rs.getString("sender")));
	            	sender=getUser(sender);
	            	User receiver=new User();
	            	receiver.setIp(InetAddress.getByName(rs.getString("receiver")));
	            	receiver=getUser(receiver);
	            	LocalDateTime date=DateConverter.convert(rs.getString("date"));
	            	String text=rs.getString("text");
	            	m.setDate(date);
	            	m.setReceiver(receiver);
	            	m.setSender(sender);
	            	m.setText(text);
	            	messages.add(m);
	            }
	            return messages;
	    }
	public static void putMessage(Message m) throws ClassNotFoundException, SQLException
	{
	    Class.forName("com.mysql.cj.jdbc.Driver") ;
	    Connection con = DriverManager.getConnection(url, login, password) ;
	        String sender=m.getSender().getIp().getHostAddress();
	        String receiver = m.getReceiver().getIp().getHostAddress();
	        String text = m.getText();
	        String query= "insert into message values(null,'"+sender+"','"+receiver+"','"+text+"',NOW())";
	        Statement statement = con.createStatement();
	        int res = statement.executeUpdate(query);
	        System.out.println(res);
	}
	public static boolean findUser(User u) throws ClassNotFoundException, SQLException
	{
	    Class.forName("com.mysql.cj.jdbc.Driver") ;
	    Connection con = DriverManager.getConnection(url, login, password) ;
	        Statement statment = con.createStatement();
	        String query = "select count(*) as rs from user where ip = '"+u.getIp().getHostAddress()+"'";
	        ResultSet rs = statment.executeQuery(query);
	        int rows=0;
	        if(rs.next())
	        	rows=Integer.parseInt(rs.getString(1));
	        return rows==1;
	}
	public static User getUser(User u) throws ClassNotFoundException, SQLException
	{
	    Class.forName("com.mysql.cj.jdbc.Driver") ;
	    Connection con = DriverManager.getConnection(url, login, password) ;
	        Statement statment = con.createStatement();
	        String query = "select * from user where ip = '"+u.getIp().getHostAddress()+"'";
	        ResultSet rs = statment.executeQuery(query);
	        if(rs.next())
	        {
	        	u.setPseudo(rs.getString("pseudo"));
	        }
	    	return u;
	}
	public static void putUser(User u) throws SQLException, ClassNotFoundException
	{
	            Class.forName("com.mysql.cj.jdbc.Driver") ;
	        Connection con = DriverManager.getConnection(url, login, password) ;
	          String query1= "insert into user values ('"+u.getIp().getHostAddress()+"','"+u.getPseudo()+"')";
	        String query2= "update user set pseudo= '"+u.getPseudo()+"' where ip = '"+u.getIp().getHostAddress()+"'";
	        Statement statement = con.createStatement();
	        if(findUser(u))
	        {
	            statement.executeUpdate(query2);
	        }
	        else
	        { statement.executeUpdate(query1);
	        }
	}
}
