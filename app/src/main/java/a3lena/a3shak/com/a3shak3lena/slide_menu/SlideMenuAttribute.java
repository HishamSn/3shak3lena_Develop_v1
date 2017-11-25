package a3lena.a3shak.com.a3shak3lena.slide_menu;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import a3lena.a3shak.com.a3shak3lena.R;
import a3lena.a3shak.com.slidemenu.SlideMenu;


public class SlideMenuAttribute extends BaseSlideMenuActivity {

	// Slider
	private SlideMenu mSlideMenu;
	ImageView btnMenu;

	Toolbar toolbar;

	@Override
	public void onContentChanged() {
		super.onContentChanged();
//		toolbar = (Toolbar) findViewById(R.id.toolbar);
//		setSupportActionBar(toolbar);
		setSlideRole(R.layout.layout_slidemenu_attribute);
		setSlideRole(R.layout.layout_secondary_menu);
		mSlideMenu = getSlideMenu();
		// Slider Toggle
		click_menu();



	}

	public void click_menu(){
		btnMenu = (ImageView) findViewById(R.id.btnMenu);
		btnMenu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(mSlideMenu.isOpen()){
					mSlideMenu.close(true);
					mSlideMenu.setSlideMode(SlideMenu.MODE_SLIDE_CONTENT);
				}else{
					mSlideMenu.open(true, true);
					mSlideMenu.setSlideMode(SlideMenu.MODE_SLIDE_CONTENT);
				}
			}
		});
	}



}
