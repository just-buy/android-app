package in.justbuy.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import in.justbuy.R;
import in.justbuy.model.Ad;

/**
 * @author aritra
 */
public class AdListAdapter extends ArrayAdapter<Ad> {

    private final LayoutInflater layoutInflater;

    public AdListAdapter(Context context) {
        super(context, R.layout.list_item_ad);
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewCache viewCache;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item_ad, parent, false);
            viewCache = new ViewCache();
            viewCache.titleView = (TextView) convertView.findViewById(R.id.list_item_ad_title);
            viewCache.valueView = (TextView) convertView.findViewById(R.id.list_item_ad_value);
            convertView.setTag(viewCache);
        } else {
            viewCache = (ViewCache) convertView.getTag();
        }

        Ad ad = getItem(position);

        viewCache.titleView.setText(ad.title);
        viewCache.valueView.setText(ad.value + "");

        return convertView;
    }

    private static class ViewCache {
        public TextView titleView;
        public TextView valueView;
    }
}
