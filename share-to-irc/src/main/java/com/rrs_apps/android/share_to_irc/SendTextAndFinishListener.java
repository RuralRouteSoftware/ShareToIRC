package com.rrs_apps.android.share_to_irc;

import android.content.Context;
import android.util.Log;

import com.rrs_apps.java.jirclib.IRCConnection;
import com.rrs_apps.java.jirclib.IRCEventListener;
import com.rrs_apps.java.jirclib.IRCModeParser;
import com.rrs_apps.java.jirclib.IRCUser;

/**
 * This IRCEventListener sends shared text to all configured IRC channels, then disconnects and finishes the context.
 */
class SendTextAndFinishListener implements IRCEventListener {
    private final String TAG = getClass().getName();
    private final Context context;
    private final String[] channels;
    private final IRCConnection conn;
    private String text;
    private String quitMessage;
    private boolean errored;
    private ICallback callback;
    private boolean quitting;

    SendTextAndFinishListener(Context context, String text, String[] channels, IRCConnection conn,
                              String quitMessage) {
        this.context = context;
        this.channels = channels;
        this.conn = conn;
        this.text = text;
        this.quitMessage = quitMessage;
    }

    @Override
    public void onDisconnected() {
        if (callback != null) {
            if (errored && !quitting) {
                callback.onError();
            } else {
                callback.onSuccess();
            }
        }
    }

    @Override
    public void onError(String msg) {
        Log.e(TAG, msg);

        errored = true;
    }

    @Override
    public void onError(int errorNum, String msg) {
        onError(msg);
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
            Log.d(TAG, "Sending to " + chan);

            conn.doJoin(chan);
            conn.doPrivmsg(chan, text);
        }

        quitting = true;
        conn.doQuit(quitMessage);
    }

    @Override
    public void onReply(int arg0, String arg1, String arg2) {
    }

    @Override
    public void onTopic(String arg0, IRCUser arg1, String arg2) {
    }

    @Override
    public void unknown(String prefix, String command, String middle, String trailing) {
        onError(trailing);
    }

    public void setCallback(ICallback callback) {
        this.callback = callback;
    }

    public interface ICallback {
        /**
         * Called if text is successfully sent to the IRC server
         */
        public void onSuccess();

        /**
         * Called if there is an error sending text to the IRC server
         */
        public void onError();
    }
}