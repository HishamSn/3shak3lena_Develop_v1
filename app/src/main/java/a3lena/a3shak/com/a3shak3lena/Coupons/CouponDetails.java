package a3lena.a3shak.com.a3shak3lena.Coupons;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import a3lena.a3shak.com.a3shak3lena.Dealers.CustomSwipeAdapter;
import a3lena.a3shak.com.a3shak3lena.Dealers.InfinitePagerAdapter;
import a3lena.a3shak.com.a3shak3lena.R;
import a3lena.a3shak.com.a3shak3lena.slide_menu.BaseSlideMenuActivity;
import a3lena.a3shak.com.a3shak3lena.util.*;
import a3lena.a3shak.com.slidemenu.SlideMenu;
import daboubi.khalid.faisalawe.com.countdownlib.TickTockView;

public class CouponDetails extends BaseSlideMenuActivity {

    // Slider
    private SlideMenu mSlideMenu;
    ImageView btnMenu;

    Bundle bundle;
    String id, delaerID, barcode, startdate, enddate, points, msg, msg2, scanStartDate, scanEndDate, used;
    TextView txtStartDate, txtEndDate, txtPoints, txtNumCoupons, txtStatus, txtDay, txtHour, txtMinute, txtSecond;
    Button btnReActive;
    SharedPreferences sharedPreferences;
    ViewPager viewPager;
    PagerAdapter pagerAdapter;
    CustomSwipeAdapter adapter;
    ArrayList<String> sliderimage;
    Activity context=this;


    private TickTockView mCountDown = null;


    @Override
    public void onContentChanged() {
        super.onContentChanged();
        StartUp.getInstance().changeLanguage("ar");
        setSlideRole(R.layout.activity_coupon_details);
        setSlideRole(R.layout.layout_secondary_menu);
        mSlideMenu = getSlideMenu();
        click_menu();
        util.changeTitle(this,"كوبوناتي");
        init();
        changeFont();
//        countDownStart();
        sliderimage = new ArrayList<>();
        getAdsImage();
        getData();

    }


