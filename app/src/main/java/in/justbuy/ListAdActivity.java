package in.justbuy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.inject.Inject;
import com.google.inject.Provider;

import java.util.List;

import in.justbuy.http.RequestManager;
import in.justbuy.http.RequestPromise;
import in.justbuy.model.Ad;
import in.justbuy.model.Category;
import in.justbuy.utils.AdListAdapter;
import in.justbuy.utils.Parsers;
import roboguice.activity.RoboListActivity;
import roboguice.inject.ContentView;

@ContentView(R.layout.activity_list_ad)
public class ListAdActivity extends RoboListActivity {

    @Inject
    private Provider<RequestManager> requestManagerProvider;

    private AdListAdapter adapter;

    @Override
    protected void onResume() {
        super.onResume();

        adapter = new AdListAdapter(this);
        setListAdapter(adapter);

        int categoryId = 0;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            categoryId = extras.getInt("id");
        }

        if (categoryId == 0) {
            Toast.makeText(this, "Invalid Category ID", Toast.LENGTH_SHORT).show();
            finish();
        }

        loadCategory(categoryId);

        getAds(categoryId);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Ad ad = adapter.getItem(position);

        openAd(ad.id);
    }

    private void openAd(int id) {
        Intent intent = new Intent(this, ViewAdActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    private void getAds(int categoryId) {
        RequestManager requestManager = requestManagerProvider.get();
        requestManager.setResourcePath("/category/" + categoryId + "/item");
        requestManager.setToDo(new RequestPromise() {
            @Override
            public void onSuccess(String data, int status) {
                Log.i("ADS", data);
                List<Ad> ads = Parsers.parseAds(data);
                adapter.addAll(ads);
                adapter.notifyDataSetChanged();
            }
        });
        requestManager.execute(null, null);
    }

    private void loadCategory(int id) {
        RequestManager requestManager = requestManagerProvider.get();
        requestManager.setResourcePath("/category/" + id);
        requestManager.setToDo(new RequestPromise() {
            @Override
            public void onSuccess(String data, int status) {
                Log.i("CAT", data);
                Category category = Parsers.parseCategory(data);
                getActionBar().setTitle(category.name);
            }
        });
        requestManager.execute(null, null);
    }

}
