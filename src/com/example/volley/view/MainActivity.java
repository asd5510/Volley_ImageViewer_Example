package com.example.volley.view;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;




import com.example.volley.R;
import com.example.volley.data.MyDatabaseHelper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    public static final String[] TITELS ={"first(read a page without save)" , "second(read database)", "third(read url)","forth"};
    public static final String FIRST_TAB_TAG = TITELS[0];
    public static final String SECOND_TAB_TAG = TITELS[1];
    public static final String THIRD_TAB_TAG = TITELS[2];
    public static final String FORTH_TAB_TAG = TITELS[3];
    private DrawerLayout mDrawer_layout;
    private RelativeLayout mMenu_layout_left;
    private RelativeLayout mMenu_layout_right;
    private TabManager tabManager;
    private ConcreteListFragment concreteListFragment;
    private FragmentManager fragmentManager;
    @SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar().setLogo(R.drawable.sing);
        concreteListFragment = new ConcreteListFragment();
		
		// We replace fragment in order to avoid items of ListView in ListFragment to overlap over themselves.
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
				.replace(R.id.fragment_layout, concreteListFragment)
				.commit();
        mDrawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mMenu_layout_left = (RelativeLayout) findViewById(R.id.menu_layout_left);
        mMenu_layout_right = (RelativeLayout) findViewById(R.id.menu_layout_right);
        ListView menu_listview_l = (ListView) mMenu_layout_left.findViewById(R.id.menu_listView_l);
        ListView menu_listview_r = (ListView) mMenu_layout_right.findViewById(R.id.menu_listView_r);
        menu_listview_l.setAdapter(new ArrayAdapter<String>(this,android.R.layout.
        simple_expandable_list_item_1,TITELS));
        menu_listview_r.setAdapter(new ArrayAdapter<String>(this,android.R.layout.
        simple_expandable_list_item_1,TITELS));

        menu_listview_l.setOnItemClickListener(new DrawerItemClickListenerLeft());
        menu_listview_r.setOnItemClickListener(new DrawerItemClickListenerRight());
        
        tabManager = new TabManager(getActionBar());
		tabManager.addTab("Json",
				FIRST_TAB_TAG, this);
		tabManager.addTab("壁纸",
				SECOND_TAB_TAG, this);
		tabManager.addTab("壁纸&数据库",THIRD_TAB_TAG,this);
		tabManager.addTab("url",FORTH_TAB_TAG,this);
    }


    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu. This adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.github:
			// When you press GitHub icon in the ActionBar
			Uri uriUrl = Uri.parse(
					"https://github.com/asd5510");
			Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
			startActivity(launchBrowser);
			return true;
		case R.id.about:
			final EditText et=new EditText(this);
			// When you press information icon in the ActionBar
			Builder builder=new AlertDialog.Builder(this);
		    builder.setTitle("请输入Url");
		    builder.setIcon(android.R.drawable.ic_dialog_info);
		    builder.setView(et);
		    builder.setPositiveButton("确定",new OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					String url = et.getText().toString();
				}
		    	 
		    });
		    builder.setNegativeButton("取消", null);
		    AlertDialog dialog=builder.create();
		    dialog.show();
			break;
		case R.id.contact:
			// When you press email icon in the ActionBar
			
			break;
		}
		return super.onOptionsItemSelected(item);
	}


	public class DrawerItemClickListenerLeft implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment fragment = null;

            switch (position) {
                case 0:
                    fragment = new FirstFragment();
                    break;
                case 1:
                    fragment = new SecondFragment();
//                    ft.detach(concreteListFragment);
                    break;
                case 2:
                	fragment = new ThirdFragment();
                	break;
                default:
                	fragment = new PicsFragment();
                    break;
            }
            ft.replace(R.id.fragment_layout, fragment);
            ft.commit();
            mDrawer_layout.closeDrawer(mMenu_layout_left);
        }
    }
    public class DrawerItemClickListenerRight implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment fragment = null;
            switch (position) {
                case 0:
                   fragment = new FirstFragment();
                    break;
                case 1:
                    fragment = new SecondFragment();
                    break;
                case 2:
                	fragment = new ThirdFragment();
                	break;
                default:
                	fragment = new PicsFragment();
                    break;

            }
            ft.replace(R.id.fragment_layout, fragment);
            ft.commit();
            mDrawer_layout.closeDrawer(mMenu_layout_right);
        }
    }
	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}


	@Override
	public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

		if(arg0.getTag() == SECOND_TAB_TAG) {
	        ft.replace(R.id.fragment_layout, new FirstFragment());
	        
		} else if(arg0.getTag() == THIRD_TAB_TAG) {
	        ft.replace(R.id.fragment_layout, new PicsFragment());
		} else if(arg0.getTag() == FORTH_TAB_TAG) {
			ft.replace(R.id.fragment_layout, new ThirdFragment());
		} else{
	        ft.replace(R.id.fragment_layout, new ConcreteListFragment());
		}
		ft.commit();
	}


	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}

}