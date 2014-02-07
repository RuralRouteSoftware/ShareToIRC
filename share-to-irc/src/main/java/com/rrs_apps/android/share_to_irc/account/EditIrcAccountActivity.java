package com.rrs_apps.android.share_to_irc.account;

import android.accounts.Account;
import android.support.v7.app.ActionBarActivity;

import com.rrs_apps.android.share_to_irc.R;
import com.rrs_apps.android.share_to_irc.account.IrcAccountEditorFragment.Listener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.FragmentById;

/**
 * EditIrcAccountActivity provides a standalone editor interface for IRC accounts.
 */
@EActivity(R.layout.edit_irc_account_activity)
public class EditIrcAccountActivity extends ActionBarActivity implements Listener {
    @FragmentById(R.id.irc_account_editor_fragment)
    IrcAccountEditorFragment editFragment;

    @Extra(IrcAccountHandler.ACCOUNT_TYPE_SHARE_TO_IRC)
    Account account;

    @AfterViews
    void onLoad() {
        editFragment.setListener(this);
        editFragment.loadAccount(account);
    }

    @Override
    public void onSave(IrcAccountEditorFragment editFragment) {
        if (editFragment.validateData()) {
            editFragment.saveAccount(account);
        }

        finish();
    }
}
