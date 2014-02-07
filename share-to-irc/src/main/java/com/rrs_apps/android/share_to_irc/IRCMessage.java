package com.rrs_apps.android.share_to_irc;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel
public class IRCMessage {
    protected String text;
    protected String address;
    protected int port;
    protected String nick;
    protected int useSsl;
    protected String password;
    protected String[] channels;

    @ParcelConstructor
    public IRCMessage(String text, String address, int port, String nick, String password, String[] channels) {
        this.text = text;
        this.address = address;
        this.port = port;
        this.nick = nick;
        this.password = password;
        this.channels = channels;
    }

    public String getText() {
        return text;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public String getNick() {
        return nick;
    }

    public String getPassword() {
        return password;
    }

    public String[] getChannels() {
        return channels;
    }

    public void setUsesSsl(boolean usesSsl) {
        this.useSsl = usesSsl ? 1 : 0;
    }

    public boolean usesSsl() {
        return (this.useSsl == 0) ? false : true;
    }
}
