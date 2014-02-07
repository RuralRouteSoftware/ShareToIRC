package com.rrs_apps.android.share_to_irc.account;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.common.AccountPicker;

/**
 * IrcAccountHandler implements the necessary AbstractAccountAuthenticator methods for managing Share To IRC accounts.
 */
public class IrcAccountHandler extends AbstractAccountAuthenticator {
    public static final String ACCOUNT_TYPE_SHARE_TO_IRC = "com.rrs_apps.android.share_to_irc";

    public static final String ACCOUNT_KEY_SERVER_NAME = "server_name";
    public static final String ACCOUNT_KEY_HOST_ADDRESS = "host_address";
    public static final String ACCOUNT_KEY_HOST_PORT = "host_port";
    public static final String ACCOUNT_KEY_IS_SSL = "is_ssl";
    public static final String ACCOUNT_KEY_NICK = "nick";
    public static final String ACCOUNT_KEY_SERVER_PASSWORD = "server_password";
    public static final String ACCOUNT_KEY_CHANNEL_LIST = "channel_list";

    private Context mContext;

    public IrcAccountHandler(Context context) {
        super(context);

        mContext = context;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType,
            String[] requiredFeatures, Bundle options) throws NetworkErrorException {
        if (!accountType.equals(ACCOUNT_TYPE_SHARE_TO_IRC))
            return null;

        // Return an intent to launch the account creation activity
        Intent addIntent = new Intent(mContext, CreateIrcAccountActivity_.class);
        addIntent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);

        Bundle addBundle = new Bundle();
        addBundle.putParcelable(AccountManager.KEY_INTENT, addIntent);

        return addBundle;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options)
            throws NetworkErrorException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType,
            Bundle options) throws NetworkErrorException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features)
            throws NetworkErrorException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account,
            String authTokenType, Bundle options) throws NetworkErrorException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Displays an account picker to the user
     * 
     * @param ctx
     *            Parent activity for the picker. This activity will have its onActivityResult called with the resulting
     *            account.
     * @param requestCode
     *            This request code will be passed to onActivityResult.
     * @param selectedAccount
     *            If not null, this Account will be rendered as currently selected in the picker.
     * @param alwaysShow
     *            If true, the picker will always be shown, even if there is only one account that matches the given
     *            types.
     * @param types
     *            One or more account types. Only these types will be displayed in the picker.
     */
    public static void launchAccountPicker(Activity ctx, int requestCode, Account selectedAccount,
            boolean alwaysShow, String... types) {
        Intent intent = AccountPicker.newChooseAccountIntent(selectedAccount, null, types, alwaysShow, null,
                null, null, null);

        if (ctx.getPackageManager().resolveActivity(intent, 0) == null) {
            // User probably doesn't have Google Play Services on their device
            // Show alternate account picker
            intent = new Intent(ctx, IrcAccountPickerActivity_.class);
            intent.putExtra(IrcAccountPickerActivity.EXTRA_SELECTED_ACCOUNT, selectedAccount);
            intent.putExtra(IrcAccountPickerActivity.EXTRA_ALWAYS_SHOW, alwaysShow ? 1 : 0);
        }

        ctx.startActivityForResult(intent, requestCode);
    }

    /**
     * Launches an Activity to create a new account
     * 
     * @param ctx
     *            The parent Activity for the request
     * @param mgr
     *            AccountManager to use for the request
     * @param callback
     *            If not null, this callback will be invoked when the request completes
     */
    public static void launchAccountCreator(Activity ctx, AccountManager mgr,
            AccountManagerCallback<Bundle> callback) {
        mgr.addAccount(ACCOUNT_TYPE_SHARE_TO_IRC, null, null, null, ctx, callback, null);
    }
}
