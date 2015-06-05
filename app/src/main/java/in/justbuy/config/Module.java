package in.justbuy.config;

import android.app.Application;
import android.net.ConnectivityManager;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;

import in.justbuy.http.RequestManager;
import in.justbuy.http.ServerConfiguration;

/**
 * @author aritra
 */
public class Module extends AbstractModule {

    public Module(Application application) {

    }

    /* (non-Javadoc)
     * @see com.google.inject.AbstractModule#configure()
     */
    @Override
    protected void configure() {

    }

    @Provides
    @Inject
    public RequestManager provideRequestManager(ConnectivityManager connectivityManager,
                                                ServerConfiguration serverConfiguration) {
        RequestManager requestManager = new RequestManager(connectivityManager, serverConfiguration);
        return requestManager;
    }

}
