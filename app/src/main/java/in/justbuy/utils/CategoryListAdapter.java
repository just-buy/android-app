package in.justbuy.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import in.justbuy.R;
import in.justbuy.model.Category;

/**
 * @author aritra
 */
public class CategoryListAdapter extends ArrayAdapter<Category> {

    private final LayoutInflater layoutInflater;

    public CategoryListAdapter(Context context) {
        super(context, R.layout.list_item_category);
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewCache viewCache;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item_category, parent, false);
            viewCache = new ViewCache();
            viewCache.nameView = (TextView) convertView.findViewById(R.id.list_item_category_name);
            convertView.setTag(viewCache);
        } else {
            viewCache = (ViewCache) convertView.getTag();
        }

        Category category = getItem(position);

        viewCache.nameView.setText(category.name);

        return convertView;
    }

    private static class ViewCache {
        public TextView nameView;
    }
}
