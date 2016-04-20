package com.example.volley.view;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.transform.ErrorListener;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.example.volley.R;
import com.example.volley.data.DataProvider;
import com.example.volley.data.MyDatabaseHelper;
import com.example.volley.util.CatchImage;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.AsyncTask;
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
 * 该类包含一个异步Task用于抓取图片Url,一个SimpleAdapter用于显示抓取的图片,并存入数据库,已弃用
 * Created by rqf on 2014/11/29.
 */
public class FirstFragment extends ListFragment {
	
    private ImageView imageView1;
    private RequestQueue mQueue;
    private ImageLoader mImageLoader;
    private NetworkImageView imageView2;
    private SimpleAdapter adapter;
    private ArrayList<HashMap<String, Object>> listItemImage;
    private CatchImage cm = new CatchImage();
    private MyDatabaseHelper dbHelper;
    private ContentResolver contentResolver;
     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.fisrt, container, false);
        DownloadImgUrl imgUrl = new DownloadImgUrl();

        imgUrl.execute(/*cm.URL,*/
//        		"http://www.3jy.com/egao/2.html"
        		"http://desk.zol.com.cn/dongman/"
        		);

    	return view;
    }
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
        
		// 创建MyDatabaseHelper对象，指定数据库版本为1，此处使用相对路径即可，
		// 数据库文件自动会保存在程序的数据文件夹的databases目录下。
//		dbHelper = new MyDatabaseHelper(getActivity(), "myPic.db3", 1);
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
             /*   
                Cursor cursor = contentResolver.query(DataProvider.PIC_CONTENT_URI, new String[]{"url"},"url=?", new String[] { imageUrl }, null);   					
                int count =	cursor.getCount();
                if(count > 0) {
                	
                } else {
                	ContentValues values = new ContentValues();
                	values.put("url",imageUrl);
                	contentResolver.insert(DataProvider.PIC_CONTENT_URI,values);
                }*/
            }
        },640, 320, null, null);
        mQueue.add(imageRequest);
        mQueue.start();
    }

    public void loadImageByImageLoader(){
        // imageView是一个ImageView实例
        // ImageLoader.getImageListener的第二个参数是默认的图片resource id
        // 第三个参数是请求失败时候的资源id，可以指定为0
        ImageListener imageListener1 = ImageLoader.getImageListener(imageView1, android.R.drawable.ic_menu_rotate, android.R.drawable.ic_delete);
        mImageLoader.get("https://images2.alphacoders.com/775/thumb-350-77572.jpg", imageListener1);

    }
    
    private void loadImageByNetworkImageView(){
//    	imageView2.setTag("url");
    	imageView2.setImageUrl("https://images2.alphacoders.com/775/thumb-350-77572.jpg",mImageLoader);
    	imageView2.setDefaultImageResId(android.R.drawable.ic_menu_rotate);
    	imageView2.setErrorImageResId(android.R.drawable.ic_delete);
    }
    
    
    @Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		HashMap<String, Object> object = (HashMap<String, Object>) adapter.getItem(position);
		String url = (String) object.get("ItemURL");
		System.out.println(url);
		Intent intent = new Intent(getActivity(),
				ImageViewActivity.class);
		intent.putExtra("image_url", url);
		startActivity(intent);
		
	}

	public class DownloadImgUrl extends AsyncTask<String, String,List<String>> {

		@Override
		protected List<String> doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			List<String> imgSrc = new ArrayList<String>();
			for(String arg : arg0) {
				String HTML = null;
				try {
					HTML = cm.getHTML(arg);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
			        //获取图片标签  
		        List<String> imgUrl = cm.getImageUrl(HTML);  
		        //获取图片src地址  
		        imgSrc.addAll(cm.getImageSrc(imgUrl));  
			}
			
			return imgSrc;
		}

		@Override
		protected void onPostExecute(List<String> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			for(String img : result) {
				// String[] index = img.split(".jp");
				// img = index[0] + "-1.jpg";
	        	loadImage(img);
	        }
		}
      }
}

