package in.justbuy;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.google.inject.Inject;
import com.google.inject.Provider;

import java.util.List;

import in.justbuy.http.RequestManager;
import in.justbuy.http.RequestPromise;
import in.justbuy.model.Category;
import in.justbuy.utils.CategoryListAdapter;
import in.justbuy.utils.Parsers;
import roboguice.activity.RoboListActivity;
import roboguice.inject.ContentView;

@ContentView(R.layout.activity_list_category)
public class ListCategoryActivity extends RoboListActivity {

    @Inject
    private Provider<RequestManager> requestManagerProvider;

    private CategoryListAdapter adapter;

    @Override
    protected void onResume() {
        super.onResume();

        adapter = new CategoryListAdapter(this);
        setListAdapter(adapter);

        getCategories();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Category category = adapter.getItem(position);

        openCategory(category.id);
    }

    private void openCategory(int id) {
        Intent intent = new Intent(this, ListAdActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    private void getCategories() {
        RequestManager requestManager = requestManagerProvider.get();
        requestManager.setResourcePath("/category");
        requestManager.setToDo(new RequestPromise() {
            @Override
            public void onSuccess(String data, int status) {
                Log.i("CATS", data);
                List<Category> categories = Parsers.parseCategories(data);
                adapter.addAll(categories);
                adapter.notifyDataSetChanged();
            }
        });
        requestManager.execute(null, null);
    }

}
