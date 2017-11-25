package a3lena.a3shak.com.a3shak3lena.Dealers;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import a3lena.a3shak.com.a3shak3lena.Dealers.TabDealership.DistCoupons;
import a3lena.a3shak.com.a3shak3lena.Dealers.TabDealership.ExchangeCoupons;
import a3lena.a3shak.com.a3shak3lena.R;
import a3lena.a3shak.com.a3shak3lena.slide_menu.BaseListFragment;
import a3lena.a3shak.com.a3shak3lena.slide_menu.BaseSlideMenuActivity;
import a3lena.a3shak.com.a3shak3lena.util.util;
import a3lena.a3shak.com.slidemenu.SlideMenu;
import pl.droidsonroids.gif.GifImageView;

public class DealersMain extends BaseSlideMenuActivity {

    // Slider
    private SlideMenu mSlideMenu;
    ImageView btnMenu;
    int numTab;

    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private NonSwipeableViewPager mViewPager;
    TabLayout tabLayout;
    Activity activity = DealersMain.this;
    Bundle bundle;
    Handler handler = null;
    Runnable r;
    Handler handlerTab;
    Runnable runnableTab;

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        setSlideRole(R.layout.activity_dealers_main);
        setSlideRole(R.layout.layout_secondary_menu);
        mSlideMenu = getSlideMenu();

            // Slider Toggle
            click_menu();
            init();


//        final int numTab = bundle.getInt("numTab");
            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
            mViewPager = (NonSwipeableViewPager) findViewById(R.id.container);

            // Set up the ViewPager with the sections adapter.
            mViewPager.setAdapter(mSectionsPagerAdapter);

            tabLayout = (TabLayout) findViewById(R.id.tabs);

            tabLayout.setTabTextColors(ColorStateList.valueOf(getResources().getColor(R.color.white)));

            handlerTab = new Handler();
            runnableTab =  new Runnable(){
                @Override
                public void run() {
                    tabLayout.getTabAt(numTab).select();
                }
            };
            handlerTab.postDelayed(runnableTab, 40);

            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onTabSelected(TabLayout.Tab tab) {


                    mViewPager.setCurrentItem(tab.getPosition());


                    int selectedTabPosition = tab.getPosition();

                    View firstTab = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(0);
                    View secondTab = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(1);

                    if (selectedTabPosition == 0) { // that means first tab
                        firstTab.setBackground(getDrawable(R.drawable.shape_selected_ar));
                        secondTab.setBackground(getDrawable(R.drawable.shape_unselected_ar));
                    } else if (selectedTabPosition == 1) { // that means it's a last tab
                        firstTab.setBackground(getDrawable(R.drawable.shape_selected_alternate_ar));
                        secondTab.setBackground(getDrawable(R.drawable.shape_unselected_alternate_ar));
                    }
                }
                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
            tabLayout.setupWithViewPager(mViewPager);
            setupTabIcons();
    }

    public void init(){
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (NonSwipeableViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        bundle = getIntent().getExtras();
        numTab = bundle.getInt("numTab");
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


    private void setupTabIcons() {

        Typeface typeface = Typeface.createFromAsset(getAssets(), "HacenTunisia.ttf");
        final TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText(activity.getString(R.string.exchage));
        tabOne.setTypeface(typeface);

        final TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText(activity.getString(R.string.places));
        tabTwo.setTypeface(typeface);


        handler = new Handler();

        r = new Runnable() {
            public void run() {

                int selectedTabPosition = tabLayout.getSelectedTabPosition();
                if(selectedTabPosition == 0){
                    tabOne.setTextColor(ContextCompat.getColor(activity, R.color.white));
                    util.changeTitle(activity,"أماكن صرف الكوبون");
                    tabTwo.setTextColor(ContextCompat.getColor(activity, R.color.grey_dark));
                }else if(selectedTabPosition == 1){
                    tabOne.setTextColor(ContextCompat.getColor(activity, R.color.grey_dark));
                    util.changeTitle(activity,"أماكن توزيع الكوبون");
                    tabTwo.setTextColor(ContextCompat.getColor(activity, R.color.white));
                }

                handler.postDelayed(this, 40);
            }
        };

        handler.postDelayed(r, 500);

        tabLayout.getTabAt(0).setCustomView(tabOne);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handler.removeCallbacksAndMessages(null);
        handlerTab.removeCallbacksAndMessages(null);
        handlerTab.removeCallbacks(r);
        handlerTab.removeCallbacks(runnableTab);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(r,250);
        handlerTab.postDelayed(runnableTab,250);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        handler.postDelayed(r, 250);
        handlerTab.postDelayed(runnableTab,250);
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    ExchangeCoupons exchangeCoupons = new ExchangeCoupons();
                    return exchangeCoupons;
                case 1:
                    DistCoupons distCoupons = new DistCoupons();
                    return distCoupons;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "أماكن صرف الكوبون";
                case 1:
                    return "أماكن توزيع الكوبون";
            }
            return null;
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacksAndMessages(null);
        handlerTab.removeCallbacksAndMessages(null);
        handler.removeCallbacks(r);
        handlerTab.removeCallbacks(runnableTab);
        if(mSlideMenu.isOpen()) {
            mSlideMenu.close(true);
            mSlideMenu.setSlideMode(SlideMenu.MODE_SLIDE_CONTENT);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        handlerTab.removeCallbacksAndMessages(null);
        handler.removeCallbacks(r);
        handlerTab.removeCallbacks(runnableTab);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacksAndMessages(null);
        handlerTab.removeCallbacksAndMessages(null);
        handler.removeCallbacks(r);
        handlerTab.removeCallbacks(runnableTab);
    }

    public void selectTab(int i){
        tabLayout.getTabAt(i).select();
    }




}
