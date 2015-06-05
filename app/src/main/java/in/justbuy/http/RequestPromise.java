package in.justbuy.http;

import android.accounts.NetworkErrorException;
import android.util.Log;

import java.net.ConnectException;

/**
 * @author aritra
 */
public abstract class RequestPromise {

    public abstract void onSuccess(String data, int status);

    public void onFailure(Exception cause, int status) {
        Log.w("HTTP_STATUS", status + "");
    }

    protected Exception handleKnownCases(Exception cause) {
        Log.w("RequestPromise", cause.getMessage());
        if (cause instanceof NetworkErrorException) {
            cause = new NetworkErrorException("Please check your network connection.", cause);
        } else if (cause instanceof ConnectException) {
            cause = new ConnectException("Cannot connect to server.");
        }
        return cause;
    }

}
