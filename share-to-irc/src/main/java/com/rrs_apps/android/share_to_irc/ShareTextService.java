package com.rrs_apps.android.share_to_irc;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.rrs_apps.java.jirclib.IRCConnection;
import com.rrs_apps.java.jirclib.ssl.SSLIRCConnection;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EService;
import org.androidannotations.annotations.UiThread;
import org.parceler.Parcels;

import java.io.IOException;

@EService
public class ShareTextService extends Service implements SendTextAndFinishListener.ICallback {
    public static final String EXTRA_IRC_MESSAGE = "extra_irc_message";
    private static final int NOTIFICATION_ID = 0;

    private final String TAG = getClass().getName();

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Received text to send to IRC");

        IRCMessage sendText = Parcels.unwrap(intent.getParcelableExtra(EXTRA_IRC_MESSAGE));

        sendTextToServer(sendText.getText(), sendText.getAddress(), sendText.getPort(), sendText.getNick(), sendText.usesSsl(), sendText.getPassword(), sendText.getChannels());

        return START_NOT_STICKY;
    }

    @Background
    void sendTextToServer(String text, String address, int port, String nick, boolean useSsl,
                          String password, final String[] channels) {
        Log.d(TAG, "Connecting to " + address + ":" + port + " as " + nick + ", SSL: " + useSsl);

        // Create connection
        final IRCConnection conn;
        if (useSsl) {
            conn = new SSLIRCConnection(address, port, port, password, nick, nick, nick);
        } else {
            conn = new IRCConnection(address, port, port, password, nick, nick, nick);
        }

        // Add listener that sends the text and finishes the activity upon connecting
        SendTextAndFinishListener ircListener = new SendTextAndFinishListener(text, channels, conn, getResources()
                .getString(R.string.quit_message));
        ircListener.setCallback(this);

        conn.addIRCEventListener(ircListener);

        try {
            conn.connect();

            buildAndDisplayNotification(R.string.sharing, true);
        } catch (IOException e) {
            Log.i(TAG, "Error connecting to IRC", e);

            onError(R.string.error_connecting, e);
        }
    }

    private void buildAndDisplayNotification(int messageId, boolean showProgress) {
        PendingIntent emptyIntent = PendingIntent.getActivity(this, 0, new Intent(), PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText(getResources().getString(messageId))
                .setContentIntent(emptyIntent);

        builder.setAutoCancel(true);

        if (showProgress) {
            builder.setProgress(0, 0, true);
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    @Override
    @UiThread
    public void onSuccess() {
        Log.d(TAG, "Successfully shared to IRC");

        buildAndDisplayNotification(R.string.share_success, false);
    }

    @Override
    @UiThread
    public void onError() {
        Log.e(TAG, "Error sharing to IRC");

        onError(R.string.share_failure);
    }

    private void onError(int errorMessageId) {
        onError(errorMessageId, null);
    }

    /**
     * Displays a notification with an error message
     *
     * @param errorMessageId The resource ID of the error message to display in the notification
     * @param optionalException If not null, the exception that caused the error
     */
    @UiThread
    void onError(int errorMessageId, Exception optionalException) {
        if (optionalException != null) {
            optionalException.printStackTrace();
        }

        buildAndDisplayNotification(errorMessageId, false);
    }
}
