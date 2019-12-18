/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chatapp.chat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 *
 * @author etse
 */
public class Message implements Serializable, Comparable<Message> {
    private User sender;
    private User receiver;
    private String texte;
    private byte[] data;
    private LocalDateTime date;
    private MessageType type;
    public MessageType getType() {
		return type;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	@Override
	public String toString() {
		String d = date.getHour()+":"+date.getMinute();
		String s = "From "+sender+ " To "+receiver+", "+d+"\n"+getText()+"\n\n";
		return "<html>" +s.replaceAll("<","&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>") + "</html>";
	}
	public void setType(MessageType type) {
		this.type = type;
	}
	public Message(){}
    public void setSender(User sender){
    this.sender=sender;
    }
    public User getSender(){
    return this.sender;}
     public void setReceiver(User receiver){
    this.receiver =receiver;
    }
    public User getReceiver(){
    return this.receiver;}
    public String getText()
    { return texte;}
    public void setText(String texte){
    this.texte=texte;
    }
	public String getTexte() {
		return texte;
	}
	public void setTexte(String texte) {
		this.texte = texte;
	}
	public LocalDateTime getDate() {
		return date;
	}
	public void setDate(LocalDateTime date) {
		this.date = date;
	}

    @Override
  public int compareTo(Message o2) {
        return getDate().compareTo(o2.getDate());
    }
}