    public void countdown(String current, String future, String used, String msg){
        if (mCountDown != null) {

//            mCountDown.start(currentDate,futureDate);
            mCountDown.setOnTickListener(new TickTockView.OnTickListener() {
                @Override
                public String getText(long timeRemaining) {
//                    int seconds = (int) (timeRemaining / 1000) % 60;
//                    int minutes = (int) ((timeRemaining / (1000 * 60)) % 60);
//                    int hours = (int) ((timeRemaining / (1000 * 60 * 60)) % 24);
//                    int days = (int) (timeRemaining / (1000 * 60 * 60 * 24));

                    long days = timeRemaining / (24 * 60 * 60 * 1000);
                    timeRemaining -= days * (24 * 60 * 60 * 1000);
                    long hours = timeRemaining / (60 * 60 * 1000);
                    timeRemaining -= hours * (60 * 60 * 1000);
                    long minutes = timeRemaining / (60 * 1000);
                    timeRemaining -= minutes * (60 * 1000);
                    long seconds = timeRemaining / 1000;

                    txtDay.setText(String.valueOf(days)+"\r\n "+"يوم");
                    txtHour.setText(String.valueOf(hours)+"\r\n "+"ساعة");
                    txtMinute.setText(String.valueOf(minutes)+"\r\n "+"دقيقة");
                    txtSecond.setText(String.valueOf(seconds)+"\r\n "+"ثانية");

                    Log.e("kokko", String.valueOf(days+" -- "+hours + " -- "+ minutes + " -- "+ seconds));
                    boolean hasDays = days > 0;
                    Log.e("dsdsds", String.valueOf(seconds+" -- "+ days));
                    return String.format("%1$02d%4$s %2$02d%5$s",
                            hasDays ? days : hours,
                            hasDays ? hours : minutes,
                            hasDays ? minutes : seconds,
                            hasDays ? "d" : "h",
                            hasDays ? "h" : "m",
                            hasDays ? "m" : "s");
                }
            });

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date futureDate = dateFormat.parse(future);
                Date currentDate = dateFormat.parse(current);
//                                            Log.e("current date : ", String.valueOf(dateFormat.format(currentDate)));
//                                            Log.e("current date future: ", String.valueOf(futureDate));



                if(used.equals("0")){
                    if (!currentDate.after(futureDate)) {
                        mCountDown.start(currentDate , futureDate);
                        btnReActive.setVisibility(View.VISIBLE);
                    }else {
                        btnReActive.setVisibility(View.GONE);
                        txtMinute.setVisibility(View.GONE);
                        txtSecond.setVisibility(View.GONE);
                        txtHour.setVisibility(View.GONE);
                        txtDay.setText(msg);
                    }
                }else{
                    btnReActive.setVisibility(View.GONE);
                    txtMinute.setVisibility(View.GONE);
                    txtSecond.setVisibility(View.GONE);
                    txtHour.setVisibility(View.GONE);
                    txtDay.setText(msg);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public void init(){

        mCountDown = (TickTockView) findViewById(R.id.view_ticktock_countdown);

        bundle = getIntent().getExtras();
        barcode =bundle.getString("barcode");

        viewPager = (ViewPager) findViewById(R.id.ViewPagerScanCoupon);

        sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE);

        txtStartDate = (TextView) findViewById(R.id.date_created);
        txtEndDate = (TextView) findViewById(R.id.date_end);
        txtNumCoupons = (TextView) findViewById(R.id.number_coupon);
        txtPoints = (TextView) findViewById(R.id.points);
        txtStatus = (TextView) findViewById(R.id.coupon_status);
        btnReActive = (Button) findViewById(R.id.btnReActive);
        txtDay = (TextView) findViewById(R.id.day);
        txtHour = (TextView) findViewById(R.id.hour);
        txtMinute = (TextView) findViewById(R.id.minute);
        txtSecond = (TextView) findViewById(R.id.second);
        btnReActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (util.haveNetworkConnection(CouponDetails.this)){
                    Intent i = new Intent(CouponDetails.this, ScanCoupon.class);
                    startActivity(i);
//                    finish();
                }else{
                    util.dialogErrorInternet(CouponDetails.this);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, MyCoupons.class);
        startActivity(i);
//        finish();
    }


    public void changeFont(){
        txtStartDate.setTypeface(util.changeFont(context));
        txtNumCoupons.setTypeface(util.changeFont(context));
        txtEndDate.setTypeface(util.changeFont(context));
        txtPoints.setTypeface(util.changeFont(context));
        txtStatus.setTypeface(util.changeFont(context));
        btnReActive.setTypeface(util.changeFont(context));
        txtDay.setTypeface(util.changeFont(context));
        txtHour.setTypeface(util.changeFont(context));
        txtMinute.setTypeface(util.changeFont(context));
        txtSecond.setTypeface(util.changeFont(context));

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


    public void getData() {

        final Dialogs dialogs = new Dialogs(this);
        dialogs.ProgressDialog();


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constatns.MAIN_API + Constatns.GET_MY_COUPONS + "?uid=" + sharedPreferences.getString("userid", "0"),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e("kasdasda", response.toString());
                            JSONArray jsonArray = response.getJSONArray("aaData");
                            for(int i=0; i < jsonArray.length(); i++){
                                JSONObject js =jsonArray.getJSONObject(i);

                                String b = js.getString("barcode");
                                Log.e("kasdasda222", b.toString());
                                if(b.equals(barcode)){
                                    String d = js.getString("currentdate") + " " + js.getString("currenttime");
                                    String f = js.getString("scanenddate") + " " + js.getString("scanendtime");
                                    scanStartDate = d;
                                    scanEndDate = f;

                                    Log.e("noor", d+" -- "+f);
                                    msg = js.getString("msg");
                                    used = js.getString("used");
                                    countdown(d,f, used, msg);
//                                    if (mCountDown != null) {
//                                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                                        try {
//                                            Date futureDate = dateFormat.parse(f);
//                                            Date currentDate = dateFormat.parse(d);
////                                            Log.e("current date : ", String.valueOf(dateFormat.format(currentDate)));
////                                            Log.e("current date future: ", String.valueOf(futureDate));
//                                            mCountDown.start(currentDate , futureDate);
//                                        } catch (ParseException e) {
//                                            e.printStackTrace();
//                                        }
//
//                                    }

                                    id = js.getString("id");
                                    enddate = js.getString("enddate");
                                    points = js.getString("points");

//                                    delaerID =js.getString("did");
                                    startdate= js.getString("scanenddate");
                                    msg2 = js.getString("msg2");

                                    txtStartDate.setText("تاريخ الاضافة : "+startdate);
                                    if(used.equals("0")){
                                        txtEndDate.setVisibility(View.GONE);
                                    }else{
                                        txtEndDate.setText("تم استخدام هذا الكوبون من "+"");
                                    }
                                    txtNumCoupons.setText("الباركود : "+barcode);
                                    txtPoints.setText("عدد النقاط : "+points);


                                    if(used.equals("1")){
                                        btnReActive.setEnabled(false);
                                        txtStatus.setText("الكوبون مستخدم");
                                        txtStatus.setVisibility(View.GONE);
                                    }else{
                                        btnReActive.setEnabled(true);
                                        txtStatus.setText("حالة الكوبون : غير مستخدم");
                                        txtStatus.setTextColor(getResources().getColor(R.color.blue));
                                    }

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        dialogs.cancelDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

//    public void countDownStart() {
//        handler = new Handler();
//        runnable = new Runnable() {
//            @Override
//            public void run() {
//                handler.postDelayed(this, 1000);
//                try {
////                    getData();
//                    Log.e("bvbvbv", scanStartDate);
//                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//                    Date futureDate = dateFormat.parse(scanEndDate);
//                    Date currentDate = dateFormat.parse(scanStartDate);
//                    if (!currentDate.after(futureDate)) {
//                        long diff = futureDate.getTime()
//                                - currentDate.getTime();
//                        long days = diff / (24 * 60 * 60 * 1000);
//                        diff -= days * (24 * 60 * 60 * 1000);
//                        long hours = diff / (60 * 60 * 1000);
//                        diff -= hours * (60 * 60 * 1000);
//                        long minutes = diff / (60 * 1000);
//                        diff -= minutes * (60 * 1000);
//                        long seconds = diff / 1000;
//                        txtDay.setText(String.valueOf(days)+"\r\n "+"يوم");
//                        txtHour.setText(String.valueOf(hours)+"\r\n "+"ساعة");
//                        txtMinute.setText(String.valueOf(minutes)+"\r\n "+"دقيقة");
//                        txtSecond.setText(String.valueOf(seconds)+"\r\n "+"ثانية");
//                    }else{
//                        btnReActive.setVisibility(View.VISIBLE);
//                        txtMinute.setVisibility(View.GONE);
//                        txtSecond.setVisibility(View.GONE);
//                        txtHour.setVisibility(View.GONE);
//                        txtDay.setText("لقد انتهت مدة صلاحية هذا الكوبون");
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        handler.postDelayed(runnable, 0);
//    }

    public void getAdsImage(){
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, Constatns.MAIN_API + Constatns.ADS,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray obj1Json = response.getJSONArray("aaData");
                            for (int i = 0; i < obj1Json.length(); i++) {
                                try {
                                    JSONObject jsonObject = obj1Json.getJSONObject(i);

                                    String image = jsonObject.getString("image");
//                                    String timer = jsonObject.getString("timer");
                                    sliderimage.add(image);
                                    adapter = new CustomSwipeAdapter(CouponDetails.this, sliderimage);
                                    pagerAdapter = new InfinitePagerAdapter(adapter);
                                    viewPager.setAdapter(pagerAdapter);
                                    pagerAdapter.notifyDataSetChanged();

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
        MySingleton.getInstance(CouponDetails.this).addToRequestQueue(stringRequest);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCountDown.stop();
        if(mSlideMenu.isOpen()) {
            mSlideMenu.close(true);
            mSlideMenu.setSlideMode(SlideMenu.MODE_SLIDE_CONTENT);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCountDown.stop();
    }


    @Override
    protected void onStart() {
        super.onStart();
        StartUp.getInstance().changeLanguage("ar");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCountDown.stop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        StartUp.getInstance().changeLanguage("ar");
    }

    @Override
    protected void onResume() {
        super.onResume();
        StartUp.getInstance().changeLanguage("ar");
    }

}
