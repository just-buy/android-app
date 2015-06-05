package in.justbuy.http;

import android.content.Context;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import in.justbuy.R;

/**
 * @author aritra
 */
@Singleton
public class ServerConfiguration {

    private final String scheme;
    private final String host;

    @Inject
    public ServerConfiguration(Context context) {
        scheme = context.getResources().getString(R.string.server_scheme);
        host = context.getResources().getString(R.string.server_url);
    }

    public String getScheme() {
        return scheme;
    }

    public String getHost() {
        return host;
    }
}
