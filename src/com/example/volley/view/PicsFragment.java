package com.example.volley.view;

import com.example.volley.data.DataProvider;
import com.example.volley.util.CatchImage;
import com.example.volley.view.FirstFragment.DownloadImgUrl;
import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;

import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public class PicsFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>,OnScrollListener {

	private PicsAdapter picsAdapter;
	private ContentResolver contentResolver;
	private SharedPreferences sharedPreferences;
	private int pageNum = 1;
	private int position;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		picsAdapter = new PicsAdapter(getActivity(), null);
		setListAdapter(picsAdapter);
		contentResolver = getActivity().getContentResolver();
		getLoaderManager().initLoader(0, null, this); 
		sharedPreferences = getActivity().getSharedPreferences("volley", getActivity().MODE_WORLD_READABLE);
//		pageNum = sharedPreferences.getInt("page", 1);
		contentResolver.delete(DataProvider.PIC_CONTENT_URI,null,null);
	}


	@Override
	public void onResume() {
	  //onResume happens after onStart and onActivityCreate
		AnimationAdapter animationAdapter = new CardsAnimationAdapter(picsAdapter);
		animationAdapter.setAbsListView(getListView());
		getListView().setAdapter(animationAdapter);
		getListView().setOnScrollListener(this);
		getListView().setSelection(position);
	  super.onResume() ; 
	}


	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		return new CursorLoader(getActivity(), DataProvider.PIC_CONTENT_URI,  
                new String[]{"_id","url"}, null, null, null);

	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		// TODO Auto-generated method stub
		picsAdapter.swapCursor(arg1);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		picsAdapter.swapCursor(null);
	}

	
    @Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Cursor cursor = (Cursor) picsAdapter.getItem(position);
		String url = (String) cursor.getString(1);	
		Intent intent = new Intent(getActivity(),
				ImageViewActivity.class);
		intent.putExtra("image_url", url);
		startActivity(intent);
//		contentResolver.delete(DataProvider.PIC_CONTENT_URI, "url = ?", new String[]{url});
   }


	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		if(firstVisibleItem + visibleItemCount >= totalItemCount  && CatchImage.isLoading == false
				&& pageNum < 10){
			CatchImage.isLoading = true;
	        CatchImage.DownloadImgUrl imgUrl = new CatchImage.DownloadImgUrl();
	        pageNum++;
	  /*      SharedPreferences.Editor editor = sharedPreferences.edit();
	        editor.putInt("page", pageNum);
	        editor.commit();*/
	        imgUrl.execute(/*cm.URL,*/
	        		"http://desk.zol.com.cn/1920x1080/"+pageNum+".html"
	        		);
		}
	}


	@Override
	public void onScrollStateChanged(AbsListView arg0, int scrollState) {
		// TODO Auto-generated method stub
		 if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {  
			 position = getListView().getFirstVisiblePosition();  
		 }      
	}
}
