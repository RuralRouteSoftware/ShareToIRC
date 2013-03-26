package com.rrs_apps.android.share_to_irc.account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.googlecode.androidannotations.annotations.EFragment;

/**
 * IrcAccountListFragment displays a list of all Share To IRC accounts.
 */
@EFragment
public class IrcAccountListFragment extends SherlockListFragment {
    public interface Listener {
        void accountClicked(Account acct);
    }

    /**
     * Simple Account subclass to change the behavior of toString()
     */
    class IrcAccount extends Account {
        public IrcAccount(Account acct) {
            super(acct.name, acct.type);
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private ArrayAdapter<Account> mAdapter;
    private Listener listener;

    @Override
    public void onStart() {
        super.onStart();

        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<Account>(getActivity(),
                    android.R.layout.simple_list_item_single_choice);

            setListAdapter(mAdapter);
        }

        refreshAccountList();
    }

    private void refreshAccountList() {
        mAdapter.clear();

        Account[] accounts = AccountManager.get(getActivity()).getAccountsByType(
                IrcAccountHandler.ACCOUNT_TYPE_SHARE_TO_IRC);

        for (Account acct : accounts) {
            mAdapter.add(new IrcAccount(acct));
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (listener != null) {
            listener.accountClicked((Account) l.getItemAtPosition(position));
        }
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public Account getSelectedAccount() {
        return mAdapter.getItem(getListView().getCheckedItemPosition());
    }
}
