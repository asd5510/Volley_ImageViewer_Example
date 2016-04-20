package com.example.volley.util;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.example.volley.data.DataProvider;

public class ImageManager {
	private static RequestQueue mQueue;
	private static ImageLoader mImageLoader;

	static {
		mQueue = Volley.newRequestQueue(App.getContext());
		int cacheSize = 8 * 1024 * 1024; // 4MiB
		final LruCache<String, Bitmap> mImageCache = new LruCache<String, Bitmap>(
				cacheSize);
		ImageCache imageCache = new ImageCache() {
			@Override
			public Bitmap getBitmap(String key) {
				return mImageCache.get(key);
			}

			@Override
			public void putBitmap(String key, Bitmap value) {
				mImageCache.put(key, value);
			}
		};
		mImageLoader = new ImageLoader(mQueue, imageCache);
	}

	private void loadImageByNetworkImageView(NetworkImageView imageView) {
		// imageView2.setTag("url");
		imageView
				.setImageUrl(
						"https://images2.alphacoders.com/775/thumb-350-77572.jpg",
						mImageLoader);
		imageView.setDefaultImageResId(android.R.drawable.ic_menu_rotate);
		imageView.setErrorImageResId(android.R.drawable.ic_delete);
	}

	public void loadImageByImageLoader(ImageView imageView) {
		// imageView是一个ImageView实例
		// ImageLoader.getImageListener的第二个参数是默认的图片resource id
		// 第三个参数是请求失败时候的资源id，可以指定为0
		ImageListener imageListener1 = ImageLoader
				.getImageListener(imageView, android.R.drawable.ic_menu_rotate,
						android.R.drawable.ic_delete);
		mImageLoader
				.get("https://images2.alphacoders.com/775/thumb-350-77572.jpg",
						imageListener1);

	}

	public static void loadImage(final String imageUrl,
			ImageLoader.ImageListener listener) {
		mImageLoader.get(imageUrl, listener);
	}

	public static ImageLoader.ImageListener getImageListener(
			final ImageView view, final Drawable defaultImageDrawable,
			final Drawable errorImageDrawable) {
		return new ImageLoader.ImageListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				if (errorImageDrawable != null) {
					view.setImageDrawable(errorImageDrawable);
				}
			}

			@Override
			public void onResponse(ImageLoader.ImageContainer response,
					boolean isImmediate) {
				if (response.getBitmap() != null) {
//					Bitmap comp = BitmapUtils.comp(response.getBitmap());
					TransitionDrawable transitionDrawable = new TransitionDrawable(
                            new Drawable[]{
                                    defaultImageDrawable,
                                    new BitmapDrawable(App.getContext().getResources(),
                                    		response.getBitmap())
                            }
                    );
                    transitionDrawable.setCrossFadeEnabled(true);
                    view.setImageDrawable(transitionDrawable);
                    transitionDrawable.startTransition(100);

				} else if (defaultImageDrawable != null) {
					view.setImageDrawable(defaultImageDrawable);
				}
			}
		};
	}
	               

}
