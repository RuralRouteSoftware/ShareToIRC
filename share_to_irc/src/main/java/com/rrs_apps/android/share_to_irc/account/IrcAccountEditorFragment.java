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
        void onSave(IrcAccountEditorFragment editIrcAccountFragment);
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
    }
}
