/**
 *
 */
package com.example.volley.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class DataProvider extends ContentProvider {
	// 第一次创建该ContentProvider时调用该方法
	public static final String AUTHORITY = "com.example.volley.provider";
	public static final String SCHEME = "content://";
	public static final String PATH_PICS = "/pics";
	public static final int PICS = 0;
	public final static Uri PIC_CONTENT_URI = Uri
				.parse("content://"	+ DataProvider.AUTHORITY + DataProvider.PATH_PICS);

	private static UriMatcher matcher = 
			new UriMatcher(UriMatcher.NO_MATCH);
	private MyDatabaseHelper myDatabaseHelperHelper;
	
	static {
		matcher.addURI(AUTHORITY, "pics", PICS);
	}

	public void getMyDatabaseHelper() {
		if(myDatabaseHelperHelper == null) {
			myDatabaseHelperHelper = new MyDatabaseHelper(getContext(),"myPic.db3", 1);
		}
//		return myDatabaseHelperHelper;
	}
	@Override
	public boolean onCreate() {
		getMyDatabaseHelper();
		return true;
	}

	// 该方法的返回值代表了该ContentProvider所提供数据的MIME类型
	@Override
	public String getType(Uri uri) {
		System.out.println("~~getType方法被调用~~");
		return null;
	}

	// 实现查询方法，该方法应该返回查询得到的Cursor
	@Override
	public Cursor query(Uri uri, String[] projection, String where,
			String[] whereArgs, String sortOrder) {
		SQLiteDatabase db = myDatabaseHelperHelper.getReadableDatabase();
		switch(matcher.match(uri)) {
		case PICS:
			Cursor cursor = db.query("picurl", projection, where, whereArgs, null, null, sortOrder);
			cursor.setNotificationUri(getContext().getContentResolver(), uri);
			return cursor;
		}
		return null;
	}

	// 实现插入的方法，该方法应该新插入的记录的Uri
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = myDatabaseHelperHelper.getReadableDatabase();
		switch(matcher.match(uri)) {
		case PICS:
			long rowId = db.insert("picurl", "_id", values);
			if(rowId > 0) {
				Uri picuri = ContentUris.withAppendedId(uri, rowId);
				getContext().getContentResolver().notifyChange(picuri, null);
				return picuri;
			}
		}
		return null;
	}

	// 实现删除方法，该方法应该返回被删除的记录条数
	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		SQLiteDatabase db = myDatabaseHelperHelper.getReadableDatabase();
		int count = 0;
		switch(matcher.match(uri)) {
			case PICS:
				count = db.delete("picurl", where, whereArgs);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	// 实现删除方法，该方法应该返回被更新的记录条数
	@Override
	public int update(Uri uri, ContentValues values, String where,
			String[] whereArgs) {
		System.out.println(uri + "===update方法被调用===");
		System.out.println("where参数为：" + where + ",values参数为：" + values);
		return 0;
	}

	public static ArrayList<Map<String, String>> converCursorToList(
			Cursor cursor) {
		ArrayList<Map<String, String>> result = new ArrayList<Map<String, String>>();
		// 遍历Cursor结果集
		while (cursor.moveToNext()) {
			// 将结果集中的数据存入ArrayList中
			Map<String, String> map = new HashMap<String, String>();
			// 取出查询记录中第2列、第3列的值
			map.put("url", cursor.getString(1));
			result.add(map);
		}
		return result;
	}
}