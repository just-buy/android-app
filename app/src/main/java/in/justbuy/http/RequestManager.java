package in.justbuy.http;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.widget.ContentLoadingProgressBar;

import com.google.inject.Inject;

import org.apache.commons.io.IOUtils;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @author aritra
 */
public class RequestManager extends AsyncTask<Map<String, String>, Void, Void> {

    private static final String charset = "UTF-8";

    private final ConnectivityManager connectivityManager;
    private final ServerConfiguration serverConfiguration;
    protected HttpURLConnection connection;
    private RequestMethod method;
    private String resourcePath;
    private RequestPromise toDo;
    private URL url;
    private int status;
    private String data;
    private Exception cause;

    private ContentLoadingProgressBar contentLoadingProgressBar;

    @Inject
    public RequestManager(ConnectivityManager connectivityManager, ServerConfiguration serverConfiguration) {
        this.connectivityManager = connectivityManager;
        this.serverConfiguration = serverConfiguration;

        method = RequestMethod.GET;

        status = 0;
        data = null;
        cause = null;

        url = null;

        contentLoadingProgressBar = null;
    }

    public void setMethod(RequestMethod method) {
        this.method = method;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public void setToDo(RequestPromise toDo) {
        this.toDo = toDo;
    }

    public void showProgressDialog(Context context, String info) {
        if (info != null) {
            contentLoadingProgressBar = new ContentLoadingProgressBar(context);
//          contentLoadingProgressBar.setCancelable(false);
//          contentLoadingProgressBar.setTitle("Connecting to server");
//          contentLoadingProgressBar.setMessage(info);
        }
    }

    private void doPost(Map<String, String> dataParams) throws Exception {
        // Encoding Data Parameters
        String encodedDataParams = encodeParams(dataParams);
        byte[] encodedDataParamsByteArray = encodedDataParams.getBytes(charset);

        // Setting Connection Parameters
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setDoOutput(true);

        // Streaming Data Parameters
        OutputStream output = connection.getOutputStream();
        output.write(encodedDataParamsByteArray);
        output.flush();
        output.close();
    }

    private String encodeParams(Map<String, String> params) throws Exception {
        String encodedParams = "";
        boolean flag = true;
        for (Map.Entry<String, String> param : params.entrySet()) {
            encodedParams += flag ? "" : "&";
            encodedParams += URLEncoder.encode(param.getKey(), charset) + "="
                    + URLEncoder.encode(param.getValue(), charset);
            flag = false;
        }
        return encodedParams;
    }

    protected Uri.Builder getUriBuilder() {
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme(serverConfiguration.getScheme());
        uriBuilder.encodedAuthority(serverConfiguration.getHost());

        for (String pathPart : resourcePath.split("/")) {
            uriBuilder.appendPath(pathPart);
        }

        return uriBuilder;
    }

    protected void setConnectionProperties() {
        // Setting Connection Parameters
        connection.setInstanceFollowRedirects(false);
//      connection.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
//      connection.addRequestProperty("User-Agent", "Mozilla");
        connection.addRequestProperty("Referer", "partners.uber.com");
    }

    private void createNewConnection(Map<String, String> queryParams) throws Exception {
        Uri.Builder uriBuilder = getUriBuilder();

        // Appending Query Parameters
        if (queryParams != null) {
            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                uriBuilder.appendQueryParameter(entry.getKey(), entry.getValue());
            }
        }

        // Creating new Connection
        URL url = this.url == null ? new URL(uriBuilder.build().toString()) : this.url;
        HttpURLConnection.setFollowRedirects(false);
        connection = (HttpURLConnection) url.openConnection();

        setConnectionProperties();
    }

    /* (non-Javadoc)
     * @see android.os.AsyncTask#onPreExecute()
     */
    @Override
    protected void onPreExecute() {
        if (contentLoadingProgressBar != null) {
            contentLoadingProgressBar.show();
        }
        super.onPreExecute();
    }

    /* (non-Javadoc)
     * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
     */
    @Override
    protected Void doInBackground(Map<String, String>... params) {
        Map<String, String> queryParams = params[0];
        Map<String, String> dataParams = params[1];

        if (!isNetworkActive()) {
            cause = new NetworkErrorException("Network connection unavailable");
        } else {
            try {
                while (true) {
                    createNewConnection(queryParams);
                    connection.setRequestMethod(method.toString());

                    switch (method) {
                        case GET:
                            // Nothing extra to be done
                            break;
                        case POST:
                            doPost(dataParams);
                            break;
                        case PUT:
                            // TODO
                            break;
                        case DELETE:
                            // TODO
                            break;
                        case PATCH:
                            // TODO
                            break;
                    }

                    status = connection.getResponseCode();

                    switch (status) {
                        case HttpURLConnection.HTTP_MOVED_PERM:
                        case HttpURLConnection.HTTP_MOVED_TEMP:
                            setMethod(RequestMethod.GET);
                            queryParams = null;
                            String location = connection.getHeaderField("Location");
                            URL base = new URL(connection.getURL().toString());
                            URL next = new URL(base, location);
                            this.url = new URL(next.toExternalForm());
                            continue;
                    }

                    break;
                }

                data = IOUtils.toString(connection.getInputStream());
                connection.disconnect();
            } catch (Exception cause) {
                this.cause = cause;
            }
        }
        return null;
    }

    /* (non-Javadoc)
     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
     */
    @Override
    protected void onPostExecute(Void result) {
        if (contentLoadingProgressBar != null) {
            contentLoadingProgressBar.hide();
        }

        if (toDo != null) {
            if (cause == null) {
                toDo.onSuccess(data, status);
            } else {
                toDo.onFailure(cause, status);
            }
        }
    }

    private boolean isNetworkActive() {
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public enum RequestMethod {
        GET, POST, PUT, DELETE, PATCH
    }

}

