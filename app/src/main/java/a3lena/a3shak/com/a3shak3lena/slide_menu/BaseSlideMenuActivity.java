package a3lena.a3shak.com.a3shak3lena.slide_menu;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import a3lena.a3shak.com.a3shak3lena.R;
import a3lena.a3shak.com.slidemenu.SlideMenu;


public class BaseSlideMenuActivity extends AppCompatActivity {
	private SlideMenu mSlideMenu;
	private Fragment fragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_slidemenu);
	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		mSlideMenu = (SlideMenu) findViewById(R.id.slideMenu);

	}

	public void setSlideRole(int res) {
		if (null == mSlideMenu) {
			return;
		}
		getLayoutInflater().inflate(res, mSlideMenu, true);
	}

	public SlideMenu getSlideMenu() {
		return mSlideMenu;
	}
}
