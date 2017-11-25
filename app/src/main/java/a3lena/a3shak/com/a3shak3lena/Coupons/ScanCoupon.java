package a3lena.a3shak.com.a3shak3lena.Coupons;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import a3lena.a3shak.com.a3shak3lena.Main;
import a3lena.a3shak.com.a3shak3lena.R;
import a3lena.a3shak.com.a3shak3lena.util.Constatns;
import a3lena.a3shak.com.a3shak3lena.util.Dialogs;
import a3lena.a3shak.com.a3shak3lena.util.MySingleton;
import a3lena.a3shak.com.a3shak3lena.util.StartUp;
import a3lena.a3shak.com.a3shak3lena.util.util;
import pl.droidsonroids.gif.GifImageView;

public class ScanCoupon extends AppCompatActivity {

    String uid;
    SharedPreferences sharedPreferences;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getIntent().getExtras();
        sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE);

        if (isPermissionCamera()) {
            getP();
        }
    }

    public void checkCouponScan(final String b) {

        Log.e("bobo", b.toString());
        uid = sharedPreferences.getString("userid", "0");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constatns.MAIN_API + Constatns.SCAN_COUPON,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("coupon_status", response);

                            JSONObject obj1Json = new JSONObject(response);
                            Log.e("coco", obj1Json.toString());
                            String idCoupon = obj1Json.getString("id");//

                            String barcode = obj1Json.getString("barcode");
                            String enddate = obj1Json.getString("enddate");
                            String serialnumber = obj1Json.getString("serialnumber");
                            String points = obj1Json.getString("points");
                            String used = obj1Json.getString("used");
                            String msg = obj1Json.getString("msg");
                            String f = obj1Json.getString("f");
                            Log.e("soao", "this is : "+used .toString());

                            Dialogs dialogs = new Dialogs(ScanCoupon.this);
                            if(f.equals("0")){
                                dialogs.error_scan_dialog("عذرا !",msg,ScanCoupon.this);
                            }else {
                                Intent i = new Intent(ScanCoupon.this, CouponDetails.class);
                                i.putExtra("barcode", b);
                                startActivity(i);
                                finish();
                            }
                            String flag = obj1Json.getString("f");
                            String msg2 = obj1Json.getString("msg2");
                            String did = obj1Json.getString("did");
                            String scanenddate = obj1Json.getString("scanenddate");
                            String scanendtime = obj1Json.getString("scanendtime");
                            String currentdate = obj1Json.getString("currentdate");
                            String currenttime = obj1Json.getString("currenttime");

//                            Intent i = new Intent(ScanCoupon.this,ScanCouponDetails.class);
//                            i.putExtra("id", idCoupon);
//                            i.putExtra("barcode", barcode);
//                            Log.i("SSSSSSSSS",barcode);
//                            i.putExtra("enddate", enddate);
//                            i.putExtra("serialnumber", serialnumber);
//                            i.putExtra("points", points);
//                            i.putExtra("used", used);
//                            i.putExtra("flag", flag);
//                            i.putExtra("msg", msg);
//                            i.putExtra("did", did);
////                            i.putExtra("scandate", scandate);
////                            i.putExtra("scantime", scantime);
//                            i.putExtra("scanenddate", scanenddate);
//                            i.putExtra("scanendtime", scanendtime);
//                            i.putExtra("currentdate", currentdate);
//                            i.putExtra("currenttime", currenttime);
//                            i.putExtra("msg2", msg2);
//                            finish();
//
//                            startActivity(i);



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
                Log.e("bobob", b+" -- "+ uid);
                params.put("barcode", b);
                params.put("type", "1");
                params.put("uid", uid);
                return params;
            }
        };
        MySingleton.getInstance(ScanCoupon.this).addToRequestQueue(stringRequest);
    }

    private void getP() {
        new IntentIntegrator(this).initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(util.haveNetworkConnection(ScanCoupon.this)){
            if (result != null) {
                if (result.getContents() == null) {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Log.e("zeaben", "this is oo  "+ result.getContents().toString());
                    checkCouponScan(result.getContents().toString());
//                Intent i = new Intent(ScanCoupon.this, CouponDetails.class);
//                i.putExtra("barcode", result.getContents().toString());
//                startActivity(i);
//                finish();


                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }else{
            dialogErrorInternet(ScanCoupon.this);
        }

    }

    public static void dialogErrorInternet(final Activity activity){
        LayoutInflater inflater = activity.getLayoutInflater();
        View v = inflater.inflate(R.layout.check_internet_connection, null);

        GifImageView gifImageView = v.findViewById(R.id.gif_internet);
        TextView txtInternet = (TextView) v.findViewById(R.id.txtinternet);
        Button btnRetry = (Button) v.findViewById(R.id.btnRetry);

        txtInternet.setTypeface(util.changeFont(activity));
        btnRetry.setTypeface(util.changeFont(activity));

        final Dialog dialog = new Dialog(activity, R.style.DialogThemeWhite);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(util.haveNetworkConnection(activity)){
                    dialog.dismiss();
                    activity.finish();
                }
            }
        });
        dialog.setContentView(v);
        dialog.show();
        Window window_register = dialog.getWindow();
        window_register.setLayout(ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT);
    }

    public boolean isPermissionCamera() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (this.checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Log.v("kha", "Permission is granted");
                return true;
            } else {
                Log.v("kha", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("kha", "Permission is granted");
            return true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];
                StartUp.getInstance().changeLanguage("ar");

                if (permission.equals(android.Manifest.permission.CAMERA)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        getP();
                    }
                }
            }
        }

    }

}
