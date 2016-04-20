package com.example.volley.view;


import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.volley.R;
import com.example.volley.data.DataProvider;

public class ThirdFragment extends ListFragment implements
		LoaderManager.LoaderCallbacks<Cursor>, OnScrollListener {

	private MyCursorAdapter adapter;
	private Cursor cursor;
	private ContentResolver contentResolver;
	private int position;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		contentResolver = getActivity().getContentResolver();
		// 创建MyDatabaseHelper对象，指定数据库版本为1，此处使用相对路径即可，
		// 数据库文件自动会保存在程序的数据文件夹的databases目录下。
		// dbHelper = new MyDatabaseHelper(getActivity(), "myPic.db3", 1);
		// cursor =
		// dbHelper.getReadableDatabase().rawQuery("select * from picurl",
		// null);
		cursor = contentResolver.query(DataProvider.PIC_CONTENT_URI,
				new String[] { "_id", "url" }, null, null, null);
		String[] from = { "url" };
		int[] to = { R.id.txtTitle };

		// adapter = new SimpleAdapter(getActivity(), listItemImage,
		// R.layout.row_image, from, to);
		// adapter = new SimpleCursorAdapter(getActivity(),
		// R.layout.row_listview, cursor, from, to);
		adapter = new MyCursorAdapter(getActivity(), R.layout.row_listview,
				null);
		setListAdapter(adapter);
		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Cursor cursor = (Cursor) adapter.getItem(position);
		String url = (String) cursor.getString(1);
		/*
		 * Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
		 * "select * from picurl where url is ? ", new String[] { url });
		 */
		// contentResolver.delete(DataProvider.PIC_CONTENT_URI, "url = ?", new
		// String[]{url});
		/*
		 * ContentValues contentValues = new ContentValues();
		 * contentValues.put("url", url);
		 * contentResolver.insert(DataProvider.PIC_CONTENT_URI,contentValues);
		 */
		Intent intent = new Intent(getActivity(), ImageViewActivity.class);
		intent.putExtra("image_url", url);
		startActivity(intent);
	}

	class MyCursorAdapter extends CursorAdapter {
		Context context = null;
		int viewResId;

		public MyCursorAdapter(Context context, int resource, Cursor cursor) {
			super(context, cursor);
			viewResId = resource;
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {

			TextView view = null;
			LayoutInflater vi = null;
			vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			return vi.inflate(viewResId, parent, false);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			Log.i("hubin", "bind" + view);
			TextView nameView = (TextView) view.findViewById(R.id.txtTitle);
			// Set the name
			nameView.setText(cursor.getString(cursor.getColumnIndex("url")));
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		return new CursorLoader(getActivity(), DataProvider.PIC_CONTENT_URI,
				new String[] { "_id", "url" }, null, null, null);

	}
	
	@Override
	public void onResume() {
	  getListView().setOnScrollListener(this);
	  getListView().setSelection(position);
	  super.onResume() ; 
	}
	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		// TODO Auto-generated method stub
		adapter.swapCursor(arg1);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		adapter.swapCursor(null);
	}

	@Override
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int scrollState) {
		// TODO Auto-generated method stub
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
			position = getListView().getFirstVisiblePosition();
		}
	}
}
