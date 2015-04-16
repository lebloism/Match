package mleblois.match.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import mleblois.match.R;
import mleblois.match.model.PanelItem;


public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private int size;
    private List<PanelItem> items;
    private Random randomGenerator;

    public ImageAdapter(Context c, int size) {
        mContext = c;
        this.size = size;
        randomGenerator = new Random();
        initializeItems();
    }

    public void initializeItems(){
        items = new ArrayList<>();
        for (int i=0;i<size;i++){
            PanelItem item = new PanelItem();
            item.setDrawable(getRandomDrawable());
            items.add(item);
        }
    }

    public int getCount() {
        return items.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(items.get(position).getDrawable());
        return imageView;
    }

    private Integer getRandomDrawable(){
      return mThumbIds[randomGenerator.nextInt(mThumbIds.length)];
    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5
    };
}