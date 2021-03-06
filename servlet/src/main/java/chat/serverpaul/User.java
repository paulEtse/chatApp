package chat.serverpaul;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.Serializable;
import java.net.*;
/**
 *
 * @author etse
 */
public class User implements Serializable {
    private String pseudo;
    private InetAddress ip;
    public InetAddress getIp() {
		return ip;
	}    
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (ip == null) {
			if (other.ip != null)
				return false;
		} else if (!ip.getHostAddress().equals(other.ip.getHostAddress()))
			return false;
		return true;
	}
	public void setPseudo(String pseudo){
    this.pseudo=pseudo;
    }
    public String getPseudo(){
    return pseudo;
    }
	public void setIp(InetAddress ip) {
		this.ip=ip;
	}
	@Override
	public String toString() {
		return pseudo ;
	}    
}
