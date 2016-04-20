package com.example.volley.view;

import java.util.ArrayList;
import java.util.HashMap;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.example.volley.R;
import com.example.volley.data.DataProvider;
import com.example.volley.data.MyDatabaseHelper;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;

/**
 * ����ʹ��SimpleCursorAdapter,�ֶ���ȡ���ݿ�Cursor����ʾͼƬ,������
 * Created by rqf on 2014/11/29.
 */
public class SecondFragment extends ListFragment {
    private RequestQueue mQueue;
    private ImageLoader mImageLoader;
    private SimpleAdapter adapter;
    private ArrayList<HashMap<String, Object>> listItemImage;
    private MyDatabaseHelper dbHelper;
    private ContentResolver contentResolver;

    @Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		contentResolver = getActivity().getContentResolver();
		listItemImage = new ArrayList<HashMap<String, Object>>(); 

	    String[] from = {"ItemImage","ItemURL"};  
	    int[] to = {R.id.imageListView,0};  
	    adapter = new SimpleAdapter(getActivity(), listItemImage, R.layout.row_image, from, to);  
	    adapter.setViewBinder(new ViewBinder() {  
	    	@Override  
	    	public boolean setViewValue(View view, Object data,  
	    	        String textRepresentation) {  
	    	    // TODO Auto-generated method stub  
	    	    if(view instanceof ImageView && data instanceof Bitmap){  
	    	        ImageView i = (ImageView)view;  
	    	        i.setImageBitmap((Bitmap) data);  
	    	        return true;  
	    	    }  
	    	    return false;  
	    	}
	    });  
	    setListAdapter(adapter);  
		
		mQueue = Volley.newRequestQueue(getActivity());
		int cacheSize = 4 * 1024 * 1024; // 4MiB
        final LruCache<String, Bitmap> mImageCache = new LruCache<String, Bitmap>(cacheSize);
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
        
		// ����MyDatabaseHelper����ָ�����ݿ�汾Ϊ1���˴�ʹ�����·�����ɣ�
		// ���ݿ��ļ��Զ��ᱣ���ڳ���������ļ��е�databasesĿ¼�¡�
/*		dbHelper = new MyDatabaseHelper(getActivity(), "myPic.db3", 1);
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery("select * from picurl", null);
*/      
        Cursor cursor = contentResolver.query(DataProvider.PIC_CONTENT_URI, new String[]{"url"},null, null, null);
        while(cursor.moveToNext()) {
        	loadImage(cursor.getString(0));
        	System.out.println(cursor.getString(0));
        }
        cursor.close();
    }
    
    
    public void loadImage(final String imageUrl){
        ImageRequest imageRequest = new ImageRequest(imageUrl, new Response.Listener<Bitmap>() {

            @Override
            public void onResponse(Bitmap bitmap) {
//                imageView.setImageBitmap(bitmap);
                HashMap map = new HashMap<String, Object>();
                map.put("ItemImage", bitmap);
                map.put("ItemURL",imageUrl);
                listItemImage.add(map);
                adapter.notifyDataSetChanged();             
            }
        },640, 320, null, null);
        mQueue.add(imageRequest);
        mQueue.start();
    }

    @Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		HashMap<String, Object> object = (HashMap<String, Object>) adapter.getItem(position);
		String url = (String) object.get("ItemURL");	
/*		Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
					"select * from picurl where url is ? ",
					new String[] { url });*/
		contentResolver.delete(DataProvider.PIC_CONTENT_URI, "url=?", new String[]{url});
/*		String strSQL = "DELETE FROM picurl WHERE url = '"+ url +"'";

		dbHelper.getReadableDatabase().execSQL(strSQL);;*/
	}
}
