package a3lena.a3shak.com.a3shak3lena.Dealers;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.*;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import a3lena.a3shak.com.a3shak3lena.Auth.Login;
import a3lena.a3shak.com.a3shak3lena.Dealers.Comments.CommentAdapter;
import a3lena.a3shak.com.a3shak3lena.Dealers.Comments.CommentModel;
import a3lena.a3shak.com.a3shak3lena.R;
import a3lena.a3shak.com.a3shak3lena.SplashScreen;
import a3lena.a3shak.com.a3shak3lena.slide_menu.BaseSlideMenuActivity;
import a3lena.a3shak.com.a3shak3lena.util.Constatns;
import a3lena.a3shak.com.a3shak3lena.util.MySingleton;
import a3lena.a3shak.com.a3shak3lena.util.util;
import a3lena.a3shak.com.slidemenu.SlideMenu;
import a3lena.a3shak.com.sweet_dialog.SweetAlertDialog;
import at.blogc.android.views.ExpandableTextView;

public class DealerDetails extends BaseSlideMenuActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener
{


    ViewPager viewPageAndroidDetails;
    PagerAdapter adapter;

    // Slider
    private SlideMenu mSlideMenu;
    ImageView btnMenu, btnFb, btnTwitter, btnYoutube, btnWeb, imgLogo;
    TextView txtCall, txtLocation, txtHours, txtFollow, txtCommentCount, buttonToggle;
    Button btnAddComment;
    EditText etComment;
    ExpandableTextView expandableTextView;
    Bundle bundle;
    String id, name, logo, desc, hours, mobile, location, fbLink, twitterLink, youtubeLink, webLink, comment_text, show_comment;
    ArrayList<String> slide_image_list;
    LinearLayout linearLayout;
    RelativeLayout btnCall, btnLocation;
    GPSTracker gps;
    static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;

    ArrayList<CommentModel> commentList;
    CommentAdapter commentAdapter;
    RecyclerView recyclerComment;
    LinearLayout comment_container;
    SharedPreferences sharedPreferences;
    private Handler handler;


    @Override
    public void onContentChanged() {
        super.onContentChanged();
        setSlideRole(R.layout.activity_dealer_details);
        setSlideRole(R.layout.layout_secondary_menu);
        mSlideMenu = getSlideMenu();
        // Slider Toggle
        click_menu();
        init();
        changeFont();
        read_more();
        setData();
        action_dealer();

        adapter = new InfinitePagerAdapter(new CustomSwipeAdapter(this, slide_image_list));
        viewPageAndroidDetails.setAlpha(0.3F);
        viewPageAndroidDetails.setAdapter(adapter);

        for(int i=0; i < slide_image_list.size(); i++){
            Log.e("kqkq",slide_image_list.get(i).toString());
        }

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        initComment();

        if(show_comment.equals("0")){
            comment_container.setVisibility(View.GONE);
        }else{
            comment_container.setVisibility(View.VISIBLE);
        }

        Picasso
            .with(this)
            .load(Constatns.imageLink+logo)
            .into(imgLogo);
    }


