package com.rrs_apps.android.share_to_irc.account;

import java.util.ArrayList;
import java.util.List;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.EFragment;

/**
 * IrcAccountListFragment displays a list of all Share To IRC accounts.
 */
@EFragment
public class IrcAccountListFragment extends SherlockListFragment {
    public interface Listener {
        /**
         * Called when an account in the list is clicked
         * 
         * @param acct
         *            The clicked account
         */
        void accountClicked(Account acct);

        /**
         * Called when the fragment has finished resuming
         * 
         * @param listFragment
         *            The resumed fragment
         */
        void onResume(IrcAccountListFragment listFragment);
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
    private SparseBooleanArray mCheckedItems;

    @AfterInject
    void setRetainInstance() {
        // Retain this instance so things like selection state are remembered
        setRetainInstance(true);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<Account>(getActivity(),
                    android.R.layout.simple_list_item_single_choice);

            setListAdapter(mAdapter);
        }

        reloadAccounts();
    }

    @Override
    public void onPause() {
        super.onPause();

        mCheckedItems = getListView().getCheckedItemPositions();
    }

    @Override
    public void onResume() {
        super.onResume();

        // Restore the selection state
        if (mCheckedItems != null) {
            for (int i = 0; i < mCheckedItems.size(); i++) {
                getListView().setItemChecked(mCheckedItems.keyAt(i), true);
            }
        }

        // Notify listener that this fragment has completed resuming
        if (listener != null) {
            listener.onResume(this);
        }
    }

    public void reloadAccounts() {
        mAdapter.clear();

        Account[] accounts = AccountManager.get(getActivity()).getAccountsByType(
                IrcAccountHandler.ACCOUNT_TYPE_SHARE_TO_IRC);

        for (Account acct : accounts) {
            mAdapter.add(new IrcAccount(acct));
        }

        setListAdapter(mAdapter); // Simultaneously clears selection state and refreshes the ListView
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
        if (getListView().getCheckedItemPosition() == ListView.INVALID_POSITION) {
            return null;
        }

        return mAdapter.getItem(getListView().getCheckedItemPosition());
    }

    public List<Account> getSelectedAccounts() {
        ArrayList<Account> selectedAccounts = new ArrayList<Account>();

        SparseBooleanArray checkedItems = getListView().getCheckedItemPositions();

        for (int i = 0; i < checkedItems.size(); i++) {
            selectedAccounts.add(mAdapter.getItem(checkedItems.keyAt(i)));
        }

        return selectedAccounts;
    }
}
