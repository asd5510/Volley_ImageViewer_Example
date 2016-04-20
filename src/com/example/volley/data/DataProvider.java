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
	// ��һ�δ�����ContentProviderʱ���ø÷���
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

	// �÷����ķ���ֵ�����˸�ContentProvider���ṩ���ݵ�MIME����
	@Override
	public String getType(Uri uri) {
		System.out.println("~~getType����������~~");
		return null;
	}

	// ʵ�ֲ�ѯ�������÷���Ӧ�÷��ز�ѯ�õ���Cursor
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

	// ʵ�ֲ���ķ������÷���Ӧ���²���ļ�¼��Uri
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

	// ʵ��ɾ���������÷���Ӧ�÷��ر�ɾ���ļ�¼����
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

	// ʵ��ɾ���������÷���Ӧ�÷��ر����µļ�¼����
	@Override
	public int update(Uri uri, ContentValues values, String where,
			String[] whereArgs) {
		System.out.println(uri + "===update����������===");
		System.out.println("where����Ϊ��" + where + ",values����Ϊ��" + values);
		return 0;
	}

	public static ArrayList<Map<String, String>> converCursorToList(
			Cursor cursor) {
		ArrayList<Map<String, String>> result = new ArrayList<Map<String, String>>();
		// ����Cursor�����
		while (cursor.moveToNext()) {
			// ��������е����ݴ���ArrayList��
			Map<String, String> map = new HashMap<String, String>();
			// ȡ����ѯ��¼�е�2�С���3�е�ֵ
			map.put("url", cursor.getString(1));
			result.add(map);
		}
		return result;
	}
}