package com.rrs_apps.android.share_to_irc.account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ListActivity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;

/**
 * Simple account picker for IRC accounts
 *
 * Used when Google Play Services is not available
 */
@EActivity
public class IrcAccountPickerActivity extends ListActivity {
    public static final String EXTRA_SELECTED_ACCOUNT = "extra_selected_account";
    public static final String EXTRA_ALWAYS_SHOW = "extra_always_show";

    private static final int REQ_CODE_CREATE_ACCOUNT = 0;
    private ArrayAdapter<Account> mAdapter;

    @Override
    protected void onResume() {
        super.onResume();

        Account[] accounts = AccountManager.get(this).getAccountsByType(IrcAccountHandler.ACCOUNT_TYPE_SHARE_TO_IRC);

        // If no IRC accounts have been created, launch the creation activity
        if (accounts.length == 0) {
            startActivityForResult(new Intent(this, CreateIrcAccountActivity_.class), REQ_CODE_CREATE_ACCOUNT);
        } else if (accounts.length == 1) {
            // Only one account available, so use it
            selectAccountAndFinish(accounts[0]);
        } else {
            // Display a list of accounts to choose from
            mAdapter = new ArrayAdapter<Account>(this, android.R.layout.simple_list_item_1, accounts) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View v = super.getView(position, convertView, parent);

                    TextView text = (TextView) v.findViewById(android.R.id.text1);
                    text.setText(getItem(position).name);

                    return v;
                }
            };

            getListView().setAdapter(mAdapter);
        }
    }

    @OnActivityResult(REQ_CODE_CREATE_ACCOUNT)
    void finishIfNoAccounts(Intent data) {
        if (data == null) {
            // Account creator was launched, but the user canceled
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        // Set result according to selected account and finish activity
        Account account = mAdapter.getItem(position);

        selectAccountAndFinish(account);
    }

    private void selectAccountAndFinish(Account account) {
        Intent data = new Intent();
        data.putExtra(AccountManager.KEY_ACCOUNT_NAME, account.name);
        data.putExtra(AccountManager.KEY_ACCOUNT_TYPE, account.type);

        setResult(RESULT_OK, data);

        finish();
    }
}
