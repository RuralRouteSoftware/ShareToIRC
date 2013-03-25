package com.rrs_apps.android.share_to_irc;

import org.schwering.irc.lib.IRCConnection;
import org.schwering.irc.lib.IRCEventListener;
import org.schwering.irc.lib.IRCModeParser;
import org.schwering.irc.lib.IRCUser;

import android.app.Activity;

/**
 * This IRCEventListener sends shared text to all configured IRC channels, then disconnects and finishes the activity.
 */
class SendTextAndFinishListener implements IRCEventListener {
    private final Activity activity;
    private final String[] channels;
    private final IRCConnection conn;
    private String text;
    private String quitMessage;

    SendTextAndFinishListener(Activity activity, String text, String[] channels, IRCConnection conn,
            String quitMessage) {
        this.activity = activity;
        this.channels = channels;
        this.conn = conn;
        this.text = text;
        this.quitMessage = quitMessage;
    }

    @Override
    public void onDisconnected() {
        // Finish the activity
        activity.finish();
    }

    @Override
    public void onError(String arg0) {
    }

    @Override
    public void onError(int arg0, String arg1) {
    }

    @Override
    public void onInvite(String arg0, IRCUser arg1, String arg2) {
    }

    @Override
    public void onJoin(String arg0, IRCUser arg1) {
    }

    @Override
    public void onKick(String arg0, IRCUser arg1, String arg2, String arg3) {
    }

    @Override
    public void onMode(String arg0, IRCUser arg1, IRCModeParser arg2) {
    }

    @Override
    public void onMode(IRCUser arg0, String arg1, String arg2) {
    }

    @Override
    public void onNick(IRCUser arg0, String arg1) {
    }

    @Override
    public void onNotice(String arg0, IRCUser arg1, String arg2) {
    }

    @Override
    public void onPart(String arg0, IRCUser arg1, String arg2) {
    }

    @Override
    public void onPing(String arg0) {
    }

    @Override
    public void onPrivmsg(String arg0, IRCUser arg1, String arg2) {
    }

    @Override
    public void onQuit(IRCUser arg0, String arg1) {
    }

    @Override
    public void onRegistered() {
        // Send text to all channels and disconnect
        for (String chan : channels) {
            conn.doJoin(chan);
            conn.doPrivmsg(chan, text);
        }

        conn.doQuit(quitMessage);
    }

    @Override
    public void onReply(int arg0, String arg1, String arg2) {
    }

    @Override
    public void onTopic(String arg0, IRCUser arg1, String arg2) {
    }

    @Override
    public void unknown(String arg0, String arg1, String arg2, String arg3) {
    }
}