package a3lena.a3shak.com.a3shak3lena.Coupons;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.zxing.client.android.Intents;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import a3lena.a3shak.com.a3shak3lena.Coupons.Coupon.AllCouponsAdapter;
import a3lena.a3shak.com.a3shak3lena.Coupons.Coupon.AllCouponsModel;
import a3lena.a3shak.com.a3shak3lena.R;
import a3lena.a3shak.com.a3shak3lena.slide_menu.BaseSlideMenuActivity;
import a3lena.a3shak.com.a3shak3lena.util.*;
import a3lena.a3shak.com.slidemenu.SlideMenu;
import a3lena.a3shak.com.sweet_dialog.SweetAlertDialog;

public class MyCoupons extends BaseSlideMenuActivity {

    // Slider
    private SlideMenu mSlideMenu;
    ImageView btnMenu;

    RelativeLayout btnAddCoupon;
    RecyclerView recycler_all_coupons;
    ArrayList<AllCouponsModel> itemsModels;
    Bundle bundle;
    AllCouponsAdapter adapter;
    SharedPreferences sharedPreferences;
    TextView txtAddCoupon, txtNoCoupon;
    Activity context =this;

    Handler handler;
    Runnable runnable;




    @Override
    public void onContentChanged() {
        super.onContentChanged();
        StartUp.getInstance().changeLanguage("ar");
        setSlideRole(R.layout.activity_my_coupons);
        setSlideRole(R.layout.layout_secondary_menu);
        mSlideMenu = getSlideMenu();
        // Slider Toggle
        click_menu();
        util.changeTitle(this,"كوبوناتي");
        init();
        changeFont();

        handler = new Handler();
        sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE);

        itemsModels = new ArrayList<>();
        adapter = new AllCouponsAdapter(this, itemsModels);
        recycler_all_coupons = (RecyclerView) findViewById(R.id.recycler_all_coupons);
        LinearLayoutManager mLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler_all_coupons.setLayoutManager(mLayoutManager2);
        recycler_all_coupons.setHasFixedSize(true);
        recycler_all_coupons.setItemAnimator(new DefaultItemAnimator());
        recycler_all_coupons.setAdapter(adapter);

        runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(runnable, 7000);
                getMyCoupons(MyCoupons.this, adapter,handler, runnable);
//                util.getDealer(getActivity(),"2",list, adapter, handler, runnable);
            }
        };
        handler.postDelayed(runnable, 0);


        btnAddCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(util.haveNetworkConnection(MyCoupons.this)){
                    Intent i = new Intent(MyCoupons.this, ScanCoupon.class);
                    i.putExtra("from", "MyCoupons");
                    startActivity(i);
                }else{
                    util.dialogErrorInternet(MyCoupons.this);
                }
            }
        });
    }

    public void init() {
        btnAddCoupon = (RelativeLayout) findViewById(R.id.btnAddCoupon);
        recycler_all_coupons = (RecyclerView) findViewById(R.id.recycler_all_coupons);
        txtAddCoupon = (TextView) findViewById(R.id.txtAddCoupon);
        txtNoCoupon = (TextView) findViewById(R.id.txtNoCoupon);
    }

    public void changeFont(){
        txtAddCoupon.setTypeface(util.changeFont(this));
        txtNoCoupon.setTypeface(util.changeFont(this));
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

    public void getMyCoupons(Activity activity, final AllCouponsAdapter adapter, final Handler handler, final Runnable runnable) {

        final Dialogs dialogs = new Dialogs(activity);
        dialogs.ProgressDialog();

//        final SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
//        pDialog.getProgressHelper().setBarColor(Color.parseColor("#babd16"));
//        pDialog.setTitleText("يرجى الانتظار ...");
//        pDialog.setColorTitleText(Color.parseColor("#000000"));
//        pDialog.setCancelable(true);
//        pDialog.show();

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constatns.MAIN_API + Constatns.GET_MY_COUPONS + "?uid=" + sharedPreferences.getString("userid", "0"),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("toto", response.toString());
                        if(response.toString().contains("لا يوجد كوبونات")){
                            txtNoCoupon.setVisibility(View.VISIBLE);
                            recycler_all_coupons.setVisibility(View.GONE);
                        }else{
                            txtNoCoupon.setVisibility(View.GONE);
                            recycler_all_coupons.setVisibility(View.VISIBLE);
                        }
                        try {
                            JSONArray jsonArray = response.getJSONArray("aaData");


                            for (int i = 0; i < jsonArray.length(); i++) {
                                Log.e("counss", "Count is : "+ String.valueOf(i));
                                AllCouponsModel allCouponsModel = new AllCouponsModel();
                                JSONObject js = jsonArray.getJSONObject(i);
                                String id = js.getString("id");
                                String barcode = js.getString("barcode");
                                String enddate = js.getString("enddate");
                                String serialnumber = js.getString("serialnumber");
                                String points = js.getString("points");
                                String used = js.getString("used");
                                String flag = js.getString("flag");
                                String did = js.getString("did");
                                String AddDateTime = js.getString("AddDateTime");
                                String scandate = js.getString("scandate");
                                String scantime = js.getString("scantime");
                                String scanenddate = js.getString("scanenddate");
                                String scanendtime = js.getString("scanendtime");
                                String currentdate = js.getString("currentdate");
                                String currenttime = js.getString("currenttime");
                                String msg = js.getString("msg");
                                String msg2 = js.getString("msg2");

                                allCouponsModel.setId(id);
                                allCouponsModel.setBarcode(barcode);
                                allCouponsModel.setEnddate(enddate);
                                allCouponsModel.setSerialnumber(serialnumber);
                                allCouponsModel.setPoints(points);
                                allCouponsModel.setUsed(used);
                                allCouponsModel.setFlag(flag);
                                allCouponsModel.setDid(did);
                                allCouponsModel.setAddDateTime(AddDateTime);
                                allCouponsModel.setScandate(scandate);
                                allCouponsModel.setScantime(scantime);
                                allCouponsModel.setScanenddate(scanenddate);
                                allCouponsModel.setScanendtime(scanendtime);
                                allCouponsModel.setCurrentdate(currentdate);
                                allCouponsModel.setCurrenttime(currenttime);
                                allCouponsModel.setMsg(msg);
                                allCouponsModel.setMsg2(msg2);
                                itemsModels.add(allCouponsModel);
                                adapter.notifyDataSetChanged();
                                dialogs.cancelDialog();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        handler.removeCallbacksAndMessages(null);
                        handler.removeCallbacks(runnable);

//                        pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    @Override
    protected void onStop() {
        super.onStop();
        StartUp.getInstance().changeLanguage("ar");
        if(mSlideMenu.isOpen()) {
            mSlideMenu.close(true);
            mSlideMenu.setSlideMode(SlideMenu.MODE_SLIDE_CONTENT);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        StartUp.getInstance().changeLanguage("ar");
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