    public void init(){
        txtCall = (TextView) findViewById(R.id.txtCall);
        txtLocation = (TextView) findViewById(R.id.txtLocation);
        expandableTextView = (ExpandableTextView) this.findViewById(R.id.expandableTextView);
        buttonToggle = (TextView) this.findViewById(R.id.button_toggle);
        txtHours = (TextView) findViewById(R.id.txtHours);
        txtFollow = (TextView) findViewById(R.id.txtFollow);
        txtCommentCount = (TextView) findViewById(R.id.txtCommentCount);
        btnAddComment = (Button) findViewById(R.id.btnAddComment);
        etComment = (EditText) findViewById(R.id.etComment);
        comment_container = (LinearLayout) findViewById(R.id.comment_container);
        linearLayout = (LinearLayout) findViewById(R.id.container);
//        flipperLayout = (FlipperLayout) findViewById(R.id.flipper_layout);
        btnCall = (RelativeLayout) findViewById(R.id.btnCall);
        btnLocation = (RelativeLayout) findViewById(R.id.btnLocation);
        viewPageAndroidDetails = (ViewPager) findViewById(R.id.viewPageAndroidDetails);
        imgLogo = (ImageView) findViewById(R.id.imgLogo);
        bundle = getIntent().getExtras();

        gps = new GPSTracker(this);

        btnFb = (ImageView) findViewById(R.id.btnFb);
        btnTwitter = (ImageView) findViewById(R.id.btnTwitter);
        btnYoutube = (ImageView) findViewById(R.id.btnYoutube);
        btnWeb = (ImageView) findViewById(R.id.btnWeb);

        findViewById(R.id.btnFb).setOnClickListener(this);
        findViewById(R.id.btnTwitter).setOnClickListener(this);
        findViewById(R.id.btnYoutube).setOnClickListener(this);
        findViewById(R.id.btnWeb).setOnClickListener(this);

        recyclerComment = (RecyclerView) findViewById(R.id.recycler_comment);
        commentList = new ArrayList<CommentModel>();
        commentAdapter = new CommentAdapter(this, commentList);

        sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE);

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

    public void changeFont(){
        txtCall.setTypeface(util.changeFont(this));
        txtLocation.setTypeface(util.changeFont(this));
        expandableTextView.setTypeface(util.changeFont(this));
        buttonToggle.setTypeface(util.changeFont(this));
        txtHours.setTypeface(util.changeFont(this));
        txtFollow.setTypeface(util.changeFont(this));
        txtCommentCount.setTypeface(util.changeFont(this));
        etComment.setTypeface(util.changeFont(this));
        btnAddComment.setTypeface(util.changeFont(this));
    }

    public void setData(){

            name = bundle.getString("name");
            desc = bundle.getString("desc");
            hours = bundle.getString("hours");
            slide_image_list = bundle.getStringArrayList("images");
            logo = bundle.getString("Logo");
            show_comment = bundle.getString("show_comment");
            expandableTextView.setText(desc);
            txtHours.setText("ساعات الدوام : "+hours);
            util.changeTitle(this, name);

            fbLink = bundle.getString("fb");
            twitterLink = bundle.getString("twitter");
            youtubeLink = bundle.getString("youtube");
            webLink = bundle.getString("web");

        if(fbLink.equals("")){
            btnFb.setEnabled(false);
            btnFb.setClickable(false);
            btnFb.setImageAlpha(80);
        }
        if(twitterLink.equals("")){
            btnTwitter.setEnabled(false);
            btnTwitter.setClickable(false);
            btnTwitter.setImageAlpha(80);
        }
        if(youtubeLink.equals("")){
            btnYoutube.setEnabled(false);
            btnYoutube.setClickable(false);
            btnYoutube.setImageAlpha(80);
        }
        if(webLink.equals("")){
            btnWeb.setEnabled(false);
            btnWeb.setClickable(false);
            btnWeb.setImageAlpha(80);
        }

        id = bundle.getString("id");
    }

