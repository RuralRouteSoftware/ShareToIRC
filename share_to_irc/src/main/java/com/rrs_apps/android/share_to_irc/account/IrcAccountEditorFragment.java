package com.rrs_apps.android.share_to_irc.account;

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

    @Click(R.id.save)
    void onSave() {
        if (listener != null) {
            listener.onSave(this);
        }
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
}
