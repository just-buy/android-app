package in.justbuy;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.inject.Inject;
import com.google.inject.Provider;

import in.justbuy.http.RequestManager;
import in.justbuy.http.RequestPromise;
import in.justbuy.model.Ad;
import in.justbuy.utils.Parsers;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * @author aritra
 */
@ContentView(R.layout.activity_view_ad)
public class ViewAdActivity extends RoboActivity {

    @InjectView(R.id.view_ad_value)
    private TextView valueView;

    @InjectView(R.id.view_ad_description)
    private TextView descriptionView;

    @Inject
    private Provider<RequestManager> requestManagerProvider;

    private int id;

    @Override
    protected void onResume() {
        super.onResume();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getInt("id");
        }

        if (id == 0) {
            Toast.makeText(this, "Invalid Ad ID", Toast.LENGTH_SHORT).show();
            finish();
        }

        loadAd();
    }

    private void loadAd() {
        RequestManager requestManager = requestManagerProvider.get();
        requestManager.setResourcePath("/item/" + id);
        requestManager.setToDo(new RequestPromise() {
            @Override
            public void onSuccess(String data, int status) {
                Log.i("AD", data);
                Ad ad = Parsers.parseAd(data);

                getActionBar().setTitle(ad.title);
                valueView.setText(ad.value + "");
                descriptionView.setText(ad.description);
            }
        });
        requestManager.execute(null, null);
    }
}
