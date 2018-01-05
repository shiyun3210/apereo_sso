package org.jasig.cas.zk.entity;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jasig.cas.authentication.Credential;

public class ZkUserGredential implements Credential,Serializable {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotNull
	@Size(min=1, message = "required.username")
	private String username;
	@NotNull
	@Size(min=1, message = "required.password")
	private String password;
	private String userinfo;
	
	public ZkUserGredential(){
		
	}
	
	public ZkUserGredential(String username,String password,String userinfo){
		this.username = username;
		this.password = password;
		this.userinfo = userinfo;
		
	}
	
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserinfo() {
		return userinfo;
	}

	public void setUserinfo(String userinfo) {
		this.userinfo = userinfo;
	}


	@Override
	public String getId() {
		return username;
	}
	
	@Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final ZkUserGredential that = (ZkUserGredential) o;

        if (password != null ? !password.equals(that.password) : that.password != null) {
            return false;
        }

        if (username != null ? !username.equals(that.username) : that.username != null) {
            return false;
        }
        
        if (userinfo !=null ? !userinfo.equals(that.userinfo) : that.userinfo !=null){
        	return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(username)
                .append(password)
                .append(userinfo)
                .toHashCode();
    }
}