    public void read_more(){

        expandableTextView.setAnimationDuration(1000L);

        expandableTextView.setInterpolator(new OvershootInterpolator());

        expandableTextView.setExpandInterpolator(new OvershootInterpolator());
        expandableTextView.setCollapseInterpolator(new OvershootInterpolator());

        buttonToggle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                expandableTextView.toggle();
                buttonToggle.setText(expandableTextView.isExpanded() ? "مشاهدة المزيد" : "مشاهدة أقل");
            }
        });

        buttonToggle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                if (expandableTextView.isExpanded())
                {
                    expandableTextView.collapse();
                    buttonToggle.setText("مشاهدة المزيد");
                }
                else
                {
                    expandableTextView.expand();
                    buttonToggle.setText("مشاهدة أقل");
                }
            }
        });

        expandableTextView.setOnExpandListener(new ExpandableTextView.OnExpandListener()
        {
            @Override
            public void onExpand(final ExpandableTextView view)
            {
                Log.d("test", "ExpandableTextView expanded");
            }

            @Override
            public void onCollapse(final ExpandableTextView view)
            {
                Log.d("test", "ExpandableTextView collapsed");
            }
        });
    }


    public void action_dealer() {
        mobile = bundle.getString("mobile");
        location = bundle.getString("lat") + "," + bundle.getString("longitude");
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + mobile));
                startActivity(intent);
            }
        });

        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mGoogleApiClient != null) {
                    if (mGoogleApiClient.isConnected()) {
                        getMyLocation();
                    } else {
                        Toast.makeText(DealerDetails.this,
                                "!mGoogleApiClient.isConnected()", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(DealerDetails.this,
                            "mGoogleApiClient == null", Toast.LENGTH_LONG).show();
                }
            }


        });

    }


    // // TODO: 10/12/2017 : get Current Location
    private void getMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(DealerDetails.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            if (gps.canGetLocation()) {

                String current_location = String.valueOf(mLastLocation.getLatitude() + "," + mLastLocation.getLongitude());
                String uri = "http://maps.google.com/maps?saddr=" + current_location + "&daddr=" + location;
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);

            } else {
                gps.showSettingsAlert();
            }
        }else{
            buildAlertMessageNoGps();
        }
    }

    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("يرجى تفعيل الـ GPS")
                .setCancelable(false)
                .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("لا", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                        //  finish();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(DealerDetails.this,
//                            "permission was granted, :)",
//                            Toast.LENGTH_LONG).show();
                    getMyLocation();

                } else {
//                    Toast.makeText(DealerDetails.this,
//                            "permission denied, ...:(",
//                            Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
//        getMyLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(DealerDetails.this,
                "onConnectionSuspended: " + String.valueOf(i),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(DealerDetails.this,
                "onConnectionFailed: \n" + connectionResult.toString(),
                Toast.LENGTH_LONG).show();
    }



    // Todo : Follow Buttons
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        final int id = view.getId();
        switch (id){
            case R.id.btnFb:
                    openFacebook(fbLink);
                Log.e("sdfsdfs", fbLink);
                break;
            case R.id.btnTwitter:
                    openTwitter(twitterLink);
                break;
            case R.id.btnYoutube:
                    openYoutubeOrWeb(youtubeLink);
                break;
            case R.id.btnWeb:
                openYoutubeOrWeb(webLink);
                break;
        }
    }

    public void openFacebook(String facebookUrl){
        try {
            int versionCode = getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) {
                Uri uri = Uri.parse(facebookUrl);
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            } else {
                // open the Facebook app using the old method (fb://profile/id or fb://page/id)
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/336227679757310")));
            }
        } catch (PackageManager.NameNotFoundException e) {
            // Facebook is not installed. Open the browser
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl)));
        }
    }

    public void openTwitter(String twitterUrl){
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + twitterUrl)));
        }catch (Exception e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/#!/" + twitterUrl)));
        }
    }

    public void openYoutubeOrWeb(String youtubeUrl){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(youtubeUrl));
        startActivity(intent);
    }


    public void initComment(){
        getComment();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DealerDetails.this, LinearLayoutManager.VERTICAL, false);
        recyclerComment.setLayoutManager(linearLayoutManager);
        recyclerComment.setAdapter(commentAdapter);
        recyclerComment.setNestedScrollingEnabled(false);

        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProgressDialog progressDialog = new ProgressDialog(DealerDetails.this);
                progressDialog.setCancelable(false);
                progressDialog.show();
                try{
                    if(util.haveNetworkConnection(DealerDetails.this)){

                        boolean n = sharedPreferences.getBoolean("loggedin", false);
                        if(!n){
                            Intent i = new Intent(DealerDetails.this, Login.class);
                            i.putExtra("from","details");
                            i.putExtra("Logo", logo);
                            i.putStringArrayListExtra("images", slide_image_list);
                            startActivity(i);
                            progressDialog.dismiss();
                        }else{
                            if(etComment.getText().toString().equals("") || etComment.getText().toString().startsWith(" ")){
                                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(DealerDetails.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
                                progressDialog.dismiss();
                                sweetAlertDialog.setTitleText("عذرا !");
                                sweetAlertDialog.setColorTitleText(Color.parseColor("#ea0f07"));
                                sweetAlertDialog.setContentText("يرجى كتابة تعليق");
                                sweetAlertDialog.setCustomImage(R.drawable.logo_menu);
                                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                    }
                                });

                                sweetAlertDialog.show();
                                etComment.setText("");
                            }else{
                                sendComment(progressDialog);
                            }
                        }
                    }else{
                        util.dialogErrorInternet(DealerDetails.this);
                    }
                }catch (Exception e){
                    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(DealerDetails.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
                    progressDialog.dismiss();
                    sweetAlertDialog.setTitleText("لقد حدث خطأ ما");
                    sweetAlertDialog.setColorTitleText(Color.parseColor("#ea0f07"));
                    sweetAlertDialog.setContentText("يرجى إعادة تشغيل التطبيق من جديد");
                    sweetAlertDialog.setCustomImage(R.drawable.logo_menu);
                    sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                        }
                    });
                    etComment.setText("");
                    sweetAlertDialog.show();
                }
            }
        });
    }

    public void sendComment(final ProgressDialog pd){
        comment_text = etComment.getText().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constatns.MAIN_API + Constatns.ADD_COMMENT,
                new Response.Listener<String>() {
                    public static final int TIME_OUT = 15000;


                    @Override
                    public void onResponse(String response) {
                        Log.e("addcomments", response.toString());

                        try {
                            JSONObject obj1Json = new JSONObject(response);
                            JSONObject aaData = obj1Json.getJSONObject("aaData");
                            String Flag = aaData.getString("flag");

                            if(Flag.equals("تم اضافة التعليق")){
                                pd.dismiss();
                                final SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(DealerDetails.this, SweetAlertDialog.SUCCESS_TYPE);
                                sweetAlertDialog.setTitleText("");
                                sweetAlertDialog.setColorTitleText(Color.parseColor("#babd16"));
                                sweetAlertDialog.setContentText("تمت إضافة تعليقك بنجاح");
                                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                    }
                                });
                                sweetAlertDialog.show();
                                sweetAlertDialog.showConfirmButton(false);
                                handler=new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        sweetAlertDialog.dismiss();

                                    }
                                }, TIME_OUT);
                                etComment.setText("");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();

                params.put("uid", sharedPreferences.getString("userid", "0"));
                params.put("dealershipid", id);
                params.put("text", comment_text);
                return params;
            }
        };

        MySingleton.getInstance(DealerDetails.this).addToRequestQueue(stringRequest);
    }

    public void getComment(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constatns.MAIN_API + Constatns.GET_COMMENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("ffffffffff", response);
                        try {
                            JSONObject obj1Json = new JSONObject(response);
                            JSONArray aaData = obj1Json.getJSONArray("aaData");
                            for (int i = 0; i < aaData.length(); i++) {
                                try {
                                    JSONObject jsonObject = aaData.getJSONObject(i);

                                    CommentModel commentModel = new CommentModel();
                                    commentModel.setcDate(jsonObject.getString("Dateofcomment"));
                                    commentModel.setcName(jsonObject.getString("name"));
                                    commentModel.setcDec(jsonObject.getString("dec"));

                                    Log.e("comm22", jsonObject.getString("dec"));
                                    commentList.add(commentModel);
                                    commentAdapter.notifyDataSetChanged();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            String count_comments = String.valueOf(recyclerComment.getAdapter().getItemCount());
                            if(count_comments.equals("0")){
                                txtCommentCount.setText("لا يوجد تعليقات");
                            }else{
                                txtCommentCount.setText(String.valueOf(recyclerComment.getAdapter().getItemCount())+" "+"تعليق");
                            }

//                            Log.e("saaaaaaa", String.valueOf(recyclerPost.getAdapter().getItemCount())+" Commentss");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.err.println(error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("dealerID", id);

                return params;
            }
        };

        MySingleton.getInstance(DealerDetails.this).addToRequestQueue(stringRequest);
    }



}
