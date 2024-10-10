package com.example.smartchat;

public class UserInfos {

    String mail , password , profileP , status, userid ,lastmasseg , username;
    UserInfos(){}

    public UserInfos(String mail, String password, String profileP, String status, String userid, String username) {
        this.mail = mail;
        this.password = password;
        this.profileP = profileP;
        this.status = status;
        this.userid = userid;
        this.username = username;
    }

    public String getLastmasseg() {
        return lastmasseg;
    }

    public void setLastmasseg(String lastmasseg) {
        this.lastmasseg = lastmasseg;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfileP() {
        return profileP;
    }

    public void setProfileP(String profileP) {
        this.profileP = profileP;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
