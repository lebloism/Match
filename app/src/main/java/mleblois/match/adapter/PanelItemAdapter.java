package mleblois.match.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import mleblois.match.R;
import mleblois.match.model.ItemState;
import mleblois.match.model.PanelItem;


public class PanelItemAdapter extends BaseAdapter {
    private Context mContext;
    private int size;
    private List<PanelItem> items;
    private Random randomGenerator;

    public PanelItemAdapter(Context c, int size) {
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
        return items.get(position);
    }

    public boolean isAllMatched(){
        for (PanelItem i : items){
            if (i.getState()!=ItemState.MATCHED){
                return false;
            }
        }
        return true;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        //TODO optimize with recycling views

       // if (convertView == null) {
            // if it's not recycled, initialize some attributes
           // imageView = new ImageView(mContext);
            //etc
        //} else {
          //  imageView = (ImageView) convertView;
        //}


            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);

    PanelItem panelItem = items.get(position);
        imageView.setImageResource(panelItem.getDrawable());
        Animation currentAnimation =  imageView.getAnimation();
        if (currentAnimation!=null) {
            currentAnimation.cancel();
            imageView.setAnimation(null);
        }
        if (panelItem.getState()== ItemState.MATCHED){

            Animation animation = new AlphaAnimation(0.5f, 0.5f); // Change alpha
            // from fully
            // visible to
            // invisible
            animation.setDuration(500); // duration - half a second
            animation.setInterpolator(new LinearInterpolator()); // do not alter
            // animation
            // rate
            animation.setRepeatCount(Animation.INFINITE); // Repeat animation
            // infinitely
            animation.setRepeatMode(Animation.REVERSE); // Reverse animation at

            imageView.setAnimation(animation);
        } else  if (panelItem.getState()== ItemState.NORMAL){

            Animation animation = new AlphaAnimation(1, 1); // Change alpha
            // from fully
            // visible to
            // invisible
            animation.setDuration(500); // duration - half a second
            animation.setInterpolator(new LinearInterpolator()); // do not alter
            // animation
            // rate
            animation.setRepeatCount(Animation.INFINITE); // Repeat animation
            // infinitely
            animation.setRepeatMode(Animation.REVERSE); // Reverse animation at

            imageView.setAnimation(animation);
        } else   if (panelItem.getState()== ItemState.WAITING){
            //Blinking
        Animation animation = new AlphaAnimation(1, 0); // Change alpha
        // from fully
        // visible to
        // invisible
        animation.setDuration(500); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter
        // animation
        // rate
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation
        // infinitely
        animation.setRepeatMode(Animation.REVERSE); // Reverse animation at

        imageView.setAnimation(animation);

        }
        return imageView;
    }

    private Integer getRandomDrawable(){
      return mThumbIds[randomGenerator.nextInt(mThumbIds.length)];
    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.bleu_sombre, R.drawable.lune, R.drawable.orange, R.drawable.rose, R.drawable.terre, R.drawable.vert
    };
}