package com.example.volley.view;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.v4.util.LruCache;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.example.volley.R;
import com.example.volley.util.App;
import com.example.volley.util.ImageManager;

/**
 * Created by storm on 14-3-26.
 */
public class PicsAdapter extends CursorAdapter {
	private static final int[] COLORS = { R.color.holo_blue_light,
			R.color.holo_green_light, R.color.holo_orange_light,
			R.color.holo_purple_light, R.color.holo_red_light };

	private LayoutInflater mLayoutInflater;
	private Drawable mDefaultImageDrawable;
	private Resources mResource;

	public PicsAdapter(Context context, Cursor cursor) {
		super(context,cursor);  
		mResource = context.getResources();
		mLayoutInflater = LayoutInflater.from(context);

	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
		return mLayoutInflater.inflate(R.layout.row_image, null);
	}

	@Override
    public void bindView(View view, Context context, Cursor cursor) {
		ImageView imageView = (ImageView) view.findViewById(R.id.imageListView);
        String url = cursor.getString(1);
        mDefaultImageDrawable = new ColorDrawable(mResource.getColor(COLORS[cursor.getPosition() % COLORS.length]));
//        ImageListener imageListener1 = ImageLoader.getImageListener(imageView, android.R.drawable.ic_menu_rotate, android.R.drawable.ic_delete);
        ImageManager.loadImage(url,ImageManager.getImageListener(imageView, mDefaultImageDrawable, mDefaultImageDrawable));
    }

}
