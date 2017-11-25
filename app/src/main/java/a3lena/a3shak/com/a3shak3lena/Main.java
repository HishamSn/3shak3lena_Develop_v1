package a3lena.a3shak.com.a3shak3lena;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import a3lena.a3shak.com.a3shak3lena.Dealers.DealerDetails;
import a3lena.a3shak.com.a3shak3lena.Dealers.DealersMain;
import a3lena.a3shak.com.a3shak3lena.Map.MapDealers;
import a3lena.a3shak.com.a3shak3lena.slide_menu.BaseSlideMenuActivity;
import a3lena.a3shak.com.a3shak3lena.util.Constatns;
import a3lena.a3shak.com.a3shak3lena.util.MySingleton;
import a3lena.a3shak.com.a3shak3lena.util.util;
import a3lena.a3shak.com.slidemenu.SlideMenu;
import a3lena.a3shak.com.slider_image_lib.banners.Banner;
import a3lena.a3shak.com.slider_image_lib.banners.RemoteBanner;
import a3lena.a3shak.com.slider_image_lib.views.BannerSlider;
import pl.droidsonroids.gif.GifImageView;

public class Main extends BaseSlideMenuActivity {
    // Slider
    private SlideMenu mSlideMenu;
    ImageView btnMenu, imgExchange, imgPlace, imgMap;

    TextView txtTaken, txtGiven, txtMap;
    private BannerSlider bannerSlider;

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        setSlideRole(R.layout.activity_main2);
        setSlideRole(R.layout.layout_secondary_menu);
        mSlideMenu = getSlideMenu();
        // Slider Toggle
        click_menu();
        init();
        changeFont();
        setupBannerSlider();
        intent_screen();
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("a3lena.a3shak.com.a3shak3lena", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mSlideMenu.isOpen()) {
            mSlideMenu.close(true);
            mSlideMenu.setSlideMode(SlideMenu.MODE_SLIDE_CONTENT);
        }
    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//
//    }

    public void init(){
        txtGiven = (TextView) findViewById(R.id.txtGiven);
        txtTaken = (TextView) findViewById(R.id.txtTaken);
        txtMap = (TextView) findViewById(R.id.txtMap);

        imgExchange = (ImageView) findViewById(R.id.exchangeCoupon);
        imgMap = (ImageView) findViewById(R.id.mapCoupon);
        imgPlace = (ImageView) findViewById(R.id.placesCoupon);
    }

    public void changeFont(){
        txtGiven.setTypeface(util.changeFont(this));
        txtTaken.setTypeface(util.changeFont(this));
        txtMap.setTypeface(util.changeFont(this));
    }

    public void intent_screen(){

        imgExchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(util.haveNetworkConnection(Main.this)){
                    Intent i = new Intent(Main.this, DealersMain.class);
                    i.putExtra("numTab", 1);
                    startActivity(i);
                }else{
                    util.dialogErrorInternet(Main.this);
                }

            }
        });
        imgPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(util.haveNetworkConnection(Main.this)) {
                    Intent i = new Intent(Main.this, DealersMain.class);
                    i.putExtra("numTab", 0);
                    startActivity(i);
                }else{
                    util.dialogErrorInternet(Main.this);
                }
            }
        });
        imgMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(util.haveNetworkConnection(Main.this)) {
                    Intent i = new Intent(Main.this, MapDealers.class);
                    startActivity(i);
                }else{
                    util.dialogErrorInternet(Main.this);
                }
            }
        });
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
                    mSlideMenu.setSlideMode(SlideMenu.MODE_SLIDE_CONTENT );
                }
            }
        });
    }
//    http://mkh-jo.com/coupons/files/images/1507393857.png
    private void setupBannerSlider(){
        bannerSlider = (BannerSlider) findViewById(R.id.banner_slider1);
        getAdsImage();
//        bannerSlider.setOnBannerClickListener(new OnBannerClickListener() {
//            @Override
//            public void onClick(int position) {
//                Toast.makeText(Main.this, "Banner with position " + String.valueOf(position) + " clicked!", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    public void getAdsImage(){
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, Constatns.MAIN_API + Constatns.ADS,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        List<Banner> remoteBanners=new ArrayList<>();
                        try {
                            JSONArray obj1Json = response.getJSONArray("aaData");
                            for (int i = 0; i < obj1Json.length(); i++) {
                                try {
                                    JSONObject jsonObject = obj1Json.getJSONObject(i);
                                    Log.e("nono", Constatns.imageLink +jsonObject.getString("image"));
                                    String image = Constatns.imageLink+jsonObject.getString("image");
                                    String timer = jsonObject.getString("timer");

                                    remoteBanners.add(new RemoteBanner(image));

                                    bannerSlider.setBanners(remoteBanners);
                                    bannerSlider.setInterval(Integer.valueOf(timer) * 1000);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.err.println(error.getMessage());
            }
        });
        MySingleton.getInstance(Main.this).addToRequestQueue(stringRequest);
    }



}
