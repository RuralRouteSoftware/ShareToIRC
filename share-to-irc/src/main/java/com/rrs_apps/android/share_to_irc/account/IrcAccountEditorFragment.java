package com.rrs_apps.android.share_to_irc.account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.widget.CheckBox;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockFragment;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;
import com.rrs_apps.android.share_to_irc.R;

/**
 * IrcAccountEditorFragment provides an interface for editing a Share To IRC account's data.
 */
@EFragment(R.layout.irc_account_editor_fragment)
public class IrcAccountEditorFragment extends SherlockFragment {
    public interface Listener {
        void onSave(IrcAccountEditorFragment fragment);
    }

    Listener listener;

    @ViewById
    EditText serverName;

    @ViewById
    EditText hostAddress;

    @ViewById
    EditText hostPort;

    @ViewById
    CheckBox usesSsl;

    @ViewById
    EditText nick;

    @ViewById
    EditText serverPassword;

    @ViewById
    EditText channelList;

    private AccountManager mAccountManager;

    @Click(R.id.save)
    void onSave() {
        if (listener != null) {
            listener.onSave(this);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mAccountManager = AccountManager.get(activity);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public String getServerName() {
        return serverName.getText().toString();
    }

    public String getNick() {
        return nick.getText().toString();
    }

    public String getHostAddress() {
        return hostAddress.getText().toString();
    }

    public String getHostPort() {
        return hostPort.getText().toString();
    }

    public boolean usesSsl() {
        return usesSsl.isChecked();
    }

    public String getServerPassword() {
        return serverPassword.getText().toString();
    }

    public String getChannelList() {
        return channelList.getText().toString();
    }

    /**
     * Fills all editor fields with an account's data
     * 
     * @param acct
     *            Data is loaded from this account
     */
    public void loadAccount(Account acct) {
        if (serverName == null) {
            // Views haven't been injected; loadAccount was probably called at the wrong time
            return;
        }

        serverName.setText(mAccountManager.getUserData(acct, IrcAccountHandler.ACCOUNT_KEY_SERVER_NAME));
        hostAddress.setText(mAccountManager.getUserData(acct, IrcAccountHandler.ACCOUNT_KEY_HOST_ADDRESS));
        hostPort.setText(mAccountManager.getUserData(acct, IrcAccountHandler.ACCOUNT_KEY_HOST_PORT));
        nick.setText(mAccountManager.getUserData(acct, IrcAccountHandler.ACCOUNT_KEY_NICK));
        usesSsl.setChecked(Boolean.parseBoolean(mAccountManager.getUserData(acct,
                IrcAccountHandler.ACCOUNT_KEY_IS_SSL)));
        serverPassword.setText(mAccountManager.getUserData(acct,
                IrcAccountHandler.ACCOUNT_KEY_SERVER_PASSWORD));
        channelList.setText(mAccountManager.getUserData(acct, IrcAccountHandler.ACCOUNT_KEY_CHANNEL_LIST));
    }

    /**
     * Saves all editor values as the account's data
     * 
     * @param acct
     *            Data is saved to this Account
     */
    public void saveAccount(Account acct) {
        mAccountManager.setUserData(acct, IrcAccountHandler.ACCOUNT_KEY_SERVER_NAME, serverName.getText()
                .toString());
        mAccountManager.setUserData(acct, IrcAccountHandler.ACCOUNT_KEY_HOST_ADDRESS, hostAddress.getText()
                .toString());
        mAccountManager.setUserData(acct, IrcAccountHandler.ACCOUNT_KEY_HOST_PORT, hostPort.getText()
                .toString());
        mAccountManager.setUserData(acct, IrcAccountHandler.ACCOUNT_KEY_NICK, nick.getText().toString());
        mAccountManager.setUserData(acct, IrcAccountHandler.ACCOUNT_KEY_IS_SSL, usesSsl.isChecked() ? "true"
                : "false");
        mAccountManager.setUserData(acct, IrcAccountHandler.ACCOUNT_KEY_SERVER_PASSWORD, serverPassword
                .getText().toString());
        mAccountManager.setUserData(acct, IrcAccountHandler.ACCOUNT_KEY_CHANNEL_LIST, channelList.getText()
                .toString());
    }

    /**
     * Checks the editor's data against basic account validity conditions, and displays errors on invalid fields.
     * 
     * @return false if the data is definitely invalid; otherwise true
     */
    public boolean validateData() {
        boolean isValid = true;

        if (getServerName().isEmpty()) {
            serverName.setError(getResources().getString(R.string.error_empty_server_name));

            isValid = false;
        }

        if (getHostAddress().isEmpty()) {
            hostAddress.setError(getResources().getString(R.string.error_empty_host_address));

            isValid = false;
        }

        if (getHostPort().isEmpty()) {
            hostPort.setError(getResources().getString(R.string.error_empty_host_port));

            isValid = false;
        }

        if (getNick().isEmpty()) {
            nick.setError(getResources().getString(R.string.error_empty_nick));

            isValid = false;
        }

        if (getChannelList().length() < 2 || !getChannelList().contains("#")) {
            channelList.setError(getResources().getString(R.string.error_invalid_channel_list));

            isValid = false;
        }

        return isValid;
    }
}
