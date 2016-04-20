package com.example.volley.view;


import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.volley.R;
//import com.example.volley.MainActivity.NewsModel;
//import com.example.volley.MainActivity.VolleyAdapter.ViewHolder;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class ConcreteListFragment extends ListFragment {
	private ArrayList<NewsModel> arrNews;
	private VolleyAdapter va;
	private LayoutInflater lf;
    private RequestQueue mRequestQueue;   
    private String TAG = this.getClass().getSimpleName();
    private ProgressDialog pd;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
       
	}

	//onActivityCreated方法：当Activity中的onCreate方法执行完后调用。
	//所以一点击third，立即就显示出来了，因为在Activity创建的时候就进行了JSON请求了。
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
/*		Bundle arguments = getArguments();

		if (arguments != null) {
			updateListFragment(arguments
					.getInt(Constants.INIT_CONCRETE_LIST_FRAGMENT_TAG));
		}*/
		lf = LayoutInflater.from(getActivity());
        mRequestQueue =  Volley.newRequestQueue(getActivity());
        arrNews = new ArrayList<NewsModel>();
		String url = "";
//        pd = ProgressDialog.show(getActivity(),"Please Wait...","Please Wait...");
	    JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET,url,null,new Response.Listener<JSONObject>() {
	        @Override
	        public void onResponse(JSONObject response) {
	            Log.i(TAG,response.toString());
	            parseJSON(response);
	            va.notifyDataSetChanged();
	            pd.dismiss();
	        	}
		    },new Response.ErrorListener() {
		        @Override
		        public void onErrorResponse(VolleyError error) {
		            Log.i(TAG,error.getMessage());
		        }
		});
	    mRequestQueue.add(jr); 
		va = new VolleyAdapter();
		setListAdapter(va);
		
	}

    class VolleyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return arrNews.size();
        }

        @Override
        public Object getItem(int i) {
            return arrNews.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder vh ;
           if(view == null){
               vh = new ViewHolder();
               view = lf.inflate(R.layout.row_listview,null);
               vh.tvTitle = (TextView) view.findViewById(R.id.txtTitle);
               vh.tvDesc = (TextView) view.findViewById(R.id.txtDesc);
               vh.tvDate = (TextView) view.findViewById(R.id.txtDate);
               vh.tvTimezone = (TextView) view.findViewById(R.id.txtTimezone);
               view.setTag(vh);
          }
            else{
               vh = (ViewHolder) view.getTag();
           }

            NewsModel nm = arrNews.get(i);
            vh.tvTitle.setText(nm.getTitle());
            vh.tvDesc.setText(nm.getDescription());
            vh.tvDate.setText(nm.getPubDate());
            vh.tvTimezone.setText(nm.getTimezone());
            return view;
        }

         class  ViewHolder{
        	 TextView tvTitle;
             TextView tvDesc;
             TextView tvDate;
             TextView tvTimezone;

        }

    }
      

	private void parseJSON(JSONObject json){
	    try{
	        JSONObject value = json.getJSONObject("value");
	        JSONArray items = value.getJSONArray("items");
	        for(int i=0;i<items.length();i++){
	
	                JSONObject item = items.getJSONObject(i);
	                NewsModel nm = new NewsModel();
	                nm.setTitle(item.optString("title"));
	                nm.setDescription(item.optString("description"));
	                nm.setLink(item.optString("link"));
	                nm.setPubDate(item.optString("pubDate"));
	                nm.setTimezone(item.optString("y:published"));
	                arrNews.add(nm);
	        }
	    }
	    catch(Exception e){
	        e.printStackTrace();
	    }
	}


	class NewsModel{
	    private String title;
	    private String link;
	    private String description;
	    private String pubDate;
	    private String timezone;
	    void setTitle(String title) {
	        this.title = title;
	    }
	
	    void setLink(String link) {
	        this.link = link;
	    }
	
	    void setDescription(String description) {
	        this.description = description;
	    }
	
	    void setPubDate(String pubDate) {
	        this.pubDate = pubDate;
	    }
	
	    String getLink() {
	        return link;
	    }
	
	    String getDescription() {
	        return description;
	    }
	
	    String getPubDate() {
	        return pubDate;
	    }
	
	    String getTitle() {
	
	        return title;
	    }
	
	    String getTimezone() {
			return timezone;
		}
	
		void setTimezone(String timezone) {
			this.timezone = timezone;
		}
	
	
	}
	
	
	
/*	public void updateListFragment(int resourceId) {
		// We are creating an array adapter to store the list of brands.
		ArrayAdapter<CharSequence> adapter = null;
		adapter = ArrayAdapter.createFromResource(context, resourceId,
				android.R.layout.simple_list_item_1);
		// We are setting the list adapter for the ListFragment.
		setListAdapter(adapter);
	}

	@Override
	public void onListItemClick(ListView list, View view, int position, long id) {
		super.onListItemClick(list, view, position, id);

		String item = (String) list.getItemAtPosition(position);
		String message = getString(R.string.dialog_list_item_click_message_part1)
				+ item
				+ Html.fromHtml(getString(R.string.dialog_list_item_click_message_part2));
		// When list item is clicked, we show a alert message for the user.
		DialogFragment dialogFragment = AlertDialogFragment.newInstance(
				R.drawable.ic_dialog_alert,
				getString(R.string.dialog_list_item_click_title), message,
				getString(R.string.dialog_list_item_click_button_text));
		dialogFragment
				.show(getFragmentManager(), Constants.DIALOG_FRAGMENT_TAG);
	}*/
}
