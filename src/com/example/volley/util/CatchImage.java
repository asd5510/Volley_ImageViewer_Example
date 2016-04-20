package com.example.volley.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.volley.data.DataProvider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.os.AsyncTask;

public class CatchImage {
	// ��ַ

	public static final String URL = "http://desk.zol.com.cn/1920x1080";
	// ����
	private static final String ECODING = "UTF-8";
	// ��ȡimg��ǩ����
	private static final String IMGURL_REG = "<img.*src=(.*?)[^>]*?>";
	private static final String PRODUCT_REG = "href=\"/product/\\d{6}\"";
	// ��ȡsrc·��������
	private static final String IMGSRC_REG = "http:\"?(.*?)(\"|>|\\s+)";
	
	private static ContentResolver contentResolver = App.getContext().getContentResolver();
	
	public static boolean isLoading = false;
	/*
	 * public static void main(String[] args) throws Exception { CatchImage cm =
	 * new CatchImage(); //���html�ı����� String HTML = cm.getHTML(URL); //��ȡͼƬ��ǩ
	 * List<String> imgUrl = cm.getImageUrl(HTML); //��ȡͼƬsrc��ַ List<String>
	 * imgSrc = cm.getImageSrc(imgUrl); //����ͼƬ cm.Download(imgSrc); }
	 */

	/***
	 * ��ȡHTML����
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static String getHTML(String url) throws Exception {
		URL uri = new URL(url);
		URLConnection connection = uri.openConnection();
		InputStream in = connection.getInputStream();
		byte[] buf = new byte[1024];
		int length = 0;
		StringBuffer sb = new StringBuffer();
		while ((length = in.read(buf, 0, buf.length)) > 0) {
			sb.append(new String(buf, ECODING));
		}
		in.close();
		return sb.toString();
	}

	/***
	 * ��ȡImageUrl��ַ
	 * 
	 * @param HTML
	 * @return
	 */
	public static List<String> getImageUrl(String HTML) {
		Matcher matcher = Pattern.compile(IMGURL_REG).matcher(HTML);
		List<String> listImgUrl = new ArrayList<String>();
		while (matcher.find()) {
			listImgUrl.add(matcher.group());
		}
		return listImgUrl;
	}
	

	/***
	 * ��ȡImageSrc��ַ
	 * 
	 * @param listImageUrl
	 * @return
	 */
	public static List<String> getImageSrc(List<String> listImageUrl) {
		List<String> listImgSrc = new ArrayList<String>();
		for (String image : listImageUrl) {
			Matcher matcher = Pattern.compile(IMGSRC_REG).matcher(image);
			while (matcher.find()) {
				listImgSrc.add(matcher.group().substring(0,
						matcher.group().length() - 1));
			}
		}
		return listImgSrc;
	}

	/***
	 * ����ͼƬ
	 * 
	 * @param listImgSrc
	 */
	private void Download(List<String> listImgSrc) {
		try {
			for (String url : listImgSrc) {
				String imageName = url.substring(url.lastIndexOf("/") + 1,
						url.length());
				URL uri = new URL(url);
				InputStream in = uri.openStream();
				FileOutputStream fo = new FileOutputStream(new File("d:/logs/"
						+ imageName));
				byte[] buf = new byte[1024];
				int length = 0;
				System.out.println("��ʼ����:" + url);
				while ((length = in.read(buf, 0, buf.length)) != -1) {
					fo.write(buf, 0, length);
				}
				in.close();
				fo.close();
				System.out.println(imageName + "�������");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("����ʧ��");
		}
	}

	public static class DownloadImgUrl extends AsyncTask<String, String, List<String>> {

		@Override
		protected List<String> doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			List<String> imgSrc = new ArrayList<String>();
			for (String arg : arg0) {
				String HTML = null;
				try {
					HTML = getHTML(arg);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// ��ȡͼƬ��ǩ
				List<String> imgUrl = getImageUrl(HTML);
				// ��ȡͼƬsrc��ַ
				imgSrc.addAll(getImageSrc(imgUrl));
			}

			return imgSrc;
		}


		@Override
		protected void onPostExecute(List<String> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			for (String imgUrl : result) {
//				String imgUrl1 = imgUrl.split(".jp")[0] + "-1.jpg";
				ContentValues contentValues1 = new ContentValues();
				contentValues1.put("url", imgUrl);
				contentResolver.insert(DataProvider.PIC_CONTENT_URI, contentValues1);
				isLoading = false;
			}
		}
	}

}
