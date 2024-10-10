package com.example.smartchat;

public class MsgClass {
    String massage;
    String senderUid;
    long time;

    public MsgClass() {}

    public MsgClass(String massage, String senderUid, long time) {
        this.massage = massage;
        this.senderUid = senderUid;
        this.time = time;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
